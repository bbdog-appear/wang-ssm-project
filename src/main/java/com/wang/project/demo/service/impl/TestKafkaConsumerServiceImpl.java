package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestKafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * <p>
 *      测试kafka消费者接收消息
 * </p>
 *
 * @author wangcheng
 * @version Id：TestKafkaConsumerServiceImpl.java Date：2020/9/14 18:44 Version：1.0
 */
@Slf4j
@Component
public class TestKafkaConsumerServiceImpl implements TestKafkaConsumerService {

    @Autowired
    private ConcurrentMessageListenerContainer<String, String> messageListenerContainer;

    @Override
    public void receiveMessage() {
        for (int i = 0; i < 5; i++) {
            messageListenerContainer.start();
        }
    }

}
