package com.wang.project.demo.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p>
 *      测试threadLocal
 * </p>
 *
 * @author wangcheng
 * @version Id：TestThreadLocalBiz.java Date：2020/7/24 9:39 Version：1.0
 */
@Component
public class TestThreadLocalBiz {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 测试threadLocal
     */
    public void testThreadLocal() throws Exception {
        Future<String> submit = threadPoolTaskExecutor.submit(() -> {
            ThreadLocal<String> threadLocal = new ThreadLocal<>();
            threadLocal.set("线程" + Thread.currentThread().getName());
            return threadLocal.get();
        });
        String s = submit.get();
        System.out.println(s);
    }

}
