package com.wang.project.demo.service.kafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.entity.WcProductEO;
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
    public void onMessage(ConsumerRecord<String, String> data) {
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
