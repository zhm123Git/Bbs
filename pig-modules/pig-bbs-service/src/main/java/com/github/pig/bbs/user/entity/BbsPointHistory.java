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
 * @author yuyanan
 * @since 2018-12-04
 * 积分消费记录历史
 */
@Data
@ApiModel
@TableName("bbs_point_history" )
 public class BbsPointHistory extends Model<BbsPointHistory> {
    private static final long serialVersionUID=1L;     /**
     * 积分消费表id
     */
    @ApiModelProperty(value = "积分消费表id")
    @TableId(value = "point_history_id" , type = IdType.AUTO)
    private Long pointHistoryId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 积分消费额
     */
    @ApiModelProperty(value = "积分消费额")
    private Integer amount;
    /**
     * 积分消费备注
     */
    @ApiModelProperty(value = "积分消费备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time" )
    private Date createTime;

    /**
     * 礼物id
     */
    @TableField("gift_id")
    private Integer giftId;


    public Long getPointHistoryId() {
        return pointHistoryId;
    }

    public void setPointHistoryId(Long pointHistoryId) {
        this.pointHistoryId = pointHistoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.pointHistoryId;
    }

}
