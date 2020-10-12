package com.wang.project.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * User:wangcheng Date:2020/4/26 9:49 ProjectName:User Version:1.0
 **/
@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = -5730383041598417485L;

    private Long id;
    private String code;
    private String name;
    private Date insertTime;
}
