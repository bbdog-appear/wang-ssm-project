package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestLockMechanismService;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/26 18:43 ProjectName:TestLockMechanismImpl Version:1.0
 **/
@Service
public class TestLockMechanismServiceImpl implements TestLockMechanismService {
    private ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void testLockMechanism(long threadId) {
        System.out.println("线程" + threadId + "开始执行，锁对象为：" + reentrantLock);
        reentrantLock.lock();
        System.out.println("线程" + threadId + "拿到锁，锁对象为：" + reentrantLock);
        try {
            System.out.println("线程" + threadId + "开始休眠");
            testReentrantLock(threadId);
            Thread.sleep(30000);
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
