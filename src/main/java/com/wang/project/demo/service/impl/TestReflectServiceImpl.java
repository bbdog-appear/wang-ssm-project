package com.wang.project.demo.service.impl;
import java.util.*;

import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.service.TestReflectService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void testReflectAddRedis() {
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

        List<WcUserEO> userList = new ArrayList<>();
        WcUserEO user1 = new WcUserEO();
        user1.setCode("10001001");
        user1.setName("cheng10001001");
        WcUserEO user2 = new WcUserEO();
        user2.setCode("10001002");
        user2.setName("cheng10001002");
        userList.add(user1);
        userList.add(user2);
        wcProductEO.setUserList(userList);
        System.out.println("addRedis的对象值：" + wcProductEO);

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
        System.out.println("反射addRedis的hashMap值：" + map);

        /* 这里需要说明一下：因为redisTemplate操作map的时候，最外面的是wcProductEO对象，已经在配置的时候
        * 加了JDK的序列化了，存入时值大概为：productCode为\xAC\xED\x00\x05t\x00\x0510001，所以WcProductEO
        * 不需要实现Serializable了，如果再实现，那么就会报错转换warn。
        * 但是里面的User对象，没有加上序列化，所以需要在User类里实现Serializable才可以，不然会报错Exception */
        redisTemplate.opsForHash().putAll("cheng007", map);
    }

    @Override
    public void testReflectGetRedis() {
        // 获取WcProductEO的所有属性
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(WcProductEO.class);
        // 获取WcProductEO的所有属性名
        List<Object> redisHashKeys = allFieldsList.stream().map(Field::getName).collect(Collectors.toList());
        // 获取WcProductEO的所有属性值
        List<Object> redisHashValues = redisTemplate.opsForHash().multiGet("cheng007", redisHashKeys);
        WcProductEO wcProductEO = new WcProductEO();
        for (int i = 0; i < allFieldsList.size(); i++) {
            try {
                Field field = allFieldsList.get(i);
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(wcProductEO, redisHashValues.get(i));
                field.setAccessible(accessible);
            } catch (IllegalAccessException e) {
                System.out.println("反射getRedis异常" + e);
            }
        }
        System.out.println("反射getRedis的值：" + wcProductEO);
    }
}
