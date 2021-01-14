package com.wang.project.demo.dao.mapper.shardingjdbc;

import com.wang.project.demo.entity.WcProductEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WcProductMapper {

    /**
     * 插入
     *
     * @param wcProductEO wcProductEO
     */
    void insert(WcProductEO wcProductEO);

    /**
     * 更新
     *
     * @param wcProductEO wcProductEO
     */
    void updateByIdAndShardDate(WcProductEO wcProductEO);

    /**
     * 根据分表日期查询
     *
     * @param shardDate 分表日期
     * @return 结果
     */
    List<WcProductEO> selectByShardDate(@Param("shardDate") String shardDate);

    /**
     * 根据分表日期+插入时间查询
     *
     * @param insertTime 插入时间
     * @param shardDate 分表日期
     * @return 结果
     */
    List<WcProductEO> selectByInsertTimeAndShard(@Param("insertTime") String insertTime,
                                                 @Param("shardDate") String shardDate);

}
