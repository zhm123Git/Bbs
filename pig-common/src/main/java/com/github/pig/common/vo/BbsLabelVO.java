package com.github.pig.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class BbsLabelVO implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String name;
    @ApiModelProperty(value = "用户发帖标签数")
    private Integer userLabelNum;

}
