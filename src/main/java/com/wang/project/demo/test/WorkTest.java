package com.wang.project.demo.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/28 10:49 ProjectName:WorkTest Version:1.0
 **/
public class WorkTest {

    /**
     * 测试jdk8新特性中list的操作
     *
     * @param args
     * @return void
     **/
    public static void main(String[] args) {
//        test1();
        test2();
    }

    /**
     * 测试if 和 if else 是否只走其中一个。答：是的
     *
     * @param
     * @return void
     **/
    public static void test2(){
        String i = "nihao";
        if("haha".equals(i)){
            System.out.println("第一个if");
        } else if (i != null) {
            if("heihei".equals(i)){
                System.out.println("第一个else if");
            }
        } else if ("nihao".equals(i)) {
            System.out.println("第二个else if");
        } else {
            System.out.println("最后一个else");
        }
    }

    /**
     * 测试jdk8新特性中list的操作
     *
     * @param
     * @return void
     **/
    public static void test1(){
        //快速创建list
        List<String> list1 = Arrays.asList("10001", "10002", "10005", "10003");
        List<String> list2 = Arrays.asList("10001", "10002", "10005");
        List<String> collect1 = list1.stream().filter(l1 -> !list2.contains(l1)).collect(Collectors.toList());
        List<String> collect2 = list2.stream().filter(l2 -> !list1.contains(l2)).collect(Collectors.toList());
        collect1.forEach(c1 -> System.out.println(c1));
        collect2.forEach(c2 -> System.out.println(c2));
    }
}
