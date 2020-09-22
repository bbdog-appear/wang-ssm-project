package com.wang.project.demo.service.rabbitMQConsumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;

import java.io.IOException;
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

    /**
     * 模拟springboot注解形式的消费者
     *
     * @param message 消息
     * @param channel 通道
     */
//    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue-1"),
//                    exchange = @Exchange(value = "exchange")))
//    @RabbitHandler
//    public void onMessage(Message message, Channel channel){
//        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
//        System.out.println("结果为：" + messageStr);
//        try {
//            channel.basicAck(1, false);
//        } catch (IOException e) {
//            System.out.println("自定义消费者异常" + e);
//        }
//    }

}
