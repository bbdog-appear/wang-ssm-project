package com.wang.project.demo.service.kafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

/**
 * <p>
 *      测试kafka类(一次可以处理一批消息，并且可以在处理完这一批消息之后提交offset，
 *      需要在消费者的配置文件中配置"max.poll.records"参数指定本批消息可以达到的最大值，
 *      并指定<property name="ackMode" value="MANUAL"/>)
 * </p>
 *
 * @author wangcheng
 * @version Id：TestBatchManualKafkaConsumer.java Date：2020/9/14 10:53 Version：1.0
 */
public class TestBatchManualKafkaConsumer implements BatchAcknowledgingMessageListener<String, String> {

    @Override
    public void onMessage(List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment acknowledgment) {

    }

}
