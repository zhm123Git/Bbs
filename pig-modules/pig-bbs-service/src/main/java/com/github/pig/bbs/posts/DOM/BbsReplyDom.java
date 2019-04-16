package com.github.pig.bbs.posts.DOM;

import com.github.pig.bbs.posts.entity.BbsReply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class BbsReplyDom extends BbsReply {
    @ApiModelProperty(value = "当前页")
    private Integer pageNum=1;
    @ApiModelProperty(value = "每页多少")
    private Integer pageSize=10;
    @ApiModelProperty(value = "是否启用分页" ,example = " 1启用 2 不启用")
    private Integer ispage=1;
}
