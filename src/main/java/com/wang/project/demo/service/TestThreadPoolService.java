package com.wang.project.demo.service;

import com.wang.project.demo.entity.WcProductEO;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public interface TestThreadPoolService {

    void testTheadPool(List<WcProductEO> wcProductEOs) throws InterruptedException, BrokenBarrierException;

    public void testSingleton();

    public void testSingleton2();

    void testExecutorService();

}
