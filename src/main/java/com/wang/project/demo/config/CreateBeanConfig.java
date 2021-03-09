package com.wang.project.demo.config;

import com.rabbitmq.client.ConnectionFactory;
import com.wang.project.demo.test.Simple;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 *      创建bean，测试提交代码
 * <p>
 * User:wangcheng Date:2020/5/7 11:18 ProjectName:CreateBeanConfig Version:1.0
 **/
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application.xml"})
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

    @Bean(name = "factory")
    public ConnectionFactory createConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
        return factory;
    }

}
