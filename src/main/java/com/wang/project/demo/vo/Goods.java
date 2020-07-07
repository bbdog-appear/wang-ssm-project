package com.wang.project.demo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description 商品类
 * User:wangcheng Date:2020/7/2 10:10 ProjectName:Goods Version:1.0
 **/
@Getter
@Setter
@ToString
public class Goods implements Serializable {

    private static final long serialVersionUID = -7235791892363888792L;
    /**
     * 商品类型(枚举：饮品drinks，干货dryCargo)
     */
    private String category;
    /**
     * 商品编号
     */
    private String goodsNo;

    /**
     * 商品类型下的商品数量（另加字段，不和商品类关联，只是少写一个VO）
     */
    private int categoryNum;
}
