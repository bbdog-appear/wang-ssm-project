package com.wang.project.demo.service.impl;

import com.wang.project.demo.biz.handle.TestTryFinallyHandle;
import com.wang.project.demo.service.TestTryCatchFinallyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/20 19:45 ProjectName:TestTryCatchFinallyServiceImpl Version:1.0
 **/
@Service
public class TestTryCatchFinallyServiceImpl implements TestTryCatchFinallyService {
    @Autowired
    private TestTryFinallyHandle testTryFinallyHandle;

    @Override
    public void testTryCatchFinally() {
        System.out.println("try 外面=========");
        try {
            System.out.println("try 里面=========");
            int i = 10/0;

        }catch (Exception e){
            System.out.println("catch 里面=========");

        }finally {
            System.out.println("finally 里面=========");

        }
    }

    @Override
    public void testTryFinally() {
        try {
            testTryFinallyHandle.testTryFinally();
        }finally {
            System.out.println("finally块");
        }
    }


}
