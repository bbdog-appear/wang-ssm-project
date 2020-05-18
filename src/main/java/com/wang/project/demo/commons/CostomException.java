package com.wang.project.demo.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description TODO
 * <p>
 * 1、TODO
 * <p>
 * User:wangcheng Date:2020/5/18 11:21 ProjectName:CostomException Version:1.0
 **/
@Getter
@Setter
@ToString
public class CostomException extends RuntimeException {

    private static final long serialVersionUID = 3046103605596301271L;

    private String msg;

    public CostomException(){

    }

    public CostomException(String msg){
        this.msg = msg;
    }
}
