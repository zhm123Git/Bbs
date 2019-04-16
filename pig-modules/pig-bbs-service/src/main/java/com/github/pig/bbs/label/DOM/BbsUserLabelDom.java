package com.github.pig.bbs.label.DOM;

import com.github.pig.bbs.label.entity.BbsUserLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Desc:
 * ClassName:BbsUserLabelDom
 * Authot:ZhangHongMeng
 * Date:2018/12/13 15:07
 * project: (pig)
 */
@Data
@ApiModel
public class BbsUserLabelDom extends BbsUserLabel{

    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数" )
    private Integer pageSize;

    @ApiModelProperty(value = "关注标签状态",example = "0:未关注,1:已关注")
    private Integer ConcernLabelStatus;

}
