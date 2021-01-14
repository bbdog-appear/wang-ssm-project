package com.wang.project.demo.dto;

import com.wang.project.demo.vo.Goods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDTO {

    /**
     * 每种类型下卖出的商品数量
     */
    private List<Goods> goodsList;

}
