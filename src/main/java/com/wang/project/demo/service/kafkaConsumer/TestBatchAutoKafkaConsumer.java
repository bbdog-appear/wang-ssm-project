package com.wang.project.demo.service.kafkaConsumer;

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

    }

}
