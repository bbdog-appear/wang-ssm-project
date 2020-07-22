package com.wang.project.demo.service.impl;

import com.wang.project.demo.biz.TestRedissonBiz;
import com.wang.project.demo.service.TestRedissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      1.测试redisson锁机制
 * </p>
 *
 * @author wangcheng
 * @version Id：TestRedissonServiceImpl.java Date：2020/7/22 14:32 Version：1.0
 */
@Service
public class TestRedissonServiceImpl implements TestRedissonService {

    @Autowired
    private TestRedissonBiz testRedissonBiz;

    @Override
    public void testRedissonWriteLock() throws InterruptedException {
        testRedissonBiz.testRedissonWriteLock();
    }

    @Override
    public void testRedissonReadLock() throws InterruptedException {
        testRedissonBiz.testRedissonReadLock();
    }


}
