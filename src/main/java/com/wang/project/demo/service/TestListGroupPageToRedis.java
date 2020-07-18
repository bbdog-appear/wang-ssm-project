package com.wang.project.demo.service;

public interface TestListGroupPageToRedis {

    /**
     * 测试list集合根据某个字段分组，并分页插入redis中(jdk8中list分组)
     *
     * @param
     * @return void
     **/
    void testListGroupPageToRedis();

    /**
     * 删除redis中list数据类型中的key或value
     *
     * @param
     * @return void
     **/
    void removeListRedis();

    /**
     * pop redis list结构中的元素，优化上面的removeListRedis方法
     *
     * @param
     * @return void
     **/
    void popRedisListElements();

    /**
     * 测试其他redis的list结构的api
     *
     * @param
     * @return void
     **/
    void testOtherListRedisOpr();

}
