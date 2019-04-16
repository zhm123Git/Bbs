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
import java.util.Date;

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
@TableName("bbs_user_posts" )
 public class BbsUserPosts extends Model<BbsUserPosts> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date" )
    private Date createDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
