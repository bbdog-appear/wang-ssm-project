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
    @Override
    public void testLockMechanism() {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        try {
            System.out.println("线程");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
