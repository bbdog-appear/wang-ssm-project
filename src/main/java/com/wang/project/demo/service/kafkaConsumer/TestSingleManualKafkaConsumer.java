package com.wang.project.demo.service.kafkaConsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * <p>
 *      测试kafka类(一次接收一条消息，可以通过acknowledgment来手动提交offset，
 *      需要在消费者的配置中指定<property name="ackMode" value="MANUAL_IMMEDIATE"/>)
 * </p>
 *
 * @author wangcheng
 * @version Id：TestSingleManualKafkaConsumer.java Date：2020/9/14 10:51 Version：1.0
 */
@Slf4j
public class TestSingleManualKafkaConsumer implements AcknowledgingMessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord, Acknowledgment acknowledgment) {

    }

}
