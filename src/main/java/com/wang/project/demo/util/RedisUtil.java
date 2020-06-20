package com.wang.project.demo.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @Description redis操作类
 * User:wangcheng Date:2020/6/20 17:34 ProjectName:RedisUtil Version:1.0
 **/
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redisTemplate的setNX操作
     *
     * @param key redisKey
     * @param object redisValue
     * @return boolean
     **/
    public boolean setNX(String key, Object object){
        log.info("setNX request：key={}，value={}", key, object);
        String value = JSONObject.toJSONString(object);
        boolean result = (boolean) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] redisValue = redisTemplate.getStringSerializer().serialize(value);
            return redisConnection.setNX(redisKey, redisValue);
        });
        log.info("setNX response：{}", result);
        return result;
    }

    /**
     * redisTemplate的get操作
     *
     * @param key redisKey
     * @return java.lang.String
     **/
    public String get(String key){
        log.info("get request：key={}", key);
        String result = (String) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] bytes = redisConnection.get(redisKey);
            return redisTemplate.getStringSerializer().deserialize(bytes);
        });
        log.info("get response：{}", result);
        return result;
    }

    /**
     * 判断redisKey是否存在，是true，否false
     *
     * @param key
     * @return boolean
     **/
    public boolean keyExist(String key){
        return redisTemplate.hasKey(key);
    }

}
