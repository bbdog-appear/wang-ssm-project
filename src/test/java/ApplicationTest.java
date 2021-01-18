import com.wang.project.demo.entity.WcCommonConfigEO;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.*;
import com.wang.project.demo.vo.Goods;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

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
    @Autowired
    private WcProductService wcProductService;
    @Autowired
    private TestService testService;

    @Test
    public void testLambda(){
        UserProductVO userProductVO = new UserProductVO();
        userProductVO.setTraceLogId(UUID.randomUUID().toString());
        String result = testSimpleService.testSimple(userProductVO);
//        testTryCatchFinallyService.testTryCatchFinally(new Goods());
        log.info("结果是：{}", result);
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

    /**
     * 批量插入，测试结果，由于shardDate计算出来是两个表，202101和202102，
     * 但是保存的话，只保存在了202101的表中，这是个bug吗？
     * 答：这个不是bug，因为分片算法里，有个逻辑，如果时间大于当前时间，则
     * 返回当前时间，现在时间是：2021年1月18日17:19:32，那么如果传202102，
     * 那么直接返回的是202101。
     */
    @Test
    public void testBatchAdd(){
        List<WcProductEO> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            WcProductEO wcProductEO = new WcProductEO();
            wcProductEO.setProductCode("20210118123456789" + i);
            wcProductEO.setProductName("方便面" + i);
            wcProductEO.setProductNum(5L + i);
            wcProductEO.setInsertTime(new Date());
            wcProductEO.setUpdateTime(new Date());
            wcProductEO.setShardDate("20210118");
            list.add(wcProductEO);
        }
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setProductCode("202101181234567896");
        wcProductEO.setProductName("方便面3");
        wcProductEO.setProductNum(7L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        wcProductEO.setShardDate("20201218");
        list.add(wcProductEO);
        WcProductEO wcProductEO2 = new WcProductEO();
        wcProductEO2.setProductCode("202101181234567896");
        wcProductEO2.setProductName("方便面3");
        wcProductEO2.setProductNum(7L);
        wcProductEO2.setInsertTime(new Date());
        wcProductEO2.setUpdateTime(new Date());
        wcProductEO2.setShardDate("20210225");
        list.add(wcProductEO2);
        wcProductService.batchAdd(list);
    }

    @Test
    public void testQueryByShardDate(){
        List<WcProductEO> wcProductEOS = wcProductService.queryByShardDate("20201215");
        log.info("=====结果是：{}", wcProductEOS);
    }

    @Test
    public void testQueryRangeSharding(){
        List<WcProductEO> wcProductEOS = wcProductService.queryRangeSharding("20201215", "20210118");
        log.info("=====结果是：{}", wcProductEOS);
    }

    @Test
    public void testAddUser(){
        WcUserEO user = new WcUserEO();
        user.setCode("20210118123456788");
        user.setName("panda");
        user.setInsertTime(new Date());
        user.setContractNo("20210118124457");
        try {
            testService.addUserEO(user);
        }catch (Exception e){
            log.error("保存异常", e);
        }
    }

    @Test
    public void testQueryByContractNo(){
        List<WcUserEO> wcUserEOS = testService.queryByContractNo("20210118124457");
        log.info("=======结果是：{}", wcUserEOS);
    }

    @Test
    public void testAddCommonConfig(){
        WcCommonConfigEO wcCommonConfigEO = new WcCommonConfigEO();
        wcCommonConfigEO.setCommonType("furniture");
        wcCommonConfigEO.setCommonKey("cabinet");
        wcCommonConfigEO.setCommonValue("柜子");
        try {
            testService.addCommonConfig(wcCommonConfigEO);
        }catch (Exception e){
            log.error("保存异常", e);
        }
    }

}
