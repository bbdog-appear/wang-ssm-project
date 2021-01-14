package com.wang.project.demo.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

/**
 * User:wangcheng Date:2020/4/24 14:59 ProjectName:Simple Version:1.0
 **/
//@Scope(value = "prototype")
//@Getter
//@Setter
//@ToString
public class Simple implements SimpleInterface{
    private static String simpleName;
    private Object object;
//    private static SimpleInner simpleInner = new SimpleInner();
    private static SimpleInner simpleInner;

    public void method1(){
        object = new Object();
        object = null;
    }
    public void method2(){
        simpleInner = new SimpleInner();
        System.out.println(simpleInner);
    }

    static {
        String s = UUID.randomUUID().toString();
        System.out.println("静态代码块：" + s);
    }

    public void initSimple(){
        simpleInner = new SimpleInner();
    }

    public SimpleInner getSimpleInner() {
        return simpleInner;
    }

    public void setSimpleInner(SimpleInner simpleInner) {
        Simple.simpleInner = simpleInner;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
