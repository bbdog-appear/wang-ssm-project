package com.wang.project.demo.service.impl;

import com.wang.project.demo.dto.CategoryDTO;
import com.wang.project.demo.service.TestListGroupPageToRedis;
import com.wang.project.demo.util.RedisUtil;
import com.wang.project.demo.vo.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description 测试list集合根据某个字段分组，并分页插入redis中
 * User:wangcheng Date:2020/7/2 15:02 ProjectName:TestListGroupPageToRedisImpl Version:1.0
 **/
@Service
@Slf4j
public class TestListGroupPageToRedisImpl implements TestListGroupPageToRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void testListGroupPageToRedis() {
        List<Goods> goodsList = getGoodsList();
        if (!CollectionUtils.isEmpty(goodsList)) {
            //根据商品分类进行分组(过滤掉以null为key的组)
            Map<String, List<Goods>> map = goodsList.stream().
                    filter(goods -> !StringUtils.isEmpty(goods.getCategory())).
                    collect(Collectors.groupingBy(Goods::getCategory));
            //遍历方式3：推荐，尤其是容量大时
            for (Map.Entry<String, List<Goods>> entry: map.entrySet()) {
                //获取每种商品下的商品详情，并按照商品编号进行正序。如果为倒序则sorted(comparator.reversed())
                List<Goods> value = entry.getValue().stream().
                        filter(goods -> !StringUtils.isEmpty(goods.getGoodsNo())).
                        sorted(Comparator.comparing(Goods::getGoodsNo)).
                        collect(Collectors.toList());
                //总数
                int totalNum = value.size();
                //每页大小
                int pageSize = 2;
                //总页数
                int pageNum = totalNum/pageSize;
                if (totalNum % pageSize != 0) {
                    pageNum += 1;
                }
                for (int i = 0; i < pageNum; i++) {
                    System.out.println("================================================");
                    //对list进行分页(每页1000条)
                    List<Goods> collect = value.stream().skip(i*pageSize).limit(pageSize).collect(Collectors.toList());
                    //获取商品编号，准备存入redis
                    List<String> goodsNos = collect.stream().map(Goods::getGoodsNo).collect(Collectors.toList());

                    //入redis中，规则：key：wangcheng_10001_(i+1), value：List<String> goodsNos， timeout：1小时（暂时不填）
                    String redisKey = "wangcheng_" + collect.get(0).getCategory() + "_" + (i + 1);

                    //每次入redis之前，先删除这个key
                    redisTemplate.delete(redisKey);

                    //第一种插入redis方案，例子：[[10001,10002,10003],[10004]]，(直接往redis的list结构中插入一个list对象)
                    // TODO: 2020/7/9 这种方案容易出问题，因为这是发货商品，所以存在并发情况，比如干货类型的商品第一次发10件货
                    //  ，第二次发20件货，但是对这个商品类型不能加锁，否则就会影响效率。所以第一次和第二次如果并发进来，第一次从
                    //  redis中一次读出一个key(也就是一个元素，即一个集合)，这些是准备发货的。然后第二次进来也读到这个key，那么
                    //  第一次发货成功后，第二次又拿着这批商品发货一次，导致超卖现象。
                    //  所以这种方案不行。
//                    Long result = redisTemplate.opsForList().rightPush(redisKey, goodsNos);
//                    System.out.println(result);

                    //第二种插入redis方案，例子：[10001,10002,10003]，(redis的list结构循环插入每一个元素，因为是字符串，所以没有[])
                    // TODO: 2020/7/8 注意：这里用RedisTemplate直接push的话，因为valueSerializer在redis配置里是
                    //  JdkSerializationRedisSerializer，保存到redis客户端里是：\xAC\xED\x00\x05t\x00\x0810002001
//                    goodsNos.forEach(goodsNo -> redisTemplate.opsForList().rightPush(redisKey, goodsNo));

                    //第二种循环插入会造成频繁的redis连接断开，所以使用管道批量插入
                    // TODO: 2020/7/8 注意：如果用RedisTemplate的回调函数，里面进行了自定义的序列化(字符串)，所以保存
                    //  到redis客户端里是：10002001
                    //  所以后面根据key取值的时候，就不能用redisTemplate直接取，因为这样取的值反序列化规则
                    //  是JdkSerializationRedisSerializer，而存的时候的序列化规则是自定义的(字符串)，所以
                    //  这种方式取值，应该在回调函数的另一个参数指定一下，返回值是StringSerializer
                    List<Object> objects = redisUtil.rPushList(redisKey, goodsNos);
                    System.out.println(objects);

//                    List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
//                    System.out.println(range);
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }
        }
    }

