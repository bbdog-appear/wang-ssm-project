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
                    List<Object> objects = redisUtil.rPushList(redisKey, goodsNos, 100);
                    System.out.println(objects);

//                    List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
//                    System.out.println(range);
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }
        }
    }

    /**
     * 重要的问题：以后可以说，开发过程中遇到哪些问题，怎么分析问题的，怎么解决的。
     * 这种通过先计算该商品类型下总共有多少个redisKey，然后再计算本次需要发的商品数量需要几个redisKey，最后再循环redisKey列表几次
     * ，这种方式是有问题的：如果1000个线程并发进来发货，那么1000个线程同时都读到该商品类型总共有10个redisKey，然后计算出都需要4个
     * redisKey，那么第一个线程先对redisKey循环4次，把前4个redisKey里面的value都给pop出来了，第二个线程也计算出需要4个，所以也对
     * 总redisKey循环4次，但是第1个线程已经把前4个redisKey都pop完了，第2个线程pop出来为空，然后就会抛异常：因为并发，第二个线程发
     * 货失败，因为需要的数和查到的数不一致。但是实际上呢，第一个线程虽然把前4个redisKey都pop完了，但是还剩6个redisKey，不应该抛异
     * 常的，应该把剩下的6个key中的4个key pop出去。所以以此类推，999个线程同时都抛异常出去。这就会造成一个问题，在前一秒内，第二个线
     * 程发不了货，但是过了一秒，这个线程又可以发货了，就会出现隔一秒可以发货，隔一秒又不能发货。
     * 解决办法：不查该商品类型下总共有多少个redisKey，也不计算本次需要发的商品数量需要几个redisKey，直接定义一个常量，比如10，固定
     * 循环10次，第一次从第一个redisKey中pop元素，比如这次要发货25个，那么第一次循环，从第一个redisKey中通过管道pop，pop25个，但是
     * 实际上就10个元素，所以pop10个，再拿25-10=15，结果大于0，就进行下一次循环，下一次从第二个redisKey中pop15，实际上pop10个，然后
     * 第三次从第三个redisKey中pop5个，如果pop出的总量等于本次的量，就跳出循环。还有中情况，第一个redisKey上次已经被发过了，那么第一
     * 各redisKey pop0个，就继续从下一个key pop，如果都循环完了，pop出的元素为0，就从数据库中查，如果pop元素不为0，但是不够，就报货
     * 不够。
     * 总结：就跟查数据库一样，查出数据库的值在内存中，但是下一秒数据库中的数据变了，但是jvm中是不感知的，所以在查询的时候要加读锁，
     * 这样其他的接口写这个数据的时候，就会被锁，保证数据的稳定性。所以在查redis的时候，也一样，查询redis，将数据放在jvm内存中，再进行
     * redis的其他操作，如变更、删除(pop)，那么并发情况下，这个数据肯定是不准的。所以操作redis时，不能先查，再对其进行变更，需要直接
     * 操作redis中的数据。数据库中的乐观锁同样也是这种，update状态为无效where状态为有效，那么这个where操作在数据库中是个查询操作，
     * 但是数据库已经加了锁空值，也就是行锁，两个线程进来，只有一个线程在同一时刻执行这条sql，另一个线程再执行的话，状态已经改变了。
     * 就相当于把并发这种问题用乐观锁，将问题抛给了数据库层面，让它去加锁解决。redis也是的，单线程的，pop操作，是线程安全的。
     */
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
//            Set<String> keys = redisUtil.getKeys("wangcheng_" + category + "_*");
            
            // 上面的keys命令在生产中禁用的，因此，用while循环，当最后一个key不存在时，跳出循环
            List<String> keys = new ArrayList<>();
            int keySubscript = 0;
            while (true){
                Boolean aBoolean = redisTemplate.hasKey("wangcheng_" + category + "_" + keySubscript);
                if (aBoolean) {
                    break;
                }
                if (keySubscript > 20) {
                    break;
                }
                keySubscript++;
            }

            while (true){
                Boolean aBoolean = redisTemplate.hasKey("wangcheng_" + category + "_" + keySubscript);
                if (!aBoolean) {
                    break;
                }
                String key = "wangcheng_" + category + "_" + keySubscript;
                keys.add(key);
                keySubscript++;
            }
            
            if (CollectionUtils.isEmpty(keys)) {
                log.error("redis中，该商品类型【" + category + "】下key不存在");
                return;
            }
            //正序排序
//            keys = keys.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

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

            // 定义本次实际需要发的商品编号集合
            List<String> resultValue = new ArrayList<>();

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

            // 考虑并发情况下，如果，需要发的量与实际发的量不一致，那么说明产生并发问题，第二个线程开始读到的key
            // 是脏数据，但是前几个key已经被第一个线程发过了，这时候第二个线程后面循环发的时候，前几个key都是空，
            // 接口返回的实际货数量不够，但是不会造成超发问题。可以直接抛异常出去。如果第二个线程需要更多的key，
            // 则还是会从redis中返回一部分商品编号出去的，但是不更新数据库，后面补偿redis的时候，还会将该商品编号
            // 补偿进去。
            if (resultValue.size() != categoryNum) {
                log.error("并发情况下发货失败，请稍后重试");
                return;
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
