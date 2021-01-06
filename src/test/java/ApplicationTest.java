import com.wang.project.demo.service.TestLambdaService;
import com.wang.project.demo.service.TestSimpleService;
import com.wang.project.demo.service.TestTryCatchFinallyService;
import com.wang.project.demo.vo.Goods;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * <p>
 *      测试包下的测试类
 * </p>
 *
 * @author wangcheng
 * @version Id：ApplicationTest.java Date：2021/1/6 9:32 Version：1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-test.xml"})
@Slf4j
public class ApplicationTest {

    @Autowired
    private TestSimpleService testSimpleService;
    @Autowired
    private TestTryCatchFinallyService testTryCatchFinallyService;

    @Test
    public void testLambda(){
        UserProductVO userProductVO = new UserProductVO();
        userProductVO.setTraceLogId(UUID.randomUUID().toString());
        String result = testSimpleService.testSimple(userProductVO);
//        testTryCatchFinallyService.testTryCatchFinally(new Goods());
        log.info("结果是：{}", result);
    }

}
