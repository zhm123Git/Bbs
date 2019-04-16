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
@TableName("bbs_gift" )
 public class BbsGift extends Model<BbsGift> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 礼物介绍
     */
    @ApiModelProperty(value = "礼物介绍")
    @TableField("gift_desc" )
    private String giftDesc;
    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer score;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String img;
    @TableField("del_flag" )
    private String delFlag;


   @Override
   protected Serializable pkVal() {
      return null;
   }
}
