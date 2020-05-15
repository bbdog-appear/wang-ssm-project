package com.wang.project.demo.controller;

import com.wang.project.demo.entity.User;
import com.wang.project.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/4/26 10:08 ProjectName:TestController Version:1.0
 **/
@Controller
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("/user/add")
    public String addUser(@RequestBody User user){
        testService.addUser(user);
        return "成功";
    }

    @RequestMapping("/find")
    public String findUser(){
        return "你好";
    }
}
