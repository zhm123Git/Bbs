package com.github.pig.admin.model.dto;

import com.github.pig.admin.model.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yyn on 2018/11/30.
 */
@Data
@ApiModel
public class SysUserDto extends SysUser{

    private Integer times;
    private double weight;
    @ApiModelProperty(value = "关注用户状态",example = "1:已关注, 0:未关注")
    private Integer concernStatus;
    private Integer consume;

    private String token;
    private boolean isme;
    @ApiModelProperty(value = "用户发帖最多类型名")
    private String maxUserType;
    @ApiModelProperty(value = "用户发帖最多类型数量")
    private Integer maxUserTypeNum;
    @ApiModelProperty(value = "用户发帖最多标签名")
    private String maxUserLabel;
    @ApiModelProperty(value = "用户发帖最多标签数量")
    private Integer maxUserLabelNum;



}
