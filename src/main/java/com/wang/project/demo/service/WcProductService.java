package com.wang.project.demo.service;

import com.wang.project.demo.entity.WcProductEO;

public interface WcProductService {

    /**
     * @author wangcheng
     * @Description 插入一条产品信息
     * @Date 11:02 2020/5/6
     * @Param [wcProductEO]
     * @return void
     **/
    void addWcProductEO(WcProductEO wcProductEO);

    /**
     * @author wangcheng
     * @Description 更新一条产品信息
     * @Date 15:38 2020/5/6
     * @Param [wcProductEO]
     * @return void
     **/
    void modifyWcProductEOById(WcProductEO wcProductEO);
}
