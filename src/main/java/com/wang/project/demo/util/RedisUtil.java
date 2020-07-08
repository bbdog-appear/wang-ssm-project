package com.wang.project.demo.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * redis分布式锁 加锁
     *
     * @param lockKey 锁名
     * @param expirationTime 锁过期时间(毫秒)
     * @return boolean
     **/
    public boolean lock(String lockKey, long expirationTime){
        boolean success = false;
        //redisValue(锁过期时间+当前时间)
        long value = expirationTime + System.currentTimeMillis();
        //设置key和value。如果key不存在，则返回true，反之false。
        success = setNX(lockKey, value);
        if (!success) {
            //获取redis中第一次存的时间
            Long keyValue = getObjByKey(lockKey, Long.class);
            long oldValue = keyValue == null ? 0L : keyValue;
            //如果第一次存的时间小于当前时间，说明已经超过expirationTime时间了，锁已经过期了。
            if (oldValue < System.currentTimeMillis()) {
                //获取这个key原来的值，插入新的值。
                long resultValue = getSet(lockKey, value, Long.class);
                if (oldValue == resultValue) {
                    success = true;
                } else {
                    //已被其他进程捷足先登了
                    success = false;
                }
            } else {
                //锁还未失效
                success = false;
            }
        }
        return success;
    }

    /**
     * 根据key获得指定类型的value
     *
     * @param key key
     * @param clazz 类型的Class对象
     * @return T
     **/
    public <T> T getObjByKey(String key, Class<T> clazz){
        log.error("getObjByKey request：key={}，clazz={}", key, clazz);

        //根据key获取value
        String value = get(key);
        //将字符串转为指定类型的对象
        T result = JSONObject.parseObject(value, clazz);

        log.error("getObjByKey request：key={}，clazz={}", key, clazz);
        return result;
    }

    /**
     * redis的getSet操作(获取原来的值，插入现在的值)
     * todo 这里存值得时候序列化指定了是StringSerializer，所以存的是字符串
     *  取的时候，也要指定一下是字符串的反序列化。
     *
     * @param key key
     * @param object value
     * @param clazz 类型的Class对象
     * @return T
     **/
    public <T> T getSet(String key, Object object, Class<T> clazz){
        String value = JSONObject.toJSONString(object);
        String resultValue = redisTemplate.execute((RedisConnection redisConnection) -> {
            byte[] redisKey = redisTemplate.getStringSerializer().serialize(key);
            byte[] redisValue = redisTemplate.getStringSerializer().serialize(value);
            byte[] result = redisConnection.getSet(redisKey, redisValue);
            return redisTemplate.getStringSerializer().deserialize(result);
        });
        return JSONObject.parseObject(resultValue, clazz);
    }

    /**
     * redis管道批量插入list结构的数据
     *
     * @param key
     * @param valueList
     * @return java.util.List<java.lang.Object>
     **/
    public List<Object> rPushList(String key, List<String> valueList){
        List<Object> results = redisTemplate.executePipelined((RedisConnection redisConnection) -> {
            // 打开管道
            redisConnection.openPipeline();
            // 给本次管道内添加，要一次性执行的多条命令
            for (String value : valueList) {
                redisConnection.rPush(key.getBytes(), value.getBytes());
            }
            // 关闭管道 不需要close 否则拿不到返回值
            // connection.closePipeline();
            // 这里一定要返回null，最终pipeline的执行结果，才会返回给最外层
            return null;
        });
        return results;
    }

    /**
     * redis管道list结构批量pop出元素
     * todo 这里要注意，因为存的value序列化规则是StringSerializer
     *  所以取的时候，要指定一下redisTemplate.getStringSerializer()
     *
     * @param key key
     * @param categoryNum 次数
     * @return java.util.List<java.lang.Object>
     **/
    public List<String> rPopList(String key, int categoryNum){
        List<Object> objects = redisTemplate.executePipelined((RedisConnection redisConnection) -> {
            // 打开管道
            redisConnection.openPipeline();
            for (int i = 0; i < categoryNum; i++) {
                redisConnection.rPop(key.getBytes());
            }
            return null;
        }, redisTemplate.getStringSerializer());
        List<String> resultList = new ArrayList<>();
        objects.forEach(object -> resultList.add((String) object));
        return resultList;
    }

    /**
     * 获取有前缀的key的列表
     *
     * @param prefixKey key前缀，如：helloJava_*
     * @return java.util.Set<java.lang.String>
     **/
    public Set<String> getKeys(String prefixKey){
        return redisTemplate.keys(prefixKey);
    }

//    public static void main(String[] args) {
//        String redisKey = "wangcheng_category_188";
//        String substring = redisKey.substring(redisKey.lastIndexOf("_") + 1);
//        String substring1 = redisKey.substring(0, redisKey.lastIndexOf("_"));
//        System.out.println(substring);
//        System.out.println(substring1);
//    }

    public static void main(String[] args) {
        Set<String> keys = new TreeSet<>();
        keys.add("wang_fruit_1");
        keys.add("wang_fruit_3");
        keys.add("wang_fruit_2");
        keys.add("wang_fruit_5");
        keys.add("wang_fruit_4");
        System.out.println(keys);
        Set<String> collect = keys.stream().sorted().collect(Collectors.toSet());
        System.out.println(collect);
        List<String> list = keys.stream().collect(Collectors.toList());
        System.out.println(list);
        List<String> collect1 = list.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        System.out.println(collect1);
    }

}
