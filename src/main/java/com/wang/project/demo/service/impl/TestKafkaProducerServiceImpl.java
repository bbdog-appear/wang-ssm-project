package com.wang.project.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.TestKafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.Date;

/**
 * <p>
 *      测试kafka生产者发送消息
 * </p>
 *
 * @author wangcheng
 * @version Id：TestKafkaProducerServiceImpl.java Date：2020/9/14 18:00 Version：1.0
 */
@Slf4j
@Component
public class TestKafkaProducerServiceImpl implements TestKafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage() {
        WcProductEO wcProductEO = getWcProductEO();
        String message = JSONObject.toJSONString(wcProductEO);
        ListenableFuture<SendResult<String, String>> callable = kafkaTemplate.sendDefault(message);
        System.out.println(callable);
    }

    private WcProductEO getWcProductEO() {
        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setId(2L);
        wcProductEO.setProductCode("10002");
        wcProductEO.setProductName("产品2");
        wcProductEO.setProductNum(6L);
        wcProductEO.setInsertTime(new Date());
        wcProductEO.setUpdateTime(new Date());
        WcUserEO user = new WcUserEO();
        user.setId(4L);
        user.setCode("10002004");
        user.setName("产品2用户4");
        user.setInsertTime(new Date());
        wcProductEO.setUserList(Collections.singletonList(user));
        return wcProductEO;
    }

}
