package com.wang.project.demo.dto.ocr;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *      项目多边形
 * </p>
 *
 * @author cheng.wang
 * @version Id：ItemPolygonDTO.java Date：2021/3/10 11:20 Version：1.0
 */
@Data
@ToString
public class ItemPolygonDTO implements Serializable {

    private static final long serialVersionUID = 2206909356571736510L;

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

}
