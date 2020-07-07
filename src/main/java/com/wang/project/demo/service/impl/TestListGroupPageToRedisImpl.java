package com.wang.project.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.dto.CategoryDTO;
import com.wang.project.demo.service.TestListGroupPageToRedis;
import com.wang.project.demo.util.RedisUtil;
import com.wang.project.demo.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 测试list集合根据某个字段分组，并分页插入redis中
 * User:wangcheng Date:2020/7/2 15:02 ProjectName:TestListGroupPageToRedisImpl Version:1.0
 **/
@Service
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
                int pageSize = 5;
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
//                    Long result = redisTemplate.opsForList().rightPush(redisKey, goodsNos);
//                    System.out.println(result);

                    //第二种插入redis方案，例子：[10001,10002,10003]，(redis的list结构循环插入每一个元素，因为是字符串，所以没有[])
//                    goodsNos.forEach(goodsNo -> redisTemplate.opsForList().rightPush(redisKey, goodsNo));

                    //第二种循环插入会造成频繁的redis连接断开，所以使用管道批量插入
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
            List<Object> objects = redisUtil.rPopList(category, categoryNum);
            System.out.println(objects);
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
        goods2.setCategoryNum(6);
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
