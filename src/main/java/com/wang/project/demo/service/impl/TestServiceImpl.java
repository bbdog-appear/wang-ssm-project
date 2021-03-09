package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.mapper.single.WcCommonConfigMapper;
import com.wang.project.demo.dao.mapper.shardingjdbc.WcUserMapper;
import com.wang.project.demo.entity.WcCommonConfigEO;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.TestService;
import com.wang.project.demo.service.WcProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User:wangcheng Date:2020/4/26 10:05 ProjectName:TestServiceImpl Version:1.0
 **/
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired(required = false)
    private WcUserMapper wcUserMapper;
    @Autowired(required = false)
    private WcProductService wcProductService;
    @Autowired(required = false)
    private WcCommonConfigMapper wcCommonConfigMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser(WcUserEO wcUserEO) {
        log.info("=========================================测试日志控件 Slf4j");
        wcUserMapper.insert(wcUserEO);
        wcProductService.addWcProductEO(getWcProductEO());
//        throw new RuntimeException("自己抛出的运行时异常");
    }

    @Override
    public void testObjectToFun(WcUserEO wcUserEO) {
        wcUserEO = new WcUserEO();
        wcUserEO.setName("name2");
        System.out.println(wcUserEO);
    }

    /**
     * 测试java对象传递
     * 该方法中的user，传进testObjectToFun里，然后这个方法里的user因为是方法里的参数，
     * 所以是一个新的引用了，所以新的引用指向另一个新的对象。所以对该方法的user没有任何
     * 影响。
     */
    @Override
    public void testObjectFun() {
        WcUserEO wcUserEO = new WcUserEO();
        wcUserEO.setName("name1");
        testObjectToFun(wcUserEO);
        System.out.println(wcUserEO);
    }

    /**
     * 测试if else
     * 如果第一个if中的&&有一个不满足，则直接跳到下一个if else
     * 那如果第一个if中满足，然后进入自己的if里面，再不满足则直接跳出所有if
     */
    @Override
    public void testIfElse() {
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setProductCode("wangchengProduct");
        wcProductEO.setProductName("wanghcengzhognwen");
        WcUserEO user1 = new WcUserEO();
        user1.setCode("user1");
        user1.setName("用户1");
        WcUserEO user2 = new WcUserEO();
        user2.setCode("user2");
        user2.setName("用户2");
        List<WcUserEO> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        wcProductEO.setUserList(users);

        if (wcProductEO != null && !CollectionUtils.isEmpty(wcProductEO.getUserList()) && "user2".equals(wcProductEO.getUserList().get(0).getCode())) {
            System.out.println("第一个if");
        } else if ("wangchengProduct".equals(wcProductEO.getProductCode())) {
            System.out.println("第二个if else");
        } else if ("wanghcengzhognwen".equals(wcProductEO.getProductName())) {
            System.out.println("第三个if else");
        }

        if (wcProductEO != null && !CollectionUtils.isEmpty(wcProductEO.getUserList())) {
            if ("user2".equals(wcProductEO.getUserList().get(0).getCode())) {
                System.out.println("第一个if");
            }
        } else if ("wangchengProduct".equals(wcProductEO.getProductCode())) {
            System.out.println("第二个if else");
        } else if ("wanghcengzhognwen".equals(wcProductEO.getProductName())) {
            System.out.println("第三个if else");
        }
    }

    private WcProductEO getWcProductEO(){
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setProductCode("13579");
        wcProductEO.setProductName("牙膏");
        wcProductEO.setProductNum(5L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        return wcProductEO;
    }

    @Override
    public void addUserEO(WcUserEO wcUserEO) {
        wcUserMapper.insert(wcUserEO);
    }

    @Override
    public List<WcUserEO> queryByContractNo(String contractNo){
        return wcUserMapper.selectByContractNo(contractNo);
    }

    @Override
    public void addCommonConfig(WcCommonConfigEO wcCommonConfigEO) {
        wcCommonConfigMapper.insert(wcCommonConfigEO);
    }

}
