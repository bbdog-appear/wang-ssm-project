package com.wang.project.demo.commons;

/**
 * <p>
 *      分片套件
 * </p>
 *
 * @author wangcheng
 * @version Id：ShardingKit.java Date：2020/11/26 17:27 Version：1.0
 */
public class ShardingKit {

    /**
     * 对模数取余
     *
     * @param modKey 取模字段(可用ID)
     * @param shardKey 定时任务分片总数
     * @return 分片项
     */
    public static Integer getSharding(String modKey, int shardKey){
        return modKey.hashCode() % shardKey;
    }

}
