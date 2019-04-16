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
@TableName("bbs_top_posts" )
 public class BbsTopPosts extends Model<BbsTopPosts> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 关联帖子id
     */
    @ApiModelProperty(value = "关联帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 置顶人权限
     */
    @ApiModelProperty(value = "置顶人权限")
    @TableField("top_auth" )
    private Integer topAuth;
    /**
     * 置顶人id（创建人id）
     */
    @ApiModelProperty(value = "置顶人id（创建人id）")
    @TableField("create_Id" )
    private Integer createId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date" )
    private Date createDate;
    @TableField("del_flag" )
    private String delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
