package com.wang.project.demo.biz;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.wang.project.demo.commons.CostomException;
import com.wang.project.demo.dao.WcProductDao;
import com.wang.project.demo.entity.WcProductEO;
import javafx.concurrent.Worker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/13 17:30 ProjectName:TestTheadPoolServiceBiz Version:1.0
 **/
@Component
@Slf4j
//@Getter
//@Setter
public class TestTheadPoolServiceBiz {

    //如果用static修饰变量，当这个类初始化的时候，就会创建一个ThreadPoolExecutor对象，并且这个线程池对象是随着这个类的回收而被回收。
    private ThreadPoolExecutor theadPoolExecutor = new ThreadPoolExecutor(
            10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.AbortPolicy());

//    private static ThreadPoolExecutor theadPoolExecutor;

    @Autowired
    private WcProductDao wcProductDao;

    /**
     * @author wangcheng
     * @Description 多线程保存数据库操作
     * @Date 15:08 2020/5/14
     * @Param []
     * @return void
     **/
    public void testTheadPool(WcProductEO wcProductEO, int threadNum, CyclicBarrier barrier) {
        theadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long id = Thread.currentThread().getId();
                String name = Thread.currentThread().getName();
                Long startTime = System.currentTimeMillis();
                log.info("线程：线程号\"" + id + "\"，线程名\"" + name + "\"开始跑，开始的毫秒数：" + startTime + "。这是第" + threadNum +"个线程");
                wcProductDao.insertWcProductEO(wcProductEO);
                log.info("第" + threadNum +"个线程执行结束，耗时：" + String.valueOf(System.currentTimeMillis() - startTime) + "毫秒");
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @author wangcheng
     * @Description 初始化线程池
     * @Date 18:41 2020/5/15
     * @Param []
     * @return void
     **/
//    public void initThreadPoolExecutor(){
//        theadPoolExecutor = new ThreadPoolExecutor(
//            10, 20, 30, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.AbortPolicy());
//    }

//    public ThreadPoolExecutor getTheadPoolExecutor() {
//        return theadPoolExecutor;
//    }
//
//    public void setTheadPoolExecutor(ThreadPoolExecutor theadPoolExecutor) {
//        TestTheadPoolServiceBiz.theadPoolExecutor = theadPoolExecutor;
//    }
}
