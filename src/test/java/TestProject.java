import com.wang.project.demo.service.TestRedisMapService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName TestProject
 * @Description
 * @Author c_wangcheng-007
 * @Date 2020/7/3/003 20:50
 * @Version 1.0
 **/
public class TestProject extends DemoApplicationTest {

    @Autowired
    private TestRedisMapService testRedisMapService;

    /**
     * 测试redis hash结构
     *
     * @param
     * @return void
     **/
    @Test
    public void testRedisHash(){
        testRedisMapService.testAddRedisMap();
        testRedisMapService.testQueryRedisMap();
    }
}
