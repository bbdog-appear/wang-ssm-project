package com.wang.project.demo.service;

public interface TestRedisLockService {

    /**
     * 测试redis分布式锁功能
     *
     * @param threadNum index
     * @return void
     **/
    void testRedisLock(int threadNum);
}
