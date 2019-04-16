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
@TableName("bbs_award" )
 public class BbsAward extends Model<BbsAward> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 帖子id
     */
    @ApiModelProperty(value = "帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 礼物id
     */
    @ApiModelProperty(value = "礼物id")
    @TableField("gift_id" )
    private Integer giftId;
    /**
     * 打赏内容
     */
    @ApiModelProperty(value = "打赏内容")
    private String content;
    /**
     * 创建人id（打赏人id）
     */
    @ApiModelProperty(value = "创建人id（打赏人id）")
    @TableField("create_id" )
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
      return null;
   }
}
