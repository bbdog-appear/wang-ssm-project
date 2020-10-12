package com.wang.project.demo.util;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * <p>
 *      GuavaThreadPool工具类
 * </p>
 *
 * @author wangcheng
 * @version Id：GuavaThreadPool.java Date：2020/10/12 16:19 Version：1.0
 */
@Component
@Slf4j
public class GuavaThreadPool {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * guava线程池执行任务，并且将返回的future通过回调函数，将结果放入VO中
     *
     * @param callable 需执行的任务
     * @param futureCallback 回调函数
     * @param <V> 泛型
     * @return ListenableFuture对象
     */
    public <V> ListenableFuture<V> submitAndCallback(Callable<V> callable, FutureCallback<V> futureCallback){
        ListenableFuture<V> listenableFuture =
                MoreExecutors.listeningDecorator(taskExecutor.getThreadPoolExecutor()).submit(callable);
        if (futureCallback != null) {
            Futures.addCallback(listenableFuture, futureCallback);
        }
        return listenableFuture;
    }

}
