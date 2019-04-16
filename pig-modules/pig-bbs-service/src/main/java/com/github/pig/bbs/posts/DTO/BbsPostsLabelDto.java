package com.github.pig.bbs.posts.DTO;

import com.github.pig.bbs.posts.entity.BbsPosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Data
@ApiModel
public class BbsPostsLabelDto  extends BbsPosts {

    @ApiModelProperty(value = "用户名字")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userUrl;

    @ApiModelProperty(value = "是否收藏了贴子 ",example = "0:未收藏,1:已收藏")
    private Integer enshrinStatus;
}
