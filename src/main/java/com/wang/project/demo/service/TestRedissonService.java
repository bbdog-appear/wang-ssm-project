package com.wang.project.demo.service;

public interface TestRedissonService {

    /**
     * 测试redisson锁机制(写锁)
     */
    void testRedissonWriteLock(int num) throws InterruptedException;

    /**
     * 测试redisson锁机制(读锁)
     */
    void testRedissonReadLock() throws InterruptedException;

}
