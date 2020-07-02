package com.wang.project.demo.dto;

import com.wang.project.demo.vo.Goods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName CategoryDTO
 * @Description TODO
 * @Author c_wangcheng-007
 * @Date 2020/7/2/002 23:30
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class CategoryDTO {

    /**
     * 每种类型下卖出的商品数量
     */
    private List<Goods> goodsList;

}
