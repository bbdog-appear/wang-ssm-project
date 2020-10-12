package com.wang.project.demo.service.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.wang.project.demo.biz.handle.QueryUserProductHandle;
import com.wang.project.demo.dao.TestDao;
import com.wang.project.demo.dao.WcProductDao;
import com.wang.project.demo.entity.User;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestGuavaThreadPoolService;
import com.wang.project.demo.util.GuavaThreadPool;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 *      测试guava线程池，guava线程池的作用，在云笔记中记录了，大概为如果是开启一个线程去查询，并
 *      有返回值，那么就相当于同步串行执行，但是如果开启多个线程去查询，那么多个查询之间是异步并行
 *      执行的，这就节省了效率了。
 *      其中guava线程池和普通线程池的区别，guava线程池可以将查询结果，通过回调函数的形式，将结果
 *      封装到new的VO中，并且多个ListenableFuture对象可以装进ListenableFuture里，进行批量的
 *      get操作。普通的线程池没有回调，并且是单个的future.get();暂时还未验证批量get和单个get的
 *      性能对比。
 * </p>
 *
 * @author wangcheng
 * @version Id：TestGuavaThreadPoolServiceImpl.java Date：2020/10/12 15:35 Version：1.0
 */
@Component
@Slf4j
public class TestGuavaThreadPoolServiceImpl implements TestGuavaThreadPoolService {

    @Autowired
    private WcProductDao wcProductDao;
    @Autowired
    private TestDao testDao;
    @Autowired
    private GuavaThreadPool guavaThreadPool;
    @Autowired
    private QueryUserProductHandle queryUserProductHandle;


    @Override
    public void testGuavaThreadPool() throws Exception{

        UserProductVO userProductVO = new UserProductVO();

        // 查询产品信息
        ListenableFuture<List<WcProductEO>> productListenableFuture = guavaThreadPool.submitAndCallback(() ->
                wcProductDao.selectAllWcProductEOs(), queryUserProductHandle.productInfoCallback(userProductVO));

        // 查询用户信息
        ListenableFuture<List<User>> userListenableFuture = guavaThreadPool.submitAndCallback(() ->
                testDao.selectAllUser(), queryUserProductHandle.userInfoCallback(userProductVO));

        ListenableFuture listListenableFuture = Futures.allAsList(productListenableFuture, userListenableFuture);

        listListenableFuture.get(2, TimeUnit.SECONDS);

        System.out.println("================guava线程池获取的结果为：" + userProductVO);

    }

}
