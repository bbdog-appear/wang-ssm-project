package com.wang.project.demo.service;

import com.wang.project.demo.entity.WcCommonConfigEO;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;

import java.util.List;

public interface TestService {

    void addUser(WcUserEO wcUserEO);

    void testObjectToFun(WcUserEO wcUserEO);

    void testObjectFun();

    void testIfElse();

    void addUserEO(WcUserEO wcUserEO);

    List<WcUserEO> queryByContractNo(String contractNo);

    void addCommonConfig(WcCommonConfigEO wcCommonConfigEO);

}
