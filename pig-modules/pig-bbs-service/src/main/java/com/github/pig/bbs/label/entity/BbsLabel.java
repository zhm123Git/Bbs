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
 * 
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel
@TableName("bbs_label" )
 public class BbsLabel extends Model<BbsLabel> {
    private static final long serialVersionUID=1L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID")
    private Integer pid;
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("label_desc" )
    private String labelDesc;
    /**
     * 图像地址
     */
    @ApiModelProperty(value = "图像地址")
    private String img;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    @TableField("create_user_id" )
    private Integer createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time" )
    private Date createTime;
    /**
     * 修改人ID
     */
    @ApiModelProperty(value = "修改人ID")
    @TableField("update_user_id" )
    private Integer updateUserId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time" )
    private Date updateTime;
    /**
     * 是否启用  0 启用  1 不启用
     */
    @ApiModelProperty(value = "是否启用  0 启用  1 不启用")
    @TableField("del_flag" )
    private String delFlag;
    /**
     * 等级  0或null为顶级  自动维护
     */
    @ApiModelProperty(value = "等级  0或null为顶级  自动维护")
    private Integer level;
    /**
     * 标签类型  自动维护
     */
    @ApiModelProperty(value = "标签类型  自动维护")
    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
