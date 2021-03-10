package com.wang.project.demo.dto.ocr;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *      页数DTO
 * </p>
 *
 * @author cheng.wang
 * @version Id：ParagDTO.java Date：2021/3/10 11:15 Version：1.0
 */
@Data
@ToString
public class ParagDTO implements Serializable {

    private static final long serialVersionUID = -4861463697695341103L;

    private Integer paragNo;

}
