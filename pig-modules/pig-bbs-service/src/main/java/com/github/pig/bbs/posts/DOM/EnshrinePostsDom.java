package com.github.pig.bbs.posts.DOM;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EnshrinePostsDom {
    @ApiModelProperty(value = "帖子Id")
    private Integer postsId;
    @ApiModelProperty(value = "用户Id")
    private Integer userId;
}
