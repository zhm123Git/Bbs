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
@TableName("bbs_content" )
 public class BbsContent extends Model<BbsContent> {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    /**
     * 关联postsId
     */
    @ApiModelProperty(value = "关联postsId")
    @TableField("posts_id" )
    private Integer postsId;
    /**
     * 文件描述
     */
    @ApiModelProperty(value = "文件描述")
    @TableField("file_description" )
    private String fileDescription;
    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    @TableField("file_path" )
    private String filePath;
    @TableField("del_flag" )
    private String delFlag;
    /**
     * 视频时长
     */
    @ApiModelProperty(value = "视频时长")
    @TableField("video_duration" )
    private String videoDuration;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private Integer type;
    /**
     * 图片高度
     */
    @ApiModelProperty(value = "图片高度")
    private String height;
    /**
     * 上传图片宽度
     */
    @ApiModelProperty(value = "上传图片宽度")
    private String width;


   @Override
   protected Serializable pkVal() {
      return null;
   }
}
