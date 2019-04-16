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
@TableName("bbs_reply" )
 public class BbsReply extends Model<BbsReply> {
    private static final long serialVersionUID=1L;     /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 父id为null时是一级评论  父id可以是一级评论id
     */
    @ApiModelProperty(value = "父id为null时是一级评论  父id可以是一级评论id")
    private Integer pid;
    /**
     * 被评论人id
     */
    @ApiModelProperty(value = "被评论人id")
    private Integer bid;
    /**
     * 被评论人的名字
     */
    @ApiModelProperty(value = "被评论人的名字")
    private String bname;
    /**
     * 帖子id
     */
    @ApiModelProperty(value = "帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 评论人id
     */
    @ApiModelProperty(value = "评论人id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    @TableField("create_date" )
    private Date createDate;
    /**
     * 是否采纳  0为默认  1为采纳  pid为null时才能采纳
     */
    @ApiModelProperty(value = "是否采纳  0为默认  1为采纳  pid为null时才能采纳")
    private Integer accept;
    /**
     * 启用标注
     */
    @ApiModelProperty(value = "启用标注")
    @TableField("del_flag" )
    private String delFlag;
    /**
     * 评论类型(1:评论,2:回复)
     */
    @ApiModelProperty(value = "评论类型(1:评论,2:回复)")
    private String type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
