package com.wang.project.demo.service.impl;

import com.wang.project.demo.biz.TestTheadPoolServiceBiz;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/13 16:54 ProjectName:TestThreadPoolServiceImpl Version:1.0
 **/
@Service
public class TestThreadPoolServiceImpl implements TestThreadPoolService {

    @Autowired
    private TestTheadPoolServiceBiz testTheadPoolServiceBiz;

    public void testTheadPool(List<WcProductEO> wcProductEOs) {
        wcProductEOs.forEach(wcProductEO -> {
            testTheadPoolServiceBiz.testTheadPool(wcProductEO);
        });
    }

    public void testSingleton3(){
        ThreadPoolExecutor theadPoolExecutor = testTheadPoolServiceBiz.getTheadPoolExecutor();
    }
    public void testSingleton4(){
        ThreadPoolExecutor theadPoolExecutor = testTheadPoolServiceBiz.getTheadPoolExecutor();
    }

    public void testSingleton(){
//        System.out.println("testSingleton = "+testTheadPoolServiceBiz.getTheadPoolExecutor());
    }

    public void testSingleton2(){
//        System.out.println("testSingleton2 = "+testTheadPoolServiceBiz.getTheadPoolExecutor());
    }
}
