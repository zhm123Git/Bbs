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
@TableName("bbs_audit_posts" )
 public class BbsAuditPosts extends Model<BbsAuditPosts> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 帖子id
     */
    @ApiModelProperty(value = "帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 审核内容保存
     */
    @ApiModelProperty(value = "审核内容保存")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date" )
    private Date createDate;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField("create_id" )
    private Integer createId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_date" )
    private Date updateDate;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    @TableField("update_id" )
    private Integer updateId;
    /**
     * 审核字段 同posts表中相同 共同维护
     */
    @ApiModelProperty(value = "审核字段 同posts表中相同 共同维护")
    @TableField("audit_posts" )
    private Integer auditPosts;


   @Override
   protected Serializable pkVal() {
      return null;
   }
}
