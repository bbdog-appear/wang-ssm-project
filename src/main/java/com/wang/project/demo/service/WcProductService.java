package com.wang.project.demo.service;

import com.wang.project.demo.entity.WcProductEO;

import java.util.List;

public interface WcProductService {

    void addWcProductEO(WcProductEO wcProductEO);

    void modifyWcProductEOById(WcProductEO wcProductEO);

    void batchAdd(List<WcProductEO> wcProductEOList);

    List<WcProductEO> queryByShardDate(String shardDate);

    List<WcProductEO> queryRangeSharding(String startShardDate, String endShardDate);

}
