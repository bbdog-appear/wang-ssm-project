package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.WcProductDao;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.WcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/6 11:03 ProjectName:WcProductServiceImpl Version:1.0
 **/
@Service
public class WcProductServiceImpl implements WcProductService{
    @Autowired
    private WcProductDao wcProductDao;

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addWcProductEO(WcProductEO wcProductEO) {
        wcProductDao.insertWcProductEO(wcProductEO);
//        modifyWcProductEOById(getWcProductEO());
//        throw new RuntimeException("自己抛出的运行时异常");
    }

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyWcProductEOById(WcProductEO wcProductEO) {
        wcProductDao.updateWcProductEOById(wcProductEO);
        throw new RuntimeException("自己抛出的运行时异常");
    }

    private WcProductEO getWcProductEO(){
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setId(6L);
        wcProductEO.setProductCode("1111");
        wcProductEO.setProductName("空调");
        wcProductEO.setProductNum(7L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        return wcProductEO;
    }

}
