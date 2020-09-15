package com.wang.project.demo.service;

public interface TestKafkaConsumerService {

    /**
     * 测试kafka消费者接收消息
     */
    void receiveMessage();

    /**
     * 测试kafka消费者批量接收消息。自动提交offset
     */
    void batchAutoReceiveMessage();

}
