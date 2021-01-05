package com.wang.project.demo.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User:wangcheng Date:2020/5/18 11:21 ProjectName:CustomizeException Version:1.0
 **/
@Getter
@Setter
@ToString
public class CustomizeException extends RuntimeException {

    private static final long serialVersionUID = 3046103605596301271L;

    private String msg;

    public CustomizeException(){

    }

    public CustomizeException(String msg){
        this.msg = msg;
    }
}
