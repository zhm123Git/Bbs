package com.github.pig.admin.model.dom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Desc:
 * ClassName:UsserStatulDom
 * Authot:ZhangHongMeng
 * Date:2018/12/26 16:46
 * project: 华夏银行(pig)
 */
@Data
@ApiModel
public class UsserStatulDom implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
    * 当前用户id
    */
    @ApiModelProperty(value = "当前用户id")
    private Integer u1;
    /**
     * 被查询用户id
     */
    @ApiModelProperty(value = "被查询用户id")
    private Integer u2;
}
