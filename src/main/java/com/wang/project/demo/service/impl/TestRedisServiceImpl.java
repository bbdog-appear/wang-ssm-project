package com.wang.project.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.service.TestRedisService;
import com.wang.project.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/6/19 15:33 ProjectName:TestRedisServiceImpl Version:1.0
 **/
@Service
public class TestRedisServiceImpl implements TestRedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void testRedis() {
        //定义一个list
        List<String> list = Arrays.asList("testList1", "testList2", "testList3");
        System.out.println("第1个"+list);

        //往redis中存入一个list
        redisTemplate.opsForList().leftPushAll("listKey2", list);
        redisTemplate.opsForList().leftPush("listKey3", list);

        //从redis中弹出这个key，redis中就没有这个key了。
        List<String> resultList2 = redisTemplate.opsForList().range("listKey2", 0, -1);
        List<String> resultList3 = (List<String>) redisTemplate.opsForList().leftPop("listKey3");
        System.out.println("第2个"+resultList2);
        System.out.println("第3个"+resultList3);
    }

    @Override
    public void testRedisTemplateExecutor() {
        String key = "listKey5";
        List<String> list = Arrays.asList("testList4", "testList5", "testList3");
        System.out.println("第1个"+list);
        boolean result = redisUtil.setNX(key, list);
        System.out.println("第3个"+result);
        String resultList4 = redisUtil.get(key);
        System.out.println("第4个"+resultList4);
        List<String> resultList5 = JSONObject.parseArray(resultList4, String.class);
        System.out.println("第5个"+resultList5);
    }

    @Override
    public void testRedisKeyExists() {
        String key = "listKey6";
        String resultList6 = (String) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            System.out.println(redisConnection.exists(redisKey));
            if (redisConnection.exists(redisKey)) {
                byte[] bytes = redisConnection.get(redisKey);
                System.out.println(bytes);
                return redisTemplate.getStringSerializer().deserialize(bytes);
            }
            return null;
        });
        System.out.println(resultList6);
    }

}