    @Override
    public void removeListRedis() {
        CategoryDTO categoryDTO = getCategoryDTO();
        List<Goods> goodsList = categoryDTO.getGoodsList();
        goodsList.forEach(goods -> {
            //请求的商品类型
            String category = goods.getCategory();
            //请求的某种类型的数量
            int categoryNum = goods.getCategoryNum();

            // TODO: 2020/7/9 并发情况下，这种，同一个商品类型进来发货，同时读到还有多少个key，也会出问题。
            //  需要加一个写锁，等第一个线程 读、更新完，再允许第二个线程进来读，这时候才是准确的。
            // 获取这个商品类型下所有的key
            Set<String> keys = redisUtil.getKeys("wangcheng_" + category + "_*");
            if (CollectionUtils.isEmpty(keys)) {
                log.error("redis中，该商品类型【" + category + "】下key不存在");
                return;
            }
            //正序排序
            keys = keys.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

            List<String> resultValue = new ArrayList<>();

            // 该商品类型的库存总量
            int totalSize = 0;
            // 循环次数(需要key的数量)
            int frequency = 0;

            // 计算需要的库存value总量、需要的库存key总数
            for (String redisKey : keys) {
                frequency += 1;
                int size = redisTemplate.opsForList().size(redisKey).intValue();
                totalSize += size;
                if (categoryNum <= totalSize){
                    break;
                }
            }
            //本次需要发货的量
            System.out.println("本次需要发货的value量：" + categoryNum);
            //总共需要key中的value的总数量
            System.out.println("库存中查出的value总量：" + totalSize);
            //总共需要key中数量
            System.out.println("库存中需要key的数量：" + frequency);

            // 如果所有的key都遍历完后，请求需要发货的量还是大于库存的量，就抛异常
            if (categoryNum > totalSize) {
                log.error("redis中，该商品类型【" + category + "】下库存不足");
                return;
            }

            // 计数器
            int counterAll = 0;
            // 需要全部发货的key
            for (String redisKey : keys) {
                counterAll += 1;
                // 情况1：如果本次需要发货的量等于库存中需要的量，则这些key需要全部都发货
                if (categoryNum == totalSize){
                    // 情况1下，发货frequency次后跳出循环
                    if (counterAll > frequency) {
                        break;
                    }
                    int size = redisTemplate.opsForList().size(redisKey).intValue();
                    List<String> objects = redisUtil.rPopList(redisKey, size);
                    resultValue.addAll(objects);
                }
                // 情况2：如果本次需要发货的量小于库存中需要的量，则这些key-1前的全部发货，最后一次key部分发货
                else {
                    // key-1次前全部发货
                    if (counterAll < frequency) {
                        int size = redisTemplate.opsForList().size(redisKey).intValue();
                        List<String> objects = redisUtil.rPopList(redisKey, size);
                        resultValue.addAll(objects);
                    }
                    // 最后一次key部分发货
                    else if (counterAll == frequency) {
                        // 剩余需要发货的量 = 需要发的量 - 已经发的量
                        int remainingNum = categoryNum - resultValue.size();
                        List<String> objects = redisUtil.rPopList(redisKey, remainingNum);
                        resultValue.addAll(objects);
                    }
                    // 发完后跳出循环
                    else {
                        break;
                    }
                }
            }

            //需要返回的商品编号
            System.out.println("本次需要卖出的商品编号：" + resultValue);
        });
    }

    @Override
    public void testOtherListRedisOpr() {
        String redisKey = "wangcheng_dryCargo_1";
        List<String> list = Arrays.asList("11111", "22222");
        List<Object> range1 = redisTemplate.opsForList().range(redisKey, 0, -1);
        System.out.println(range1);
        if (!CollectionUtils.isEmpty(range1)) {
            // TODO: 2020/7/3 这个list1可能是[]
            List<String> list1 = (List<String>) range1.get(0);
            System.out.println(list1);
        }
//        redisTemplate.opsForList().rightPushIfPresent(redisKey, list);
//        redisTemplate.opsForList().rightPush(redisKey, list);
//        Long size = redisTemplate.opsForList().size(redisKey);
//        Object o = redisTemplate.opsForList().rightPop(redisKey);
//        System.out.println(o);
        List<Object> range2 = redisTemplate.opsForList().range(redisKey, 0, -1);
        System.out.println(range2);
    }

//    @Override
//    public void testRedisListPush() {
//        String key = "testGoodsKey";
//        List<String> goodsNos = Arrays.asList("10002001", "10002002");
//        goodsNos.forEach(value -> redisTemplate.opsForList().rightPush(key, value));
//    }

