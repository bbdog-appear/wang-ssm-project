package com.wang.project.demo.biz;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *      1.测试redisson锁机制
 * </p>
 *
 * @author wangcheng
 * @version Id：TestRedissonBiz.java Date：2020/7/22 14:36 Version：1.0
 */
@Component
public class TestRedissonBiz {

    @Autowired
    private RedissonClient redissonClient;

    private String rwLockKey = "redissonLock_wangcheng_10001";

    /**
     * 测试redisson锁机制(获取对象rwLockKey的写锁)
     */
    public void testRedissonWriteLock() throws InterruptedException {

        // 获取读写锁对象
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(rwLockKey);

        // 获取写锁对象
        RLock rLock = readWriteLock.writeLock();

        // 对于该key加写锁
        rLock.lock(10, TimeUnit.MINUTES);

        System.out.println("写锁业务代码执行开始");
        Thread.sleep(5000);
        System.out.println("写锁业务代码执行完成");

        // 对于该key释放写锁
//        rLock.unlock();
    }

    /**
     * 测试redisson锁机制(读锁)
     * 结论：读读不互斥，读写互斥，写读互斥，写写互斥
     */
    public void testRedissonReadLock() throws InterruptedException {

        // 获取读写锁对象
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(rwLockKey);

        // 获取读锁对象
        RLock rLock = readWriteLock.readLock();

        // 对于该key加读锁
        rLock.lock(10, TimeUnit.MINUTES);

        System.out.println("读锁业务代码执行开始");
        Thread.sleep(5000);
        System.out.println("读锁业务代码执行完成");

        // 对于该key释放读锁
//        rLock.unlock();
    }

}
