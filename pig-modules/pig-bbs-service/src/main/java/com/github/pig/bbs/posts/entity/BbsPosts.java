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
@TableName("bbs_posts" )
 public class BbsPosts extends Model<BbsPosts> {
    private static final long serialVersionUID=1L;     /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID" , type = IdType.AUTO)
    private Integer id;
    /**
     * 关联user表 用户ID
     */
    @ApiModelProperty(value = "关联user表 用户ID")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 类型 与bbs_article_type关联
     */
    @ApiModelProperty(value = "类型 与bbs_article_type关联")
    @TableField("article_type" )
    private Integer articleType;
    /**
     * 帖子标题名称
     */
    @ApiModelProperty(value = "帖子标题名称")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 描述 取内容的前100字
     */
    @ApiModelProperty(value = "描述 取内容的前100字")
    @TableField("posts_desc" )
    private String postsDesc;
    /**
     * 首图地址
     */
    @ApiModelProperty(value = "首图地址")
    @TableField("img_url" )
    private String imgURL;
    /**
     * 首图高度
     */
    @ApiModelProperty(value = "首图高度")
    @TableField("img_url_height" )
    private String imgURLHeight;
    /**
     * 首图宽度
     */
    @ApiModelProperty(value = "首图宽度")
    @TableField("img_url_width" )
    private String imgURLWidth;
    /**
     * 奖励点
     */
    @ApiModelProperty(value = "奖励点")
    @TableField("reward_point" )
    private Integer rewardPoint;
    /**
     * 踩 次数
     */
    @ApiModelProperty(value = "踩 次数")
    @TableField("trample_time" )
    private Integer trampleTime;
    /**
     * 赞 次数
     */
    @ApiModelProperty(value = "赞 次数")
    @TableField("praise_time" )
    private Integer praiseTime;
    /**
     * 浏览数
     */
    @ApiModelProperty(value = "浏览数")
    @TableField("look_time" )
    private Integer lookTime;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    @TableField("create_user_id" )
    private Integer createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date" )
    private Date createDate;
    /**
     * 修改人Id
     */
    @ApiModelProperty(value = "修改人Id")
    @TableField("update_user_id" )
    private Integer updateUserId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_date" )
    private Date updateDate;
    /**
     * 审核字段 1待审核 2通过  3不通过  4草稿箱
     */
    @ApiModelProperty(value = "审核字段 1待审核 2通过  3不通过  4草稿箱")
    @TableField("is_public" )
    private Integer isPublic;
    @TableField("del_flag" )
    private String delFlag;

   /**
    * 是否采纳  0为默认  1为采纳  pid为null时才能采纳
    */
   @ApiModelProperty(value = "是否采纳  0为默认  1为采纳 ")
   private Integer accept;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
