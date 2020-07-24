package com.wang.project.demo.service.impl;

import com.wang.project.demo.biz.TestThreadLocalBiz;
import com.wang.project.demo.service.TestThreadLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      测试threadLocal
 * </p>
 *
 * @author wangcheng
 * @version Id：TestThreadLocalServiceImpl.java Date：2020/7/24 9:14 Version：1.0
 */
@Service
public class TestThreadLocalServiceImpl implements TestThreadLocalService {

    @Autowired
    private TestThreadLocalBiz testThreadLocalBiz;

    @Override
    public void testThreadLocal() throws Exception {
        for (int i = 0; i < 10; i++) {
            testThreadLocalBiz.testThreadLocal();
        }
    }

}
