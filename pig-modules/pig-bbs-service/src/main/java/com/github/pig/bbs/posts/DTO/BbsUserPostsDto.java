package com.github.pig.bbs.posts.DTO;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.github.pig.bbs.posts.entity.BbsUserPosts;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Data
@ApiModel
public class BbsUserPostsDto extends BbsUserPosts{

}
