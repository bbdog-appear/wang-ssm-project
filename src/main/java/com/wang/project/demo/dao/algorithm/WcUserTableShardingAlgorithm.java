package com.wang.project.demo.dao.algorithm;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * <p>
 *      wc_user表分表策略
 * </p>
 *
 * @author wangcheng
 * @version Id：WcUserTableShardingAlgorithm.java Date：2021/1/14 11:06 Version：1.0
 */
@Slf4j
public class WcUserTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * 自定义精确分片算法
     *
     * @param collection 配置中actual-data-nodes表名集合[wc_user_0,wc_user_1,...,wc_user_9]
     * @param preciseShardingValue 配置文件中的sharding-column的值(运行中获取)，比如此分片键1234567879
     * @return 计算出的目标表
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String value = preciseShardingValue.getValue();
        // 获取分片键的最后一位
        String suffix = preciseShardingValue.getValue().substring(value.length() - 1);

        for (String tableName : collection) {
            if (tableName.endsWith(suffix)) {
                log.info("本次操作的表为：{}", tableName);
                return tableName;
            }
        }
        return null;
    }

}
