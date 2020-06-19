package com.wang.project.demo.test;

import com.wang.project.demo.task.MyTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/4/22 15:55 ProjectName:TestBean Version:1.0
 **/
public class TestBean {
    static Simple simple = new Simple();
    static Logger logger = LoggerFactory.getLogger(TestBean.class);

    public static void main(String[] args) {
//        test2();
        System.out.println("now："+new Date());
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,10);
        Date date=calendar.getTime();
        MyTask task = new MyTask();
        Timer timer = new Timer();
        //后一个参数是任务的执行时间,timer
        timer.schedule(task,date);
        System.out.println("主线程执行完");
    }

    public static void test2(){
        String str = logger.getName();
        System.out.println(str);//com.wang.project.demo.test.TestBean
        int i = str.lastIndexOf(46);
        System.out.println(i);//26
    }

    public static void test(){
//        String a = "你好……&*";
//        System.out.println(a.length());
//        simple.method1();
//        System.out.println(Simple.simpleInner);
//        simple.method2();
//        System.out.println(Simple.simpleInner);
//        List<Simple> list = new LinkedList<Simple>();
//        for (int i = 0; i < 10; i++){
//            Simple simple = new Simple();
//            list.add(simple);
//        }
//        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

    }

    public static void testBean(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/application.xml");
        PropertyPlaceholderConfigurer propertyConfigurer = (PropertyPlaceholderConfigurer) context.getBean("propertyConfigurer");
        PropertyPlaceholderConfigurer propertyConfigurer2 = (PropertyPlaceholderConfigurer) context.getBean("propertyConfigurer");
        System.out.println(propertyConfigurer);
        System.out.println(propertyConfigurer2);
    }
}
