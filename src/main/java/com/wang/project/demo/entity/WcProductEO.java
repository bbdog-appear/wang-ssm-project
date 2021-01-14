package com.wang.project.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class WcProductEO {

//    private static final long serialVersionUID = 5203567668957067748L;

    private Long id;
    private String productCode;
    private String productName;
    private Long productNum;
    private Date insertTime;
    private Date updateTime;
    private String shardDate;

    private List<WcUserEO> userList;

}
