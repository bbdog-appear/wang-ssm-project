package com.wang.project.demo.service.rabbitMQConsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *      测试rabbitMQ消费者
 * </p>
 *
 * @author wangcheng
 * @version Id：TestRabbitMQConsumer.java Date：2020/9/21 9:41 Version：1.0
 */
@Slf4j
public class TestRabbitMQConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("结果为：" + messageStr);
        } catch (Exception e) {
            System.out.println("转换异常" + e);
        }

    }

}
