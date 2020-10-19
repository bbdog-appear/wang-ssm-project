package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.TestDao;
import com.wang.project.demo.entity.User;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestService;
import com.wang.project.demo.service.WcProductService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/4/26 10:05 ProjectName:TestServiceImpl Version:1.0
 **/
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;
    @Autowired
    private WcProductService wcProductService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser(User user) {
        log.info("=========================================测试日志控件 Slf4j");
        testDao.insertUser(user);
        wcProductService.addWcProductEO(getWcProductEO());
//        throw new RuntimeException("自己抛出的运行时异常");
    }

    @Override
    public void testObjectToFun(User user) {
        user = new User();
        user.setName("name2");
        System.out.println(user);
    }

    /**
     * 测试java对象传递
     * 该方法中的user，传进testObjectToFun里，然后这个方法里的user因为是方法里的参数，
     * 所以是一个新的引用了，所以新的引用指向另一个新的对象。所以对该方法的user没有任何
     * 影响。
     */
    @Override
    public void testObjectFun() {
        User user = new User();
        user.setName("name1");
        testObjectToFun(user);
        System.out.println(user);
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
}
