package com.wang.project.demo.dao;

import com.wang.project.demo.entity.WcProductEO;

import java.util.List;

public interface WcProductDao {

    /**
     * @author wangcheng
     * @Description 插入一条产品信息
     * @Date 11:02 2020/5/6
     * @Param [wcProductEO]
     * @return void
     **/
    void insertWcProductEO(WcProductEO wcProductEO);

    /**
     * @author wangcheng
     * @Description 更新一条产品信息
     * @Date 15:38 2020/5/6
     * @Param [wcProductEO]
     * @return void
     **/
    void updateWcProductEOById(WcProductEO wcProductEO);

    /**
     * @author wangcheng
     * @Description 查询所有产品列表
     * @Date 16:40 2020/5/18
     * @Param []
     * @return java.util.List<com.wang.project.demo.entity.WcProductEO>
     **/
    List<WcProductEO> selectAllWcProductEOs();
}
