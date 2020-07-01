package com.wang.project.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.entity.User;
import com.wang.project.demo.service.TestRedisService;
import com.wang.project.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis操作
 * User:wangcheng Date:2020/6/19 15:33 ProjectName:TestRedisServiceImpl Version:1.0
 **/
@Service
public class TestRedisServiceImpl implements TestRedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void testRedis() {
        //定义一个list
        List<String> list = Arrays.asList("testList1", "testList2", "testList3");
        System.out.println("第1个"+list);

        //往redis中存入一个list
        //解析：相当于redis的List中存了一个list对象
        redisTemplate.opsForList().leftPushAll("listKey2", list);
        //解析：相当于redis的List中存了一个list对象，两个字符串
        redisTemplate.opsForList().leftPush("listKey3", list);
        redisTemplate.opsForList().leftPush("listKey3", "zhuijia1");
        redisTemplate.opsForList().leftPush("listKey3", "zhuijia2");

        //从redis中弹出这个key，redis中就没有这个key了。
//        List<String> resultList3 = (List<String>) redisTemplate.opsForList().leftPop("listKey3");
        List<Object> resultList2 = redisTemplate.opsForList().range("listKey2", 0, -1);
        List<Object> resultList3 = redisTemplate.opsForList().range("listKey3", 0, -1);
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

    @Override
    public void testRedisSet() {
        String key = "listKey7";
        List<String> list = Arrays.asList("testKey77", "testKey888");
        long timeOut = 60*10;
        boolean result = redisUtil.set(key, list, timeOut);
        System.out.println(result);
    }

    @Override
    public void testRedisSetListObject() {
        String key = "listKey8";
        List<User> userList = getUserList();
        long timeOut = 60*10;
        boolean result = redisUtil.set(key, userList, timeOut);
        System.out.println(result);
        String resultList = redisUtil.get(key);
        List<User> users = JSONObject.parseArray(resultList, User.class);
        System.out.println(users);
    }

    private List<User> getUserList(){
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setCode("wangcheng");
        user.setName("王成");
        user.setInsertTime(new Date());
        User user2 = new User();
        user2.setCode("wangcheng2");
        user2.setName("王成2");
        user2.setInsertTime(new Date());
        users.add(user);
        users.add(user2);
        return users;
    }

    @Override
    public void testRedisHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "10001");
        map.put("count", 20);
        System.out.println(map);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll("store1", map);
        redisTemplate.expire("store1", 10, TimeUnit.SECONDS);

        Object o = redisTemplate.opsForHash().get("store1", "count");
        System.out.println(o);
    }


}
