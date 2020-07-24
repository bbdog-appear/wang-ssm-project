import com.wang.project.demo.service.TestRedissonService;
import com.wang.project.demo.service.TestThreadLocalService;
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

}
