package com.github.pig.bbs.posts.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel
@TableName("bbs_user_praise" )
 public class BbsUserPraise extends Model<BbsUserPraise> {
    private static final long serialVersionUID=1L;     /**
     * 用户 点赞 踩 帖子 中间表
     */
    @ApiModelProperty(value = "用户 点赞 踩 帖子 中间表")
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 帖子id
     */
    @ApiModelProperty(value = "帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 1赞 2踩 3 取消赞 4取消踩
     */
    @ApiModelProperty(value = "1赞 2踩 3 取消赞 4取消踩")
    private Integer status;
    @TableField("del_flag" )
    private String delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
