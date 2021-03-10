package com.wang.project.demo.dto.ocr;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *      多边形DTO
 * </p>
 *
 * @author cheng.wang
 * @version Id：PolygonDTO.java Date：2021/3/10 10:37 Version：1.0
 */
@Data
@ToString
public class PolygonDTO implements Serializable {

    private static final long serialVersionUID = 581470353377729954L;

    private Integer x;
    private Integer y;

}
