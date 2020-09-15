package com.wang.project.demo.service.kafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.entity.WcProductEO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchMessageListener;

import java.util.List;

/**
 * <p>
 *      测试kafka类(一次接受一批消息,消息的数量是随机的，但最大不会超过"max.poll.records"参数配置的数量)
 * </p>
 *
 * @author wangcheng
 * @version Id：TestBatchAutoKafkaConsumer.java Date：2020/9/14 10:48 Version：1.0
 */
@Slf4j
public class TestBatchAutoKafkaConsumer implements BatchMessageListener<String, String> {

    @Override
    public void onMessage(List<ConsumerRecord<String, String>> consumerRecords) {
        for (ConsumerRecord<String, String> data : consumerRecords) {
            String topic = data.topic();
            System.out.println("topic为：" + topic);
            long offset = data.offset();
            System.out.println("offset为：" + offset);
            int partition = data.partition();
            System.out.println("partition为：" + partition);
            String value = data.value();
            WcProductEO wcProductEO = JSONObject.parseObject(value, WcProductEO.class);
            System.out.println("消息为：" + wcProductEO);
        }
    }

}
