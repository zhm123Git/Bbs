package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel(value = "用户表")
@TableName("sys_user" )
 public class SysUser extends Model<SysUser> {
    private static final long serialVersionUID=1L;     /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "user_id" , type = IdType.AUTO)
    private Integer userId;
    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid")
    @TableField("open_id" )
    private String openId;
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    @TableField("nick_name" )
    private String nickName;
    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    @TableField("avatar_url" )
    private String avatarUrl;
    /**
     * 性别 0:未知,1:男,2:女
     */
    @ApiModelProperty(value = "性别 0:未知,1:男,2:女")
    private Integer gender;
    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;
    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String country;
    /**
     * 显示 country，province，city 所用的语言 en:英文,zh_CN:简体中文,zh:TW:繁体中文
     */
    @ApiModelProperty(value = "显示 country，province，city 所用的语言 en:英文,zh_CN:简体中文,zh:TW:繁体中文")
    private String language;
    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名")
    @TableField("person_signature_name" )
    private String personSignatureName;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    private String password;
    /**
     * 随机盐
     */
    @ApiModelProperty(value = "随机盐")
    private String salt;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String phone;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @TableField("dept_id" )
    private Integer deptId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time" )
    private Date createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time" )
    private Date updateTime;
    /**
     * 0-正常，1-删除
     */
    @ApiModelProperty(value = "0-正常，1-删除")
    @TableField("del_flag" )
    private String delFlag;
    /**
     * 0:允许查看个人主页,1:不允许查看个人主页
     */
    @ApiModelProperty(value = "0:允许查看个人主页,1:不允许查看个人主页")
    @TableField("allow_flag" )
    private String allowFlag;
    /**
     * 0:可以使用,1:禁止使用
     */
    @ApiModelProperty(value = "0:可以使用,1:禁止使用")
    @TableField("forbide_flag" )
    private String forbideFlag;
    /**
     * 是否是行内人员: 0 是 ,1否
     */
    @ApiModelProperty(value = "是否是行内人员: 0 是 ,1否")
    private Integer insider;
    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer amount;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private Date birthday;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
