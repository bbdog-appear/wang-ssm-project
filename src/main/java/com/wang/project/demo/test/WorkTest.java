package com.wang.project.demo.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
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
//        test4();
//        testHashMapSourceCode();
        testConcurrentHashMapSourceCode();
    }

    /**
     * 测试ConcurrentHashMap源码：
     * 其中线程安全的实现为，(f = tabAt(tab, i = (n - 1) & hash)) == null，计算出数组下标的位置
     * 后，判断这个桶里元素是不是为null，这里是用cas原子操作，保证线程安全，即(判断这个位置为空，再放入元素)
     * 做成了原子操作，代码为：U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);用的是
     * sun.misc.Unsafe类下的native方法，比较初始值为null，然后当前值也为null，代表相同，则放入元素，再返回true，
     * break跳出循环，第一个线程结束。第二个线程比较初始值为null，但是当前的值如果不为空，则不放入元素，返回false，
     * 这时通过for (Node<K,V>[] tab = table;;)进入下一次循环，也就是自旋的过程，
     * 由于数组节点用volatile修饰：volatile Node<K,V>[] table;所以其实第一个线程改完之后，第二个线程就立马能读取
     * 最新值，即读取主存中是有元素的并赋给初始值，那么初始值不为空，然后当前值也不为空，比价相同了，则放入元素，并返回true。
     *
     * 其中是先用这个方法：(Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);查询是否为null
     */
    private static void testConcurrentHashMapSourceCode(){
        Map<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 18; i++) {
            String key = "goods"+i;
            String value = "777777"+i;
            concurrentHashMap.put(key, value);
        }
        String key = "goods2";
        String value = "这是good2的覆盖值";
        concurrentHashMap.put(key, value);
        System.out.println(concurrentHashMap);
    }

    /**
     * 测试hashMap源码：
     * 1.当节点为null时，会初始化一个数组节点 Node<K,V>[] table，大小为16，其中阈值为12=16*0.75
     * 2.当第一组值存入时，先对key取一个hash值(经过运算更加散列)，然后再用(16-1) & hash值获取数组下标位置，如9，
     * 如果这个位置为null，则new一个Node节点{hash,key,value,nextNode}，给table[9]=Node的位置装入这个新节点。size+1
     * 3.当第二组值存入时，同样的操作...，如果这时经过hash算法计算出数组下标位置也等于9。接着
     * 第一种情况：判断9的位置key的hash值等于现在这个hash值且(两个key的地址相同或值相同)，则把原来的value值覆盖为新的value值
     * 第二种情况：key不相同，只是hash算法计算出的结果一致。那么for循环，找原来的节点Node1的next是否为空，如果为空，则new一个
     * 新的Node2节点，将Node1.next=Node2;如果不为空，就找Node1.next的next节点是否为空。这样就在9的位置组成了一个链表。
     * 4.当这个链表的长度大于等于8，就转为红黑树。
     * 5.然后如果数组节点 Node<K,V>[] table里的 元素个数 超过阈值12的话，就会扩容。
     * 6.扩容是初始化一个新的数组节点 Node<K,V>[] table，大小翻倍变成32，接着再put值。
     */
    private static void testHashMapSourceCode(){
        Map<String, Object> hashMap = new HashMap<>();
        for (int i = 0; i < 17; i++) {
            String key = "goods"+i;
            String value = "13567"+i;
            hashMap.put(key, value);
        }
        String key = "goods2";
        String value = "这是good2的覆盖值";
        hashMap.put(key, value);
        System.out.println(hashMap);
    }

    private static void test4(){
        Long arr = null;
        if (arr != -1 && arr <= 0) {
            System.out.println("arr不行");
        }
        System.out.println("结束");
        Map map = new HashMap();
        map.put("nihao", "nihao");
    }

    /**
     * 1、测试和SimpleDateFormat一起用的Locale.getDefault();可以获取该虚拟机的运行语言环境
     * 2、测试jdk8中的日期和时间
     */
    private static void test3(){
        Locale aDefault = Locale.getDefault();
        System.out.println(aDefault);
        String country = aDefault.getCountry();
        System.out.println(country);
        System.out.println();
        System.out.println();

        // jdk1.8获取日期和时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();
        System.out.println(localDate);
        System.out.println(localTime);
        System.out.println();

        LocalDate localDate1 = LocalDate.now();
        System.out.println(localDate1);
        System.out.println();

        LocalTime localTime1 = LocalTime.now();
        System.out.println(localTime1);
        int hour = localTime1.getHour();
        int minute = localTime1.getMinute();
        int second = localTime1.getSecond();
        System.out.println(hour);
        System.out.println(minute);
        System.out.println(second);
        System.out.println();

        LocalDateTime localDateTime1 = LocalDateTime.of(2020, 7, 28, 7, 31, 10);
        System.out.println(localDateTime1);
        LocalDate localDate2 = localDateTime1.toLocalDate();
        LocalTime localTime2 = localDateTime1.toLocalTime();
        System.out.println(localDate2);
        System.out.println(localTime2);
        System.out.println();

        int year = localDateTime1.getYear();
        int month = localDateTime1.getMonth().getValue();
        int dayOfMonth = localDateTime1.getDayOfMonth();

    }


    /**
     * 测试原始线程，原始线程执行完之后，就会自动销毁。随之虚拟机结束
     * 注意，传统的new Thread方式，只支持RunnerAble，没有返回值。用线程池的时候，可以用CallAble
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
