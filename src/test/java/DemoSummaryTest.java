import com.wang.project.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *      演示测试汇总类
 * </p>
 *
 * @author wangcheng
 * @version Id：DemoSummaryTest.java Date：2020/7/15 9:27 Version：1.0
 */
@Slf4j
public class DemoSummaryTest extends DemoApplicationTest{
    //线程池
    private ThreadPoolExecutor theadPoolExecutor = new ThreadPoolExecutor(
            10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TestRedissonService testRedissonService;
    @Autowired
    private TestThreadLocalService testThreadLocalService;
    @Autowired
    private TestReflectService testReflectService;
    @Autowired
    private TestKafkaProducerService testKafkaProducerService;
    @Autowired
    private TestKafkaConsumerService testKafkaConsumerService;
    @Autowired
    private TestRabbitMQService testRabbitMQService;
    @Autowired
    private TestGuavaThreadPoolService testGuavaThreadPoolService;

    @Test
    public void testRedisOperate(){
        redisTemplate.opsForValue().set("wangcheng_1", "value1");
        redisTemplate.opsForValue().set("wangcheng_2", "value2");
        Set<String> keys = redisTemplate.keys("wangcheng_*");
        System.out.println(keys);
    }

    @Test
    public void testIntAdd() {
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < 5; i++) {
            count++;
            ++count2;
            System.out.println("count：" + count);
            System.out.println("count2：" + count2);
        }
    }

    /**
     * 测试redisson操作
     */
    @Test
    public void testRedisson() throws Exception{
        for (int i = 0; i < 2; i++) {
            int threadNum = i;
            theadPoolExecutor.execute(() -> {
                try {
                    testRedissonService.testRedissonWriteLock(threadNum + 1);
                } catch (InterruptedException e) {
                    System.out.println("线程" + (threadNum+1) + "没拿到锁，结束");
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(60000);
//        testRedissonService.testRedissonWriteLock();
//        testRedissonService.testRedissonReadLock();
    }

    /**
     * 测试threadLocal
     */
    @Test
    public void testThreadLocal(){
        try {
            testThreadLocalService.testThreadLocal();
        } catch (Exception e) {
            log.error("测试threadLocal异常", e);
        }
    }

    /**
     * 测试反射
     */
    @Test
    public void testReflect() {
        testReflectService.testReflectAddRedis();
        testReflectService.testReflectGetRedis();
    }

    /**
     * 测试kafka发送消息
     */
    @Test
    public void sendMessage(){
        testKafkaProducerService.sendMessage();
    }

    /**
     * 测试kafka接收消息
     */
    @Test
    public void receiveMessage(){
        testKafkaConsumerService.receiveMessage();
        testKafkaConsumerService.batchAutoReceiveMessage();
    }

    /**
     * 测试rabbitMQ
     *
     * @throws Exception 异常
     */
    @Test
    public void testRabbitMQ() throws Exception{
//        testRabbitMQService.testProducer();
//        testRabbitMQService.testConsumer();
        testRabbitMQService.testSpringProducer();
//        testRabbitMQService.testSpringConsumer();
    }

    /**
     * 测试guava线程池
     *
     * @throws Exception 异常
     */
    @Test
    public void testGuavaThreadPool() throws Exception{
        testGuavaThreadPoolService.testGuavaThreadPool();
    }

}
