package com.wang.project.demo.dto.ocr;

import junit.extensions.TestDecorator;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *      Ocr结果DTO
 * </p>
 *
 * @author cheng.wang
 * @version Id：OcrResultDTO.java Date：2021/3/10 10:29 Version：1.0
 */
@Data
@ToString
public class OcrResultDTO implements Serializable {

    private static final long serialVersionUID = 6616372496811621108L;

    private List<TextDetectionDTO> textDetections;
    private String language;
    private Double angel;
    private Integer pdfPageSize;
    private String requestId;

}
