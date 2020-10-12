package com.wang.project.demo.dao;

import com.wang.project.demo.entity.User;

import java.util.List;

public interface TestDao {

    void insertUser(User user);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> selectAllUser();

}
