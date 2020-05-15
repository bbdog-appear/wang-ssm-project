package com.wang.project.demo.service;

import com.wang.project.demo.entity.WcProductEO;

import java.util.List;

public interface TestThreadPoolService {

    void testTheadPool(List<WcProductEO> wcProductEOs);

    public void testSingleton();

    public void testSingleton2();
}
