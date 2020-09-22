package com.wang.project.demo.service.impl;

import com.rabbitmq.client.*;
import com.wang.project.demo.service.TestRabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    @Override
    public void testSpringProducer() throws Exception{
        rabbitTemplate.convertAndSend("exchange_1_queue_1_message_1");
        // 队列在配置中已经配好，这里可以指定一下routerKey(路由key)
        rabbitTemplate.convertAndSend("queue_1_key", "exchange_1_queue_1_message_1");
        Thread.sleep(50000);
    }

    @Override
    public void testSpringConsumer() throws Exception{
        /*
        使用消息中间件的目的:
            1.模块解耦, 各个模块各司其职, 避免业务耦合:
                应用场景:商品上下架时,可以将Solr索引库的同步请求发送到消息队列, 让搜索微服务自己去同步Solr库,
                而商品模块可以直接认为商品同步成功, 并且此处的Solr索引同步延迟是可以接受的;
            2.异步处理,将不需要同步处理的业务进行异步处理, 提高接口响应速度:
                典型场景,后端生成验证码之后不必等待短信系统发送完再将结果返回, 当提交给短信发送微服务后即可认
                为发送成功, 故可直接通知用户短信已发送;
            3.削峰填谷, 均衡整个系统的负载:
                rabbitmq是默认单线程消费某个队列的消息, 也可以通过设置, 进行并发消费,总之其实可控的,避免高并发下的系统假死;
                可以设置最大消息数, 当超过时直接丢弃, 保证系统不至于过载;
        应用场景:秒杀;
         */
    }

}
