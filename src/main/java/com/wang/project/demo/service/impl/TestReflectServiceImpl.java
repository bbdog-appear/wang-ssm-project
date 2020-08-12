package com.wang.project.demo.service.impl;
import java.util.Date;

import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.service.TestReflectService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      测试反射类
 * </p>
 *
 * @author wangcheng
 * @version Id：TestReflectServiceImpl.java Date：2020/8/12 18:34 Version：1.0
 */
@Component
public class TestReflectServiceImpl implements TestReflectService {

    @Override
    public void testReflect() {
        // 获取当前类的所有属性(public、private等)
//        Field[] fields = wcProductEO.getClass().getDeclaredFields();
        // 获取当前类和父类的所有属性(public)
//        Field[] fields = wcProductEO.getClass().getFields();

        // Commons-lang3包中可以获取当前类和父类所有的属性(public、private)
        // 下面两个一样的效果
//        Field[] allFields = FieldUtils.getAllFields(WcProductEO.class);
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(WcProductEO.class);

        WcProductEO wcProductEO = new WcProductEO();
        wcProductEO.setId(10L);
        wcProductEO.setProductCode("10001");
        wcProductEO.setInsertTime(new Date());

        Map<String, Object> map = new HashMap<>();

        for (Field allField : allFieldsList) {
            try {
                boolean accessible = allField.isAccessible();
                allField.setAccessible(true);
                map.put(allField.getName(), allField.get(wcProductEO));
                allField.setAccessible(accessible);
            } catch (IllegalAccessException e) {
                System.out.println("反射异常");
            }
        }
        System.out.println(map);

    }

}
