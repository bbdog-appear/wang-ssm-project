package com.wang.project.demo.service;

/**
 * @Description 测试redis map结构
 * User:wangcheng Date:2020/7/10 11:15 ProjectName:TestRedisMapService Version:1.0
 **/
public interface TestRedisMapService {

    /**
     * 测试将信息入redis map结构
     *
     * @param
     * @return void
     **/
    void testAddRedisMap();

    /**
     * 测试从redis map结构中查询数据
     *
     * @param
     * @return void
     **/
    void testQueryRedisMap();
}
