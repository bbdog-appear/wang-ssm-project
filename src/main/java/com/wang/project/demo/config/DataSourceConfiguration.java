package com.wang.project.demo.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/7 11:28 ProjectName:DataSourceConfiguration Version:1.0
 **/
@Configuration
public class DataSourceConfiguration {

    public BasicDataSource createDataSource(){
        BasicDataSource dataSource = new BasicDataSource();

        return dataSource;
    }
}
