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
@TableName("bbs_posts_label" )
 public class BbsPostsLabel extends Model<BbsPostsLabel> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 帖子id
     */
    @ApiModelProperty(value = "帖子id")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    @TableField("label_id" )
    private Integer labelId;


    @Override
    protected Serializable pkVal() {
        return this.id;
        }


}
