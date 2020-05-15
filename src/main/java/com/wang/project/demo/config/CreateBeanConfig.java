package com.wang.project.demo.config;

import com.wang.project.demo.test.Simple;
import lombok.Data;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/7 11:18 ProjectName:CreateBeanConfig Version:1.0
 **/
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class CreateBeanConfig {

//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    public void testSQLSessionFactory() throws SQLException {
//        Connection connection = dataSource.getConnection();
//    }
//
//    @Bean(name = "sqlSessionFactory")
//    public SqlSessionFactoryBean getSqlSessionFactoryBean(){
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean;
//    }

    @Bean(name = "simple")
    public Simple createSimpleBean(){
        Simple simple = new Simple();
        simple.setSimpleName("简单名字");
        return simple;
    }
}