    /**
     * 临时方法
     *
     * @param
     * @return void
     **/
    private void temporary(){
        String redisKey = "wangcheng_dryCargo_1";
//        List<String> goodsNos = Arrays.asList("10002001", "10002002");
        /*
         * 这里注意，redisTemplate.opsForList().rightPush(redisKey, list);这里存到redis中的数据结构是
         * [[10002001,10002002]]，意思是redis里有一个List，本次存的list，是放在redis中的List中的，所以
         * 获取的时候redisTemplate.opsForList().range(redisKey, 0, 0);意思是获取redis中的List中的第
         * 0个元素，也就是上次存的list。
         */
        List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
        System.out.println(range);
        List<String> list = (List<String>) range.get(0);
        System.out.println(list);

//        Long remove = redisTemplate.opsForList().remove(redisKey, -1, range.get(0));
//        System.out.println(remove);
        List<Object> range2 = redisTemplate.opsForList().range(redisKey, 0, -1);
        System.out.println(range2);


        //临时方法1：
//        List<Object> objects = redisUtil.rPopList(category, categoryNum);
//            System.out.println(objects);

//            int i = 1;
//            List<String> list = new ArrayList<>();
//            String redisKey = "wangcheng_"+ category +"_" + i;
//            // TODO: 2020/7/3 有可能查出来的是空,那么就会报错
//            List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
//            // TODO: 2020/7/3 这个range可能是[[]],所以得到第一个就是[]，所以list1可能为空集合，所以这里也要判断一下，不能get(0)
//            List<String> list1 = (List<String>) range.get(0);
//            list.addAll(list1);
//            while (true){
//                //如果请求中需卖出的商品数大于查出来的数量，则查询该商品
//                //类型的下一个key。比如要卖5件，一个key里有2件，那么当i = 3时(第3个key)，5 < 6，跳出while循环。
//                //这时候list里是6件，然后需要卖5件，从这个list中去掉5件，剩下的1件，是循环到最后一个key里的。
//                //所以把前2个key都给删了，第3个key覆盖一下。
//                if (categoryNum > list.size()) {
//                    i = i + 1;
//                    String redisKey2 = "wangcheng_"+ category +"_" + i;
//                    // TODO: 2020/7/3 有可能查出来是空
//                    List<Object> range2 = redisTemplate.opsForList().range(redisKey2, 0, -1);
//                    List<String> list2 = (List<String>) range2.get(0);
//                    list.addAll(list2);
//                } else {
//                    break;
//                }
//            }
//            //需要卖出的商品编号
//            List<String> list2 = list.subList(0, categoryNum - 1);
//            //最后一个key中剩余的商品编号
//            List<String> collect = list.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());


        //临时方法2：
//        CategoryDTO categoryDTO = getCategoryDTO();
//        List<Goods> goodsList = categoryDTO.getGoodsList();
//        goodsList.forEach(goods -> {
//            //请求的商品类型
//            String category = goods.getCategory();
//            //请求的某种类型的数量
//            int categoryNum = goods.getCategoryNum();
//
//            // 获取这个商品类型下所有的key
//            Set<String> keys = redisUtil.getKeys("wangcheng_" + category + "_*");
//            if (CollectionUtils.isEmpty(keys)) {
//                log.error("redis中，该商品类型【" + category + "】下不存在商品");
//                return;
//            }
//
//            List<String> resultValue = new ArrayList<>();
//
//            for (String redisKey : keys) {
//
//                // 如：wangcheng_category_188 -> wangcheng_category
//                String subRedisKeyPre = redisKey.substring(0, redisKey.lastIndexOf("_"));
//                // 如：wangcheng_category_188 -> 188
//                String subRedisKeySuf = redisKey.substring(redisKey.lastIndexOf("_") + 1);
//                int subRedisKeySufInt = Integer.parseInt(subRedisKeySuf);
//
//                int i = subRedisKeySufInt;
//                int size = redisTemplate.opsForList().size(redisKey).intValue();
//                while (true){
//                    //注意一点，如果所有的key的value总数量也小于本次需要发的总数量，那么就直接抛异常
//                    if (categoryNum > size) {
//                        i = i + 1;
//                        redisKey = subRedisKeyPre + "_" + i;
//                        //测试一下key如果不存在，返回值是什么
//                        int size2 = redisTemplate.opsForList().size(redisKey).intValue();
//                        // 如果key没有的情况下，还不够卖出这些商品，那么就可以退出循环，并且抛异常出去。
//                        if (size2 <= 0) {
//                            log.error("redis中，该商品类型【" + category + "】下库存不足");
//                            break;
//                        }
//                        size = size + size2;
//                    }else {
//                        break;
//                    }
//                }
//                //本次需要发的量
//                System.out.println(categoryNum);
//                //总共需要key中的value的总数量
//                System.out.println(size);
//                //需要的key的数量
//                System.out.println(i - subRedisKeySufInt);
//
//
//                //进入循环中的key，都是全部需要发的
//                for (int j = subRedisKeySufInt; j < i; j++) {
//                    String redisKey2 = subRedisKeyPre + "_" + j;
//                    int size3 = redisTemplate.opsForList().size(redisKey2).intValue();
//                    List<String> objects = redisUtil.rPopList(redisKey2, size3);
//                    resultValue.addAll(objects);
//                }
//                //最后一个需要发的key，判断本次需要发的总量是否大于前几个key中value的总量，
//                // 如果是，则还需要从最后一个key中取value值
//                if (categoryNum > resultValue.size()) {
//                    String redisKey2 = subRedisKeyPre + "_" + i;
//                    //剩余需要发的量，从最后一个key中拿值
//                    int remainingNum = categoryNum - resultValue.size();
//                    List<String> objects = redisUtil.rPopList(redisKey2, remainingNum);
//                    resultValue.addAll(objects);
//                }
//            }
//            //需要返回的商品编号
//            System.out.println(resultValue);
//        });
    }

