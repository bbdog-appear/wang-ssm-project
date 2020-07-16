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
public class DemoSummaryTest extends DemoApplicationTest{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisOperate(){
        redisTemplate.opsForValue().set("wangcheng_1", "value1");
        redisTemplate.opsForValue().set("wangcheng_2", "value2");
        Set<String> keys = redisTemplate.keys("wangcheng_*");
        System.out.println(keys);
    }
}
