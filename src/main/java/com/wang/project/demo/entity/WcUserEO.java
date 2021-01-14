package com.wang.project.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class WcUserEO implements Serializable {

    private static final long serialVersionUID = -5730383041598417485L;

    private Long id;
    private String code;
    private String name;
    private Date insertTime;
    private String contractNo;

}
