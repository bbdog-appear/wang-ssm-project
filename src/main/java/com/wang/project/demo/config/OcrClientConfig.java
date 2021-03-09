package com.wang.project.demo.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>
 *      配置OcrClient
 * </p>
 *
 * @author cheng.wang
 * @version Id：OcrClientConfig.java Date：2021/3/9 19:00 Version：1.0
 */
@Configuration
@PropertySource("classpath:properties/application.properties")
public class OcrClientConfig {

    @Value("${ocr.secretId}")
    private String secretId;
    @Value("${ocr.secretKey}")
    private String secretKey;
    @Value("${ocr.address}")
    private String address;
    @Value("${ocr.region}")
    private String region;

    @Bean
    public Credential createCredential(){
        Credential credential = new Credential(secretId, secretKey);
        return credential;
    }

    @Bean
    public HttpProfile createHttpProfile(){
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(address);
        return httpProfile;
    }

    @Bean
    public ClientProfile createClientProfile(HttpProfile httpProfile){
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return clientProfile;
    }

    @Bean
    public OcrClient createOcrClient(Credential cred, ClientProfile clientProfile){
        OcrClient client = new OcrClient(cred, region, clientProfile);
        return client;
    }

}
