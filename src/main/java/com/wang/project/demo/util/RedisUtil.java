package com.wang.project.demo.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description redis操作类
 * User:wangcheng Date:2020/6/20 17:34 ProjectName:RedisUtil Version:1.0
 **/
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redisTemplate的setNX操作
     * (如果是相同的key，则不进行操作，返回false)
     *
     * @param key redisKey
     * @param object redisValue
     * @return boolean
     **/
    public boolean setNX(String key, Object object){
        log.error("setNX request：key={}，value={}", key, object);
        String value = JSONObject.toJSONString(object);
        boolean result = (boolean) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] redisValue = redisTemplate.getStringSerializer().serialize(value);
            return redisConnection.setNX(redisKey, redisValue);
        });
        log.error("setNX response：{}", result);
        return result;
    }

    /**
     * redisTemplate的get操作
     *
     * @param key redisKey
     * @return java.lang.String
     **/
    public String get(String key){
        log.error("get request：key={}", key);
        String result = (String) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] bytes = redisConnection.get(redisKey);
            return redisTemplate.getStringSerializer().deserialize(bytes);
        });
        log.error("get response：{}", result);
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

    /**
     * redis插入，并设置key的存活时间，单位为秒
     * (如果是相同的key，则覆盖内容，返回true)
     *
     * @param key key
     * @param object value
     * @param timeOut key存活时间(秒)
     * @return boolean
     **/
    public boolean set(String key, Object object, long timeOut){
        log.error("setNX request：key={}，value={}", key, object);
        String value = JSONObject.toJSONString(object);
        boolean result = (boolean) redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] redisValue = redisTemplate.getStringSerializer().serialize(value);
            redisConnection.set(redisKey, redisValue);
            if (timeOut > 0) {
                redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
            }
            return true;
        });
        log.error("setNX response：{}", result);
        return result;
    }

}