    private List<Goods> getGoodsList(){
        Goods goods1 = new Goods();
        goods1.setCategory("drinks");
        goods1.setGoodsNo("10001001");
        Goods goods2 = new Goods();
        goods2.setCategory("drinks");
        goods2.setGoodsNo("10001002");

        Goods goods3 = new Goods();
        goods3.setCategory("dryCargo");
        goods3.setGoodsNo("10002001");
        Goods goods4 = new Goods();
        goods4.setCategory("dryCargo");
        goods4.setGoodsNo("10002002");
        Goods goods5 = new Goods();
        goods5.setCategory("dryCargo");
        goods5.setGoodsNo("10002003");
        Goods goods6 = new Goods();
        goods6.setCategory("dryCargo");
        goods6.setGoodsNo("10002004");
        Goods goods7 = new Goods();
        goods7.setCategory("dryCargo");
        goods7.setGoodsNo("10002005");
        Goods goods8 = new Goods();
        goods8.setCategory("dryCargo");
        goods8.setGoodsNo("10002006");

        Goods goods9 = new Goods();
        goods9.setCategory(null);
        goods9.setGoodsNo("10003001");
        Goods goods10 = new Goods();
        goods10.setCategory("");
        goods10.setGoodsNo("10004001");
        return Arrays.asList(goods1, goods2, goods3, goods4, goods5, goods6, goods7, goods8, goods9, goods10);
    }

    /**
     * @Author c_wangcheng-007
     * @Description 获取每种商品下所要卖出的数量
     * @Date 23:40 2020/7/2/002
     * @Param []
     * @return com.wang.project.demo.dto.CategoryDTO
     **/
    private CategoryDTO getCategoryDTO(){
        CategoryDTO categoryDTO = new CategoryDTO();
//        Goods goods1 = new Goods();
//        goods1.setCategory("drinks");
//        goods1.setCategoryNum(1);
        Goods goods2 = new Goods();
        goods2.setCategory("dryCargo");
        goods2.setCategoryNum(1);
        categoryDTO.setGoodsList(Arrays.asList(goods2));
//        categoryDTO.setGoodsList(Arrays.asList(goods1, goods2));
        return categoryDTO;
    }

    /**
     * HashMap的几种遍历方式
     *
     * @param
     * @return void
     **/
    private void testHashMapFor(){
        Map<String, List<Goods>> map = new HashMap<>();
        //遍历方式1：
        Set<String> set = map.keySet();
        for (String key : set) {
            List<Goods> list = map.get(key);
        }
        //遍历方式2：
        Iterator<Map.Entry<String, List<Goods>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<Goods>> next = iterator.next();
            String key = next.getKey();
            List<Goods> value = next.getValue();
        }
    }

    /**
     * @Author c_wangcheng-007
     * @Description 测试list的addAll操作
     * @Date 0:17 2020/7/3/003
     * @Param [args]
     * @return void
     **/
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        System.out.println(list);
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        list1.add("4");
        System.out.println(list1);
        List<String> list2 = new ArrayList<>();
        list2.add("5");
        list2.add("6");
        list2.add("7");
        System.out.println(list2);
//        boolean b = list1.addAll(list2);
        boolean b = list.addAll(list1);
        boolean b1 = list.addAll(list2);
        System.out.println(list);
        System.out.println(b + "," + b1);
    }



}
