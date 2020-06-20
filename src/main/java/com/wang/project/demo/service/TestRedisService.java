package com.wang.project.demo.service;

public interface TestRedisService {

    /**
     * redisTemplate.opsForList()操作list
     *
     * @param
     * @return void
     **/
    void testRedis();

    /**
     * 测试redisTemplate中的setNX，其中如果这个key存在，那么就不进行操作了
     *
     * @param
     * @return void
     **/
    void testRedisTemplateExecutor();

    /**
     * 测试redis key是否存在，如果不存在，获取的话，会不会报错
     *
     * @param
     * @return void
     **/
    void testRedisKeyExists();
}
