package com.wang.project.demo.service.impl;

import com.wang.project.demo.dao.mapper.shardingjdbc.WcProductMapper;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestLambdaService;
import com.wang.project.demo.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User:wangcheng Date:2020/5/18 16:36 ProjectName:TestLambdaServiceImpl Version:1.0
 **/
@Service
public class TestLambdaServiceImpl implements TestLambdaService {

    @Autowired(required = false)
    private WcProductMapper wcProductMapper;

    @Override
    public void testLambda() {
        //获取产品列表
        List<WcProductEO> wcProductEOS = wcProductMapper.selectByShardDate("20200518");

        //过滤出产品名为薯片的产品id
//        List<Long> productIds = wcProductEOS.stream().filter(
//                wcProductEO -> "薯片".equals(wcProductEO.getProductName())).
//                map(WcProductEO::getId).collect(Collectors.toList());
//
//        //输出产品id字符串
//        productIds.forEach(id -> {
//            String sid = String.valueOf(id);
//            System.out.println(sid);
//        });

        Map<String, String> collect = wcProductEOS.stream().collect(Collectors.toMap(WcProductEO::getProductCode,
                WcProductEO::getProductName, (key1, key2) -> key1));
        for (String key : collect.keySet()) {
            System.out.println(collect.get(key));
        }

    }

    /**
     * 结论：list转map之后，通过map遍历出来的对象，更改属性值之后，list里的对象跟着改变
     */
    @Override
    public void testListToMap() {
        List<Goods> goodsList = new ArrayList<>();
        Goods goods1 = new Goods();
        goods1.setCategory("1");
        goods1.setGoodsNo("1");
        goods1.setCategoryNum(1);
        Goods goods2 = new Goods();
        goods2.setCategory("2");
        goods2.setGoodsNo("2");
        goods2.setCategoryNum(2);
        goodsList.add(goods1);
        goodsList.add(goods2);
        System.out.println("goodsList的值：" + goodsList);

        testTrans(goodsList);
        System.out.println("goodsList变更后的值：" + goodsList);

    }

    private void testTrans(List<Goods> goodsList){
        Map<String, Goods> goodsMap = goodsList.stream().
                collect(Collectors.toMap(Goods::getCategory, Function.identity()));
        System.out.println("goodsMap的值：" + goodsMap);

        Goods goods1 = goodsMap.get("1");
        goods1.setGoodsNo("3");
        System.out.println("goodsMap变更后的值：" + goodsMap);
    }

}
