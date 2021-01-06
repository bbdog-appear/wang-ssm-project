import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
