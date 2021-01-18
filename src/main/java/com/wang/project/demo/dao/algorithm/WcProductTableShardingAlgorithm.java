package com.wang.project.demo.dao.algorithm;

import com.google.common.collect.Range;
import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingjdbc.core.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 *      wc_product表分表策略
 *      RangeShardingAlgorithm可以搜索某段时间范围内的，例：20210101 ~ 20210331
 *
 *      Q&A:
 *      1、PreciseShardingAlgorithm和RangeShardingAlgorithm的作用？
 *      PreciseShardingAlgorithm是必选的，用于处理=和IN的分片
 *      RangeShardingAlgorithm是可选的，用于处理BETWEEN AND分片，如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理
 *
 *      2、PreciseShardingValue或RangeShardingValue的值怎么得到的？
 *      其实不是直接根据Mapper类中的参数获取的，比如(String shardDate)。而是根据mapper.xml中的sql语句，
 *      shard_date between #{startShardDate} and #{endShardDate}，然后根据@Param("startShardDate")找到对应参数的值再计算。
 *      例如：范围分片算法中Mapper类中两个参数，startShardDate,endShardDate，运行时，配置文件中shardingSqlSessionFactory中配置的
 *      扫描mapper.xml文件，找到sql语句，确认是between and，拿到对应的开始结束两个值。然后再找shardingDataSource中引入的分片算法，
 *      把这两个参数传进来用来计算表的范围。比如计算出是wc_product_202012、wc_product_202101两个表，那么查询出的结果，会自动封装
 *      到List中返回。
 * </p>
 *
 * @author wangcheng
 * @version Id：WcProductTableShardingAlgorithm.java Date：2021/1/14 10:58 Version：1.0
 */
@Slf4j
public class WcProductTableShardingAlgorithm implements PreciseShardingAlgorithm<String>, RangeShardingAlgorithm<String> {

    /**
     * 自定义精确分片算法
     *
     * @param collection 配置中actual-data-nodes表名前缀集合，此表就一个：wc_product_
     * @param preciseShardingValue 配置文件中的sharding-column的值(运行中获取)，比如此分片键20210101
     * @return 计算出的目标表
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String yearMonth = preciseShardingValue.getValue().substring(0, 6);
        String nowYearMonth = LocalDate.now().toString().replaceAll("-", "").substring(0, 6);
        // 如果是未来的月份，则返回当前月份
        if (yearMonth.compareTo(nowYearMonth) > 0) {
            yearMonth = nowYearMonth;
        }

        // 计算分表表名。findFirst：返回列表中的第一个元素。orElse：如果存在则返回，不存在返回other，也就是自定义的null。
        String targetName = collection.stream().findFirst().orElse(null) + yearMonth;
        log.info("本次操作的表为：{}", targetName);
        return targetName;
    }

    /**
     * 自定义范围分片算法
     *
     * @param collection collection 配置中actual-data-nodes表名前缀集合，此表就一个：wc_product_
     * @param rangeShardingValue 配置中的sharding-column的范围(运行中获取)，比如此分片键[20210101,20210331]，表示查询这段时间范围内的结果。
     * @return 计算出的目标表集合，比如[wc_product_202101, wc_product_202102, wc_product_202103]
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        // LinkedHashSet可以保证顺序(插入顺序与遍历顺序是一致的)，且元素不重复。
        Collection<String> targetNameSet = new LinkedHashSet<>(collection.size());

        Range<String> range = rangeShardingValue.getValueRange();
        Set<String> shardingRangeKeys = generateShardingRangeKeys(range.lowerEndpoint(), range.upperEndpoint());

        for (String tableName : collection) {
            for (String shardDate : shardingRangeKeys) {
                targetNameSet.add(tableName + shardDate);
            }
        }
        log.info("本次操作的表为：{}", targetNameSet);
        return targetNameSet;
    }

    /**
     *  根据传入的最小和最大分片键生成连续的分片键，格式：yyyyMMdd
     *  例：传入20210101, 20210331，生成[202101, 202102, 202103]
     *
     * @param shardDate1 日期字符串1
     * @param shardDate2 日期字符串2
     * @return 连续的分片键
     */
    private Set<String> generateShardingRangeKeys(String shardDate1, String shardDate2){
        LocalDate localDate1 = LocalDate.parse(shardDate1, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDate2 = LocalDate.parse(shardDate2, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return localDate1.isBefore(localDate2) ? getKeys(localDate1, localDate2) : getKeys(localDate2, localDate1);
    }

    /**
     * 根据传入的最小和最大分片键生成连续的分片键
     * 例：传入20210101, 20210331，生成[202101, 202102, 202103]
     *
     * @param minShardDate 最小日期
     * @param MaxShardDate 最大日期
     * @return 连续的分片键
     */
    private Set<String> getKeys(LocalDate minShardDate, LocalDate MaxShardDate){
        // TemporalAdjusters.firstDayOfMonth():返回每月的第一天。LocalDate.with():返回此日期的调整副本。
        minShardDate = minShardDate.with(TemporalAdjusters.firstDayOfMonth());
        MaxShardDate = MaxShardDate.with(TemporalAdjusters.firstDayOfMonth());

        LocalDate nowDate = LocalDate.now();
        MaxShardDate = MaxShardDate.isBefore(nowDate) ? MaxShardDate : nowDate;

        // 有序的，不重复，线程不安全
        Set<String> treeSet = new TreeSet<>();
        while (!minShardDate.isAfter(MaxShardDate)) {
            treeSet.add(minShardDate.format(DateTimeFormatter.ofPattern("yyyyMM")));
            minShardDate = minShardDate.plusMonths(1L);
        }
        return treeSet;
    }

}
