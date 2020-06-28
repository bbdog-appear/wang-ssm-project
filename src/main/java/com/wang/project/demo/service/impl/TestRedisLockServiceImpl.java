package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestRedisLockService;
import com.wang.project.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description redis分布式锁测试类
 * User:wangcheng Date:2020/6/28 18:03 ProjectName:TestRedisLockServiceImpl Version:1.0
 **/
@Slf4j
@Service
public class TestRedisLockServiceImpl implements TestRedisLockService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void testRedisLock(int threadNum) {
        long id = Thread.currentThread().getId();
        String name = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();
        String lockKey = "testLockKey";
        long expirationTime = 15 * 1000;
        boolean resultLock = redisUtil.lock(lockKey, expirationTime);
        if (resultLock) {
            log.error("++++++++++++++++++++第【" + threadNum + "】个线程：线程号【"
                    + id + "】，线程名【" + name + "】拿到锁");
        } else {
            log.error("--------------------第【" + threadNum + "】个线程：线程号【"
                    + id + "】，线程名【" + name + "】没拿到锁");
        }
    }

}
