package com.wang.project.demo.task;

import java.util.Date;
import java.util.TimerTask;

/**
 * User:wangcheng Date:2020/5/18 14:39 ProjectName:MyTask Version:1.0
 **/
public class MyTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("begin time:" + new Date());
    }
}
