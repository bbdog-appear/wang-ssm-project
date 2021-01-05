package com.wang.project.demo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 *      用户产品VO
 * </p>
 *
 * @author wangcheng
 * @version Id：UserProductVO.java Date：2020/10/12 17:20 Version：1.0
 */
@Getter
@Setter
@ToString
public class UserProductVO {

    private String traceLogId;

    private Long id;
    private String productCode;
    private String productName;
    private Long productNum;
    private Date insertTime;
    private Date updateTime;
    private String code;
    private String name;

}
