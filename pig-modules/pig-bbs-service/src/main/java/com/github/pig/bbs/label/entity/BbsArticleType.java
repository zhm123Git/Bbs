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

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author zhm
 * @since 2019-01-16
 */
@Data
@ApiModel(value = "分类表")
@TableName("bbs_article_type" )
 public class BbsArticleType extends Model<BbsArticleType> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Integer pid;
    /**
     * 名字
     *
     */
    @ApiModelProperty(value = "名字")
    private String name;
    /**
     * 图标地址
     */
    @ApiModelProperty(value = "图标地址")
    @TableField("img_url" )
    private String imgUrl;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("article_desc" )
    private String articleDesc;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
