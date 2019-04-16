package com.github.pig.bbs.posts.DOM;

import com.github.pig.bbs.posts.entity.BbsPostsLabel;
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
public class BbsPostsLabelDom extends BbsPostsLabel{

    @ApiModelProperty(value = "当前页")
    private int pagesize;

    @ApiModelProperty(value = "每页条数")
    private int page;
}
