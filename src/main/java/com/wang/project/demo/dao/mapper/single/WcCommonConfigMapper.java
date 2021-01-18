package com.wang.project.demo.dao.mapper.single;

import com.wang.project.demo.entity.WcCommonConfigEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WcCommonConfigMapper {

    /**
     * 插入
     *
     * @param wcCommonConfigEO EO
     */
    void insert(WcCommonConfigEO wcCommonConfigEO);

    /**
     * 根据commonType查询
     *
     * @param commonType 类型
     * @return 结果
     */
    List<WcCommonConfigEO> selectByCommonType(@Param("commonType") String commonType);

}
