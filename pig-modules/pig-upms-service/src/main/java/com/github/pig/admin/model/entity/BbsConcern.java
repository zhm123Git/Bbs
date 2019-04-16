package com.github.pig.admin.model.entity;

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
 * 用户互相关注表
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel(value = "用户互相关注表")
@TableName("bbs_concern" )
 public class BbsConcern extends Model<BbsConcern> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    /**
     * 关注人Id
     */
    @ApiModelProperty(value = "关注人Id")
    @TableField("concern_id" )
    private Integer concernId;
    /**
     * 被关注人Id
     */
    @ApiModelProperty(value = "被关注人Id")
    @TableField("concerned_id" )
    private Integer concernedId;
    /**
     * 关注时间
     */
    @ApiModelProperty(value = "关注时间")
    @TableField("concern_time" )
    private Date concernTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
