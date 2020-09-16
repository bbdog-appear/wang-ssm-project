package com.wang.project.demo.service.impl;

import com.rabbitmq.client.*;
import com.wang.project.demo.service.TestRabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *      测试rabbitMQ类，原始连接方式(不是用spring集成的rabbitMQ)
 * </p>
 *
 * @author wangcheng
 * @version Id：TestRabbitMQServiceImpl.java Date：2020/9/16 17:46 Version：1.0
 */
@Component
@Slf4j
public class TestRabbitMQServiceImpl implements TestRabbitMQService {

    @Autowired
    private ConnectionFactory factory;

    @Override
    public void testProducer() throws Exception {
        // 建立到代理服务器到连接
        Connection connection = factory.newConnection();
        // 获得信道
        Channel channel = connection.createChannel();
        // 声明交换器
        String exchangeName = "Hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        // 路由
        String routingKey = "Hello-rout";
        // 发布消息
        byte[] messageBodyBytes = "quit".getBytes();
        channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
        // 关闭资源
        channel.close();
        connection.close();
    }

    @Override
    public void testConsumer() throws Exception{
        // 建立到代理服务器到连接
        Connection connection = factory.newConnection();
        // 获得信道
        Channel channel = connection.createChannel();
        // 声明交换器
        String exchangeName = "Hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        //声明队列
        String queueName = channel.queueDeclare().getQueue();
        // 路由
        String routingKey = "Hello-rout";
        //绑定队列，通过键 hola 将队列和交换器绑定起来
        channel.queueBind(queueName, exchangeName, routingKey);

        while (true) {
            String consumerTag = "";
            channel.basicConsume(queueName, false, consumerTag, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("消费的路由键：" + routingKey);
                    System.out.println("消费的内容类型：" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    System.out.println("消费的消息体内容：");
                    String bodyStr = new String(body, StandardCharsets.UTF_8);
                    System.out.println(bodyStr);
                }
            });
        }
    }

}
