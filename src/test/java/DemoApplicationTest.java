import com.wang.project.demo.config.CreateBeanConfig;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.*;
import com.wang.project.demo.test.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试主类
 * User:wangcheng Date:2020/4/26 13:36 ProjectName:DemoApplicationTest Version:1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-test.xml"})
@Slf4j
public class DemoApplicationTest {

    //线程池
    private ThreadPoolExecutor theadPoolExecutor = new ThreadPoolExecutor(
            10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private TestService testService;
    @Autowired
    private WcProductService wcProductService;
    @Autowired
    private SqlSessionFactoryBean sqlSessionFactory;
    @Autowired
    private Simple simple;
    @Autowired
    private SimpleInterface simpleInterface;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TestThreadPoolService testThreadPoolService;
    @Autowired
    private TestLambdaService testLambdaService;
    @Autowired
    private TestTryCatchFinallyService testTryCatchFinallyService;
    @Autowired
    private TestLockMechanismService testLockMechanismService;
    @Autowired
    private TestRedisService testRedisService;
    @Autowired
    private TestRedisLockService testRedisLockService;
    @Autowired
    private TestListGroupPageToRedis testListGroupPageToRedis;

    /**
     * 删除redis中list数据类型中的key或value
     *
     **/
    @Test
    public void removeListRedis() {
//        testListGroupPageToRedis.removeListRedis();
//        testListGroupPageToRedis.popRedisListElements();
        Object obj = 10;
        Long objLong = Long.valueOf(String.valueOf(obj));
        System.out.println(objLong);
    }

    /**
     * 测试list集合根据某个字段分组，并分页插入redis中(jdk8中list分组)
     *
     **/
    @Test
    public void testListGroupPageToRedis() {
        testListGroupPageToRedis.testListGroupPageToRedis();
    }

    /**
     * 测试redis分布式锁
     * 猜测一下：第1个线程拿到锁，然后第5个线程拿到锁，第9个线程拿到锁，其他拿不到锁。 猜错了！
     * 答案：因为虽然睡眠5秒，但是执行代码还要时间，所以每次循环大于5秒，经过3次循环，第4次已经超过15秒了。
     *      所以第一次存的毫秒数 < 当前时间。所以第1、4、7、10个线程拿到锁，其余没拿到锁。
     *
     **/
    @Test
    public void testRedisLock() throws Exception{
        for (int i = 0; i < 10; i++) {
            int threadNum = i;
            theadPoolExecutor.execute(() ->
                    testRedisLockService.testRedisLock(threadNum + 1));
            Thread.sleep(5000);
        }
    }

    /**
     * 测试redis相关操作
     *
     **/
    @Test
    public void testRedis(){
        testRedisService.testRedis();
//        testRedisService.testRedisTemplateExecutor();
//        testRedisService.testRedisKeyExists();
//        testRedisService.testRedisSet();
//        testRedisService.testRedisSetListObject();
//        testRedisService.testRedisHash();
    }


    /**
     * 测试相同属性的两个类的类型转换
     *
     **/
    @Test
    public void testTypeConversion(){
//        AddUserDefine addUserDefine = new AddUserDefine();
//        ModifyUserDefine modifyUserDefine = new ModifyUserDefine();
//        BeanUtils.copyProperties(addUserDefine, modifyUserDefine);
        AddUserDefine addUserDefine = new AddUserDefine();
        List<ModifyUserDefine> list = new ArrayList<>();
        addUserDefine.setList(list);
        if(addUserDefine.getList() != null){
            System.out.println(addUserDefine.getList());
        }
    }

    /**
     * 模拟10个并发情况下，测试锁机制是否生效
     *
     * 注意：不是main方法测试的话，那么主线程跑完之后，虚拟机就停止了。那么子线程自然不会得到执行。
     * 但是在实际开发中，虽然主线程跑完了，但是只要这个进程存在，虚拟机会一直存在的，所以子线程也会
     * 得到执行，就跟WorkTest中的main方法测试的一种情况差不多。
     *
     **/
    @Test
    public void testLockMechanism() throws InterruptedException {
        for (int i = 0; i < 10; i++){
            theadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    testLockMechanismService.testLockMechanism(Thread.currentThread().getId());
                }
            });
        }
        //主线程睡眠10分钟，为了子线程全部执行完再结束虚拟机。
        Thread.sleep(1000*60*10);
    }

    /**
     * 测试Try Catch Finally
     *
     **/
    @Test
    public void testTryCatchFinally(){
//        testTryCatchFinallyService.testTryCatchFinally();
        try {
            testTryCatchFinallyService.testTryFinally();
        }catch (Exception e){
            System.out.println("catch啦");
        }
        System.out.println("结束啦");
    }

    /**
     * @author wangcheng
     * @Description 测试jdk8中的lambda表达式（集合）
     **/
    @Test
    public void testLambda(){
//        testLambdaService.testLambda();
        testLambdaService.testListToMap();
    }

    /**
     * @author wangcheng
     * @Description 测试线程池
     **/
    @Test
    public void testThreadPool(){
        try {
            List<WcProductEO> wcProductEOs = getWcProductEOs();
            testThreadPoolService.testTheadPool(wcProductEOs);
        }catch (Exception e){
            log.error("异常",e);
        }
    }

    private List<WcProductEO> getWcProductEOs(){
        List<WcProductEO> wcProductEOS = new ArrayList<>();
        WcProductEO wcProductEO1 = new WcProductEO();
        wcProductEO1.setProductCode("100012");
        wcProductEO1.setProductName("薯片");
        wcProductEO1.setProductNum(5L);
        wcProductEO1.setInsertTime(new Date());
        wcProductEO1.setUpdateTime(new Date());
        WcProductEO wcProductEO2 = new WcProductEO();
        wcProductEO2.setProductCode("100013");
        wcProductEO2.setProductName("海苔");
        wcProductEO2.setProductNum(6L);
        wcProductEO2.setInsertTime(new Date());
        wcProductEO2.setUpdateTime(new Date());
        WcProductEO wcProductEO3 = new WcProductEO();
        wcProductEO3.setProductCode("100014");
        wcProductEO3.setProductName("辣条");
        wcProductEO3.setProductNum(7L);
        wcProductEO3.setInsertTime(new Date());
        wcProductEO3.setUpdateTime(new Date());
        WcProductEO wcProductEO4 = new WcProductEO();
        wcProductEO4.setProductCode("100015");
        wcProductEO4.setProductName("可乐");
        wcProductEO4.setProductNum(8L);
        wcProductEO4.setInsertTime(new Date());
        wcProductEO4.setUpdateTime(new Date());
        wcProductEOS.add(wcProductEO1);
        wcProductEOS.add(wcProductEO2);
        wcProductEOS.add(wcProductEO3);
        wcProductEOS.add(wcProductEO4);
        return wcProductEOS;
    }

    /**
     * @author wangcheng
     * @Description 测试一个类中静态成员变量，是否随着这个类的对象的改变而改变。结果是：不改变
     * 因为，一个类中的静态属性，在这个类加载的时候，就初始化这个成员变量，放在方法区里，是随着这个类的
     * 消失而消失的，不随着对象的改变而改变。
     *
     * 如果将一个类放在spring容器中，那么项目启动的时候，就会初始化配置文件，然后如果要拿这个类的对象的话，
     * 直接从工厂中拿，因为为单例模式，所以每次拿到的都是一个对象，不管哪个线程过来，都是一个同一个对象。
     **/
    @Test
    public void testBeanSingleton(){
//        testThreadPoolService.testSingleton();
//        testThreadPoolService.testSingleton2();
        Simple simple = new Simple();
        simple.initSimple();
        SimpleInner simpleInner = simple.getSimpleInner();
        Simple simple2 = new Simple();
//        simple2.initSimple();
        SimpleInner simpleInner2 = simple2.getSimpleInner();
        System.out.println("simpleInner是"+simpleInner);
        System.out.println("simpleInner2是"+simpleInner2);
        System.out.println(simpleInner == simpleInner2);
    }


    @Test
    public void testSimpleBean(){
//        String simpleName = simpleInterface.getSimpleName();
//        String simpleName2 = simpleInterface.getSimpleName();
//        System.out.println(simpleName);
//        System.out.println(simpleName2);
//        Simple simple = beanFactory.getBean(Simple.class);
//        Simple simple2 = beanFactory.getBean(Simple.class);
//        System.out.println(simple.getSimpleName());
//        System.out.println(simple2.getSimpleName());
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(CreateBeanConfig.class);
        Simple simple = applicationContext.getBean("simple", Simple.class);
        Simple simple2 = applicationContext.getBean("simple", Simple.class);
        threadPoolTaskExecutor.execute(new Runnable() {
            public void run() {
                System.out.println("nihao");
            }
        });
        Future<?> submit = threadPoolTaskExecutor.submit(new Runnable() {
            public void run() {

            }
        });
    }

    @Test
    public void testSQLSessionFactory(){
        Class<? extends SqlSessionFactoryBean> aClass = sqlSessionFactory.getClass();
    }

    @Test
    public void testAddWcProductEO(){
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setProductCode("202101181234567890");
        wcProductEO.setProductName("方便面");
        wcProductEO.setProductNum(5L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        wcProductEO.setShardDate("20210118");
        try {
            wcProductService.addWcProductEO(wcProductEO);
        }catch (Exception e){
            log.error("保存异常", e);
        }
    }

    @Test
    public void testAddUser(){
        String logId = UUID.randomUUID().toString();
        WcUserEO user = new WcUserEO();
        user.setCode("123");
        user.setName("wangcheng");
        user.setInsertTime(new Date());
        log.info("日志id：" + logId);
        try {
            testService.addUser(user);
        }catch (Exception e){
            log.error("保存异常", e);
        }
    }

    public static void main(String[] args) {
        String logId = UUID.randomUUID().toString();
        System.out.println(logId);
    }

    @Test
    public void testObjectToFun(){
//        testService.testObjectFun();
        testService.testIfElse();
    }

}
