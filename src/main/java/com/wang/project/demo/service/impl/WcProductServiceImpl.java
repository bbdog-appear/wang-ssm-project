package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.mapper.shardingjdbc.WcProductMapper;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.WcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User:wangcheng Date:2020/5/6 11:03 ProjectName:WcProductServiceImpl Version:1.0
 **/
@Service
public class WcProductServiceImpl implements WcProductService{
    @Autowired
    private WcProductMapper wcProductMapper;

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addWcProductEO(WcProductEO wcProductEO) {
        wcProductMapper.insert(wcProductEO);
//        modifyWcProductEOById(getWcProductEO());
//        throw new RuntimeException("自己抛出的运行时异常");
    }

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyWcProductEOById(WcProductEO wcProductEO) {
        wcProductMapper.updateByIdAndShardDate(wcProductEO);
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
        wcProductEO.setShardDate("20200506");
        return wcProductEO;
    }

}
