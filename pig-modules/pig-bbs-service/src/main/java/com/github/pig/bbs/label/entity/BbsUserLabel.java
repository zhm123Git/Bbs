package com.github.pig.bbs.label.entity;

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
 * 用户关注标签的中间表
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel(value = "用户关注标签的中间表")
@TableName("bbs_user_label" )
 public class BbsUserLabel extends Model<BbsUserLabel> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 用户(Id)
     */
    @ApiModelProperty(value = "用户(Id)")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 标签(Id)
     */
    @ApiModelProperty(value = "标签(Id)")
    @TableField("label_id" )
    private Integer labelId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date" )
    private Date createDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
