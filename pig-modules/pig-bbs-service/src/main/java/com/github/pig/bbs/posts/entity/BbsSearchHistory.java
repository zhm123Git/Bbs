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
 * 用户搜索历史记录表
 * </p>
 *
 * @author zhm
 * @since 2019-01-28
 */
@Data
@ApiModel(value = "用户搜索历史记录表")
@TableName("bbs_search_history" )
 public class BbsSearchHistory extends Model<BbsSearchHistory> {
    private static final long serialVersionUID=1L;     /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    /**
     * 搜索标题
     */
    @ApiModelProperty(value = "搜索标题")
    private String title;
    /**
     * 搜索人id
     */
    @ApiModelProperty(value = "搜索人id")
    @TableField("user_id" )
    private Integer userId;
    /**
     * 搜索时间
     */
    @ApiModelProperty(value = "搜索时间")
    @TableField("search_time" )
    private Date searchTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
