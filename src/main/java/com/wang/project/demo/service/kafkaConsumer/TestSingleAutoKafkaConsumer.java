package com.wang.project.demo.service.kafkaConsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * <p>
 *      测试kafka类(一次处理一条消息，而且是自动提交offset)
 * </p>
 *
 * @author wangcheng
 * @version Id：TestSingleAutoKafkaConsumer.java Date：2020/9/14 10:40 Version：1.0
 */
@Slf4j
public class TestSingleAutoKafkaConsumer implements MessageListener<String, String> {


    @Override
    public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {

    }

}
