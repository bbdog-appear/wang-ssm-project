package com.wang.project.demo.dto.ocr;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *      图片文字内容DTO
 * </p>
 *
 * @author cheng.wang
 * @version Id：TextDetection.java Date：2021/3/10 10:33 Version：1.0
 */
@Data
@ToString
public class TextDetectionDTO implements Serializable {

    private static final long serialVersionUID = -413428796719856758L;

    private String detectedText;
    private Integer confidence;
    private List<PolygonDTO> polygon;
    private String advancedInfo;
    private ItemPolygonDTO itemPolygon;

}
