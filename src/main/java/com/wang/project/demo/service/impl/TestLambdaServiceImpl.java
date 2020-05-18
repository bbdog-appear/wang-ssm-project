package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.WcProductDao;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestLambdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/18 16:36 ProjectName:TestLambdaServiceImpl Version:1.0
 **/
@Service
public class TestLambdaServiceImpl implements TestLambdaService {

    @Autowired
    private WcProductDao wcProductDao;

    @Override
    public void testLambda() {
        //获取产品列表
        List<WcProductEO> wcProductEOS = wcProductDao.selectAllWcProductEOs();
        //过滤出产品名为薯片的产品id
        List<Long> productIds = wcProductEOS.stream().filter(
                wcProductEO -> "薯片".equals(wcProductEO.getProductName())).
                map(WcProductEO::getId).collect(Collectors.toList());
        //输出产品id字符串
        productIds.forEach(id -> {
            String sid = String.valueOf(id);
            System.out.println(sid);
        });
    }
}
