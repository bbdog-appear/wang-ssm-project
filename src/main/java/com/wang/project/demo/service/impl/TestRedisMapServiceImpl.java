package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestRedisMapService;
import com.wang.project.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 测试redis map结构
 * User:wangcheng Date:2020/7/10 11:19 ProjectName:TestRedisMapServiceImpl Version:1.0
 **/
@Service
public class TestRedisMapServiceImpl implements TestRedisMapService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 说明：redis hash结构，相同的key，其中hashMap中的key相同的话，会覆盖上一次的值
     * 过期时间：expire方法，会覆盖上一次的过期时间。
     */
    @Override
    public void testAddRedisMap() {
        String redisKey1 = "dryCargo";
        Map<String, Object> redisValueMap1 = new HashMap<>();
        redisValueMap1.put("goodsTotalNum", 16);
        redisValueMap1.put("goodsSellNum", 3);
        String redisKey2 = "drinks";
        Map<String, Object> redisValueMap2 = new HashMap<>();
        redisValueMap2.put("goodsTotalNum", 15);
        redisValueMap2.put("goodsSellNum", 0);

        redisTemplate.opsForHash().putAll(redisKey1, redisValueMap1);
        redisTemplate.opsForHash().putAll(redisKey2, redisValueMap2);
        redisTemplate.expire(redisKey1, 30, TimeUnit.SECONDS);

//        redisTemplate.opsForHash().put(redisKey1, "goodsTotalNum", 20);
    }

    @Override
    public void testQueryRedisMap() {
        String redisKey1 = "dryCargo";
        String redisKey2 = "drinks";

//        redisTemplate.delete(redisKey1);

        Object goodsTotalNum1 = redisTemplate.opsForHash().get(redisKey1, "goodsTotalNum");
        Object goodsSellNum1 = redisTemplate.opsForHash().get(redisKey1, "goodsSellNum");
        Object goodsTotalNum2 = redisTemplate.opsForHash().get(redisKey2, "goodsTotalNum");
        Object goodsSellNum2 = redisTemplate.opsForHash().get(redisKey2, "goodsSellNum");
        System.out.println("dryCargo goodsTotalNum: " + goodsTotalNum1);
        System.out.println("dryCargo goodsSellNum: " + goodsSellNum1);
        System.out.println("drinks goodsTotalNum: " + goodsTotalNum2);
        System.out.println("drinks goodsSellNum: " + goodsSellNum2);

        Boolean aBoolean = redisTemplate.hasKey(redisKey1);
        Boolean aBoolean2 = redisTemplate.hasKey(redisKey2);
        System.out.println("dryCargo是否存在：" + aBoolean);
        System.out.println("drinks是否存在：" + aBoolean2);

        List<Object> list = new ArrayList<>();
        list.add("goodsTotalNum");
        list.add("goodsSellNum");
        List<Object> objects = redisTemplate.opsForHash().multiGet(redisKey1, list);
        System.out.println("redis hash multiGet：" + objects);

        Set<Object> keys = redisTemplate.opsForHash().keys(redisKey1);
        System.out.println("redis hash keys:" + keys);

    }

}
