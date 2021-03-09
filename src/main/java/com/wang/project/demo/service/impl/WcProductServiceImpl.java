package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.mapper.shardingjdbc.WcProductMapper;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.WcProductService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * User:wangcheng Date:2020/5/6 11:03 ProjectName:WcProductServiceImpl Version:1.0
 **/
@Service
public class WcProductServiceImpl implements WcProductService{
    @Autowired(required = false)
    private WcProductMapper wcProductMapper;
    @Autowired(required = false)
    private SqlSessionFactory shardingSqlSessionFactory;

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

    @Override
    public void batchAdd(List<WcProductEO> wcProductEOList) {
        try (SqlSession sqlSession = shardingSqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            WcProductMapper mapper = sqlSession.getMapper(WcProductMapper.class);
            wcProductEOList.forEach(mapper::insert);
            sqlSession.commit();
        }
    }

    @Override
    public List<WcProductEO> queryByShardDate(String shardDate) {
        return wcProductMapper.selectByShardDate(shardDate);
    }

    @Override
    public List<WcProductEO> queryRangeSharding(String startShardDate, String endShardDate) {
        return wcProductMapper.selectByShardDateRange(startShardDate, endShardDate);
    }

}
