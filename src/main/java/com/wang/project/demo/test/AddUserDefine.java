package com.wang.project.demo.test;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/6/2 10:16 ProjectName:AddUserDefine Version:1.0
 **/
@Getter
@Setter
public class AddUserDefine {
    private String code;
    private String name;
    private String age;
    private String classRoom;
    private List<ModifyUserDefine> list;
}
