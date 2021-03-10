package com.wang.project.demo.dto.ocr;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *      AdvancedInfoDTO
 * </p>
 *
 * @author cheng.wang
 * @version Id：AdvancedInfoDTO.java Date：2021/3/10 11:13 Version：1.0
 */
@Data
@ToString
public class AdvancedInfoDTO implements Serializable {

    private static final long serialVersionUID = -3944204576795364860L;

    private ParagDTO parag;

}
