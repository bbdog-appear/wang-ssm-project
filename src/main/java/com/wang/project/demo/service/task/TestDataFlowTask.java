package com.wang.project.demo.service.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.wang.project.demo.commons.ShardingKit;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *      测试数据流式作业模式
 * </p>
 *
 * @author wangcheng
 * @version Id：TestDataFlowTask.java Date：2020/11/26 17:21 Version：1.0
 */
@Component
@Slf4j
public class TestDataFlowTask implements DataflowJob<UserProductVO> {

    /**
     * 抓取数据，返回本台机器分片项将要处理的数据集合
     *
     * @param shardingContext ShardingContext
     * @return 抓取到的本台机器的数据
     */
    public List<UserProductVO> fetchData(ShardingContext shardingContext) {
        List<UserProductVO> list = new ArrayList<>();
        List<UserProductVO> shardList = list.stream().filter(item -> ShardingKit.getSharding(
                item.getId().toString(), 4) == shardingContext.getShardingItem()).collect(Collectors.toList());
        return shardList;
    }

    /**
     * 逻辑处理
     *
     * @param shardingContext ShardingContext
     * @param list 上面抓取到的本台机器的数据
     */
    public void processData(ShardingContext shardingContext, List<UserProductVO> list) {
        System.out.println(list);
    }

}
