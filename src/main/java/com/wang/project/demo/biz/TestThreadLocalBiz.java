package com.wang.project.demo.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        }
    };

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

        /* 使用ThreadLocal的原因：一定要使用全局变量，而又想让多个线程之间在访问共享变量的时候互不影响，那就使用ThreadLocal<T>。
        SimpleDateFormat作为全局变量，线程不安全。
           ThreadLocal的作用：可以让线程在操作共享变量时，复制该共享变量的一个副本(局部变量)到线程自己的栈空间，以后就操作这个副本空间来代替共享空间。
           ThreadLocal的工作原理：其中threadLocal new出来的是ThreadLocal的子类，子类重写了initialValue方法，在threadLocal初始化的时候，自动
        执行了initialValue方法，然后通过get()方法获取当前线程的SimpleDateFormat对象
           ps：上面的synchronized关键字可以不加，也可以实现线程安全*/
        SimpleDateFormat simpleDateFormat = threadLocal.get();
        String nowDate = simpleDateFormat.format(new Date());
        System.out.println(nowDate);
    }

}
