package com.wang.project.demo.biz.handle;

import org.springframework.stereotype.Component;

/**
 * User:wangcheng Date:2020/7/14 11:28 ProjectName:TestTryFinallyHandle Version:1.0
 **/
@Component
public class TestTryFinallyHandle {

    public String testTryFinally(){
        try {
            int i = 1/0;
        }catch (Exception e){
            throw e;
        }
        return "你好";
    }
}
