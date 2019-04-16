/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.admin.model.dto;

import com.github.pig.admin.model.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@Data
@ApiModel
public class UserDTO extends SysUser {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private List<Integer> role;
    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newpassword1;
    @ApiModelProperty(value = "关注状态")
    private Integer concernStatus;
    @ApiModelProperty(value = "用户发帖最多类型名")
    private String maxUserType;
    @ApiModelProperty(value = "用户发帖最多类型数量")
    private Integer maxUserTypeNum;
    @ApiModelProperty(value = "用户发帖最多标签名")
    private String maxUserLabel;
    @ApiModelProperty(value = "用户发帖最多标签数量")
    private Integer maxUserLabelNum;
    private Integer consume;
    private String token;
}
