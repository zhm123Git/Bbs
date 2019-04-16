package com.github.pig.bbs.user.entity;

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
@TableName("bbs_notify" )
 public class BbsNotify extends Model<BbsNotify> {
    private static final long serialVersionUID=1L;     /**
     * 通知主键
     */
    @ApiModelProperty(value = "通知主键")
    @TableId(value = "notify_id" , type = IdType.AUTO)
    private Long notifyId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 通知内容
     */
    @ApiModelProperty(value = "通知内容")
    private String content;
    /**
     * 消息状态 0:未读  1:已读  2:未读删除  3:已读删除
     */
    @ApiModelProperty(value = "消息状态 0:未读  1:已读  2:未读删除  3:已读删除")
    private Integer status;
    /**
     * 通知类型ID
     */
    @ApiModelProperty(value = "通知类型ID")
    @TableField("notify_type" )
    private Integer notifyType;
    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_time" )
    private Date sendTime;
    /**
     * 阅读时间
     */
    @ApiModelProperty(value = "阅读时间")
    @TableField("read_time" )
    private Date readTime;


    @Override
    protected Serializable pkVal() {
        return this.notifyId;
    }

}
