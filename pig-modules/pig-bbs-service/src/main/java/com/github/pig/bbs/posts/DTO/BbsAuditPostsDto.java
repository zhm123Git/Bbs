package com.github.pig.bbs.posts.DTO;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.github.pig.bbs.posts.entity.BbsAuditPosts;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-12-06
 */
@Data
@ApiModel
public class BbsAuditPostsDto extends BbsAuditPosts{

}
