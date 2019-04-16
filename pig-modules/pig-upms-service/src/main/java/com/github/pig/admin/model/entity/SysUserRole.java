package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel(value = "用户角色表")
@TableName("sys_user_role" )
 public class SysUserRole extends Model<SysUserRole> {
    private static final long serialVersionUID=1L;     /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @TableId("user_id" )
    private Integer userId;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @TableField("role_id" )
    private Integer roleId;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
