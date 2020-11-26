package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestLockMechanismService;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 测试锁功能
 * <p>
 *      1、这里，是按照ReentrantLock对象为维度的，如果每个线程进来，都是同一个ReentrantLock对象，
 *      那么这些线程就是共享同一个reentrantLock对象，这样，就能实现多个线程中，一次只能有一个线程拿到
 *      锁，并且只能由这个线程解锁。
 *      如果每次都是不同的reentrantLock对象，那么就是一个线程对应一个reentrantLock对象，那么每个线程
 *      就各跑各的，锁功能就失效了。
 * <p>
 * User:wangcheng Date:2020/5/26 18:43 ProjectName:TestLockMechanismImpl Version:1.0
 **/
@Service
public class TestLockMechanismServiceImpl implements TestLockMechanismService {
    private ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void testLockMechanism(long threadId) {
//        ReentrantLock reentrantLock = new ReentrantLock();
        System.out.println("线程" + threadId + "开始执行，锁对象为：" + reentrantLock);
        reentrantLock.lock();
        System.out.println("线程" + threadId + "拿到锁，锁对象为：" + reentrantLock);
        try {
            synchronized (this){
                System.out.println("锁这个对象");
            }
            System.out.println("线程" + threadId + "开始休眠");
            testReentrantLock(threadId);
            Thread.sleep(10000);
            System.out.println("线程" + threadId + "结束休眠");
        } catch (Exception e) {
            System.out.println("线程" + threadId + "捕捉到异常");
            e.printStackTrace();
        }
        System.out.println("线程" + threadId + "结束执行，锁对象为：" + reentrantLock);
        reentrantLock.unlock();
        System.out.println("线程" + threadId + "解锁，锁对象为：" + reentrantLock);
    }

    private void testReentrantLock(long threadId){
        System.out.println("线程" + threadId + "开始进入可重入方法");
        reentrantLock.lock();
        System.out.println("线程" + threadId + "开始进入可重入方法，并再次拿到锁");
        System.out.println("线程" + threadId + "开始进入可重入方法，并开始解锁");
        reentrantLock.unlock();
        System.out.println("线程" + threadId + "开始进入可重入方法，并结束解锁");
    }
}
