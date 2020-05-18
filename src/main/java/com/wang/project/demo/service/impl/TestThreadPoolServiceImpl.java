package com.wang.project.demo.service.impl;

import com.wang.project.demo.biz.TestTheadPoolServiceBiz;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/13 16:54 ProjectName:TestThreadPoolServiceImpl Version:1.0
 **/
@Service
@Slf4j
public class TestThreadPoolServiceImpl implements TestThreadPoolService {

    @Autowired
    private TestTheadPoolServiceBiz testTheadPoolServiceBiz;
    private int threadNum = 0;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void testTheadPool(List<WcProductEO> wcProductEOs) throws InterruptedException, BrokenBarrierException {
        //类似于线程的计数器，当计数器的数量变成0，那么就会唤醒主线程。
//        final CountDownLatch countDownLatch = new CountDownLatch(wcProductEOs.size());
        final CyclicBarrier barrier = new CyclicBarrier(wcProductEOs.size());
        wcProductEOs.forEach(wcProductEO -> {
            threadNum += 1;
            testTheadPoolServiceBiz.testTheadPool(wcProductEO, threadNum, barrier);
        });
        barrier.await();
        log.info("子线程都跑完了，主线程紧接着跑完了");
    }

//    public void testSingleton3(){
//        ThreadPoolExecutor theadPoolExecutor = testTheadPoolServiceBiz.getTheadPoolExecutor();
//    }
//    public void testSingleton4(){
//        ThreadPoolExecutor theadPoolExecutor = testTheadPoolServiceBiz.getTheadPoolExecutor();
//    }

    public void testSingleton(){
//        System.out.println("testSingleton = "+testTheadPoolServiceBiz.getTheadPoolExecutor());
    }

    public void testSingleton2(){
//        System.out.println("testSingleton2 = "+testTheadPoolServiceBiz.getTheadPoolExecutor());
    }
}
