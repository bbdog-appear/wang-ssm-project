package com.wang.project.demo.service.impl;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.wang.project.demo.biz.handle.QueryUserProductHandle;
import com.wang.project.demo.dao.mapper.shardingjdbc.WcProductMapper;
import com.wang.project.demo.dao.mapper.shardingjdbc.WcUserMapper;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.TestGuavaThreadPoolService;
import com.wang.project.demo.util.GuavaThreadPool;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    private WcProductMapper wcProductMapper;
    @Autowired
    private WcUserMapper wcUserMapper;
    @Autowired
    private GuavaThreadPool guavaThreadPool;
    @Autowired
    private QueryUserProductHandle queryUserProductHandle;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    /**
     * 结论：guava线程池和普通线程池的区别：guava线程池多个回调函数，另外对于性能来说，两者执行时间差不多，
     * guava线程池将多个ListenableFuture放入list中，批量get，普通线程池一个一个get，两者的时间差不多。
     * 但是需要注意的是，普通线程池，必须在所有线程异步执行代码的最后，一个个的get，否则就是串行执行了。
     *
     * @throws Exception 异常
     */
    @Override
    public void testGuavaThreadPool() throws Exception{

        UserProductVO userProductVO = new UserProductVO();

//        // 查询产品信息
//        ListenableFuture<List<WcProductEO>> productListenableFuture = guavaThreadPool.submitAndCallback(() ->
//                wcProductDao.selectAllWcProductEOs(), queryUserProductHandle.productInfoCallback(userProductVO));
//        // 查询用户信息
//        ListenableFuture<List<User>> userListenableFuture = guavaThreadPool.submitAndCallback(() ->
//                testDao.selectAllUser(), queryUserProductHandle.userInfoCallback(userProductVO));

        // guava线程池执行开始时间
        long guavaStartMillis = System.currentTimeMillis();

        // 查询产品信息
        ListenableFuture<List<WcProductEO>> productListenableFuture = guavaThreadPool.submitAndCallback(() -> {
            Thread.sleep(2000);
            return wcProductMapper.selectByShardDate("20200518");
        }, queryUserProductHandle.productInfoCallback(userProductVO));
        // 查询用户信息
        ListenableFuture<List<WcUserEO>> userListenableFuture = guavaThreadPool.submitAndCallback(() -> {
            Thread.sleep(3000);
            return wcUserMapper.selectByContractNo("1234567880");
        }, queryUserProductHandle.userInfoCallback(userProductVO));
        ListenableFuture listListenableFuture = Futures.allAsList(productListenableFuture, userListenableFuture);

        listListenableFuture.get(5, TimeUnit.SECONDS);
        System.out.println("================guava线程池获取的结果为：" + userProductVO);
        // guava线程池执行结束时间
        long guavaEndMillis = System.currentTimeMillis();
        // 时间差
        long guavaResultMillis = guavaEndMillis - guavaStartMillis;
        System.out.println("guava线程池执行完，耗时：" + guavaResultMillis + "毫秒");

        // 普通线程池执行
        ordinaryThreadPool();
    }

    /**
     * 普通线程池执行所需时间
     *
     * @throws Exception 异常
     */
    private void ordinaryThreadPool() throws Exception {
        // 普通线程池执行开始时间
        long ordinaryStartMillis = System.currentTimeMillis();

        // 普通线程池查询产品信息
        Future<List<WcProductEO>> submit1 = taskExecutor.submit(() -> {
            Thread.sleep(2000);
            return wcProductMapper.selectByShardDate("20200518");
        });
        // 普通线程池查询用户信息
        Future<List<WcUserEO>> submit2 = taskExecutor.submit(() -> {
            Thread.sleep(3000);
            return wcUserMapper.selectByContractNo("1234567880");
        });
        WcProductEO wcProductEO = submit1.get().get(0);
        WcUserEO user = submit2.get().get(0);
        System.out.println("================普通线程池获取的结果为：" + wcProductEO + user);

        // 普通线程池执行结束时间
        long ordinaryEndMillis = System.currentTimeMillis();
        // 时间差
        long ordinaryResultMillis = ordinaryEndMillis - ordinaryStartMillis;
        System.out.println("普通线程池执行完，耗时：" + ordinaryResultMillis + "毫秒");
    }

}
