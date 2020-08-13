package com.wang.project.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/6 10:58 ProjectName:WcProductEO Version:1.0
 **/
@Getter
@Setter
@ToString
public class WcProductEO implements Serializable {

    private static final long serialVersionUID = 5203567668957067748L;

    private Long id;
    private String productCode;
    private String productName;
    private Long productNum;
    private Date insertTime;
    private Date updateTime;
    private List<User> userList;

}
