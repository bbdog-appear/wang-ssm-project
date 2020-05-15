package com.wang.project.demo.dao;

import com.wang.project.demo.entity.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

public interface TestDao {

    void insertUser(User user);
}
