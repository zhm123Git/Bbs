package com.github.pig.bbs.user.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("notify_type" )
 public class NotifyType extends Model<NotifyType> {
    private static final long serialVersionUID=1L;     /**
     * 通知类型主键
     */
    @ApiModelProperty(value = "通知类型主键")
    @TableId("notify_type_id" )
    private Integer notifyTypeId;
    /**
     * 类型(0:回帖,1:回复,2:被收藏,3:被关注)
     */
    @ApiModelProperty(value = "类型(0:回帖,1:回复,2:被收藏,3:被关注)")
    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.notifyTypeId;
    }

}
