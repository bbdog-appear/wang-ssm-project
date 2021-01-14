package com.wang.project.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class WcCommonConfigEO implements Serializable {

    private static final long serialVersionUID = -5730383041598417485L;

    private Long id;
    private String commonType;
    private String commonKey;
    private String commonValue;

}
