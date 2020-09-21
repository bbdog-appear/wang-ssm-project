package com.wang.project.demo.service;

public interface TestRabbitMQService {

    /**
     * 测试生产者
     */
    void testProducer() throws Exception;

    /**
     * 测试消费者
     */
    void testConsumer() throws Exception;

    /**
     * 测试Spring配置的生产者
     */
    void testSpringProducer() throws Exception;

    /**
     * 测试Spring配置的消费者
     */
    void testSpringConsumer() throws Exception;

}
