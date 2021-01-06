package com.wang.project.demo.service.impl;

import com.wang.project.demo.service.TestSimpleService;
import com.wang.project.demo.vo.UserProductVO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      简单的测试ServiceImpl，不加载任何配置
 * </p>
 *
 * @author wangcheng
 * @version Id：TestSimpleServiceImpl.java Date：2021/1/6 9:40 Version：1.0
 */
@Service
public class TestSimpleServiceImpl implements TestSimpleService {

    @Override
    public String testSimple(UserProductVO userProductVO) {
        System.out.println("这是个简单的测试ServiceImpl");
        return "结果返回";
    }

}
