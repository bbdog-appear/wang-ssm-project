package com.wang.project.demo.dao.mapper.shardingjdbc;

import com.wang.project.demo.entity.WcUserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WcUserMapper {

    /**
     * 插入
     *
     * @param wcUserEO wcUserEO
     */
    void insert(WcUserEO wcUserEO);

    /**
     * 根据contractNo客户号 分表键查询
     *
     * @return 用户列表
     */
    List<WcUserEO> selectByContractNo(@Param("contractNo") String contractNo);

}
