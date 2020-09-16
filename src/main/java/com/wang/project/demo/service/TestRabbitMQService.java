package com.wang.project.demo.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface TestRabbitMQService {

    /**
     * 测试生产者
     */
    void testProducer() throws Exception;

    /**
     * 测试消费者
     */
    void testConsumer() throws Exception;

}
