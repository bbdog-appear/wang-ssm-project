package com.wang.project.demo.test;

import com.wang.project.demo.vo.Goods;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/28 10:49 ProjectName:WorkTest Version:1.0
 **/
public class WorkTest {
    //如果用static修饰变量，当这个类初始化的时候，就会创建一个ThreadPoolExecutor对象，并且这个线程池对象是随着这个类的回收而被回收。
    private static ThreadPoolExecutor theadPoolExecutor = new ThreadPoolExecutor(
            10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
//        test1();
//        test2();
//        testCallable();
//        testOriginalThread();
        testGroupBy();
    }

    /**
     * @Author c_wangcheng-007
     * @Description 测试原始线程，原始线程执行完之后，就会自动销毁。随之虚拟机结束
     * 注意，传统的new Thread方式，只支持RunnerAble，没有返回值。用线程池的时候，可以用CallAble
     * @Date 0:05 2020/6/6/006
     * @Param []
     * @return void
     **/
    private static void testOriginalThread(){
        Thread thread = new Thread(() -> {
            System.out.println("线程内部执行");
            try {
                Thread.sleep(3000);
                System.out.println("线程内部执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            //阻塞，等到子线程执行完毕，再执行主线程。但是这样毫无意义，跟串行调用没区别。
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程开始执行");
//        testCallable();
        testHashSetOrTreeSet();
    }

    /**
     * 测试HashSet或TreeSet
     *
     * @param
     * @return void
     **/
    private static void testHashSetOrTreeSet(){
        Set<String> hashSet = new HashSet<>();
        hashSet.add("wangyuyan");
        hashSet.add("wangjingchu");
        hashSet.add("wangyuyan");
        hashSet.add("haoha");
        hashSet.add("84747");
        Iterator<String> iterator = hashSet.iterator();
//        System.out.println("=========="+iterator);
        while (iterator.hasNext()){
            String next = iterator.next();
            if ("wangjingchu".equals(next)) {
                iterator.remove();
            }
        }
        while (iterator.hasNext()){
            System.out.println("++++++++"+iterator.next());
        }
    }

    /**
     * 测试线程池返回值
     *
     * 《涉及到一个问题，在main方法中开线程的话，因为线程池中核心线程数，
     * 我设置的10，所以第一次创建一个线程，就一直存活在线程池里，
     * 所以jvm判断还有线程存活，就不会退出。如果用Junit测试的话，虚拟机就会退出。》
     *
     * 记住，在main方法中开线程，虚拟机肯定等到最后一个线程中的任务跑完，再结束的。
     * 但是如果线程还存在的话，那么虚拟机就不会结束。
     *
     * @param
     * @return void
     **/
    private static void testCallable() {
        //将一个任务丢进线程池中执行
        Future<String> submit = theadPoolExecutor.submit(() -> {
            System.out.println("子线程执行");
            Thread.sleep(3000);
            System.out.println("子线程执行结束");
            return "nihao";
        });
        try {
            System.out.println("主线程执行");
            //获取线程的返回值，另外这个子线程没有执行完，这里就会一直阻断。
            String s = submit.get();
            System.out.println(s);

            //在main方法中，需要手动的关闭线程池，否则虚拟机一直不会关闭。
            System.out.println(theadPoolExecutor.isShutdown());
            theadPoolExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (i == null) {
            if("heihei".equals(i)){
                System.out.println("第一个else if");
            }
        } else if ("nihao1".equals(i)) {
            System.out.println("第二个else if");
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
    /**
     * 测试jdk8中list的分组
     *
     * @param
     * @return void
     **/
    public static void testGroupBy(){
        List<Goods> goodsList = getGoodsList();
        //根据商品分类进行分组(过滤掉以null为key的组)
        Map<String, List<Goods>> collect = goodsList.stream().
                filter(goods -> !StringUtils.isEmpty(goods.getCategory())).
                collect(Collectors.groupingBy(Goods::getCategory));
        System.out.println(collect);
    }

    private static List<Goods> getGoodsList(){
        Goods goods1 = new Goods();
        goods1.setCategory("drinks");
        goods1.setGoodsNo("10001001");
        Goods goods2 = new Goods();
        goods2.setCategory("drinks");
        goods2.setGoodsNo("10001002");
        Goods goods3 = new Goods();
        goods3.setCategory("dryCargo");
        goods3.setGoodsNo("10002001");
        Goods goods4 = new Goods();
        goods4.setCategory("dryCargo");
        goods4.setGoodsNo("10002002");
        Goods goods5 = new Goods();
        goods5.setCategory("dryCargo");
        goods5.setGoodsNo("10002003");
        Goods goods6 = new Goods();
        goods6.setCategory(null);
        goods6.setGoodsNo("10003001");
        Goods goods7 = new Goods();
        goods7.setCategory("");
        goods7.setGoodsNo("10004001");
        return Arrays.asList(goods1, goods2, goods3, goods4, goods5, goods6, goods7);
    }
}
