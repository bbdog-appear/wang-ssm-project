import com.wang.project.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

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
    public void testRedisson(){
        try {
            testRedissonService.testRedissonWriteLock();
//            testRedissonService.testRedissonReadLock();
        } catch (Exception e){
            log.error("操作redisson异常", e);
        }
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
        testRabbitMQService.testProducer();
        testRabbitMQService.testConsumer();
    }

}
