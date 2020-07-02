package com.wang.project.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.service.TestListGroupPageToRedis;
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
                    System.out.println(collect);
                    //获取商品编号，准备存入redis
                    List<String> goodsNos = collect.stream().map(Goods::getGoodsNo).collect(Collectors.toList());
                    //入redis中，规则：key：wangcheng_10001_(i+1), value：List<String> goodsNos， timeout：1小时（暂时不填）
                    String redisKey = "wangcheng_" + collect.get(0).getCategory() + "_" + (i + 1);
//                    redisTemplate.delete(redisKey);
                    Long result = redisTemplate.opsForList().rightPush(redisKey, goodsNos);
                    System.out.println(result);
                    List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
                    System.out.println(range);
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }
            System.out.println(map);
        }
    }

    @Override
    public void removeListRedis() {
        String redisKey = "wangcheng_dryCargo_1";
//        List<String> goodsNos = Arrays.asList("10002001", "10002002");
        /*
         * 这里注意，redisTemplate.opsForList().rightPush(redisKey, list);这里存到redis中的数据结构是
         * [[10002001,10002002]]，意思是redis里有一个List，本次存的list，是放在redis中的List中的，所以
         * 获取的时候redisTemplate.opsForList().range(redisKey, 0, 0);意思是获取redis中的List中的第
         * 0个元素，也就是上次存的list。
         */
        List<Object> range = redisTemplate.opsForList().range(redisKey, 0, 0);
        System.out.println(range);
        List<String> list = (List<String>) redisTemplate.opsForList().range(redisKey, 0, 0).get(0);
        System.out.println(list);
        Long remove = redisTemplate.opsForList().remove(redisKey, -1, range.get(0));
//        System.out.println(remove);
        List<Object> range2 = redisTemplate.opsForList().range(redisKey, 0, -1);
        System.out.println(range2);
    }

    private static List<Goods> getGoodsList(){
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
        goods6.setCategory(null);
        goods6.setGoodsNo("10003001");
        Goods goods7 = new Goods();
        goods7.setCategory("");
        goods7.setGoodsNo("10004001");
        return Arrays.asList(goods1, goods2, goods3, goods4, goods5, goods6, goods7);
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

}
