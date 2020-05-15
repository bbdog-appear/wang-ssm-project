import com.wang.project.demo.config.CreateBeanConfig;
import com.wang.project.demo.entity.User;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestService;
import com.wang.project.demo.service.TestThreadPoolService;
import com.wang.project.demo.service.WcProductService;
import com.wang.project.demo.test.Simple;
import com.wang.project.demo.test.SimpleInner;
import com.wang.project.demo.test.SimpleInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/4/26 13:36 ProjectName:DemoApplicationTest Version:1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
@Slf4j
public class DemoApplicationTest {
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

    /**
     * @author wangcheng
     * @Description 测试线程池
     * @Date 9:30 2020/5/15
     * @Param []
     * @return void
     **/
    @Test
    public void testThreadPool(){
        List<WcProductEO> wcProductEOs = getWcProductEOs();
        testThreadPoolService.testTheadPool(wcProductEOs);
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
     * @Date 14:09 2020/5/15
     * @Param []
     * @return void
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
        wcProductEO.setProductCode("13579");
        wcProductEO.setProductName("牙膏");
        wcProductEO.setProductNum(5L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        try {
            wcProductService.addWcProductEO(wcProductEO);
        }catch (Exception e){
            log.error("保存异常", e);
        }
    }

    @Test
    public void testAddUser(){
        String logId = UUID.randomUUID().toString();
        User user = new User();
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
}
