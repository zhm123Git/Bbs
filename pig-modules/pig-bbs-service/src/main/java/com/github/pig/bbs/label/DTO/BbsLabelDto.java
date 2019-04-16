package com.github.pig.bbs.label.DTO;

import com.github.pig.bbs.label.entity.BbsLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-11-24
 */
@Data
@ApiModel
public class BbsLabelDto extends BbsLabel{

    @ApiModelProperty(value = "当前标签关注人数")
    private  Integer attentionUsers;

    @ApiModelProperty(value = "当前登录人对当前标签的关注状态")
    private  Integer arttentionStatus;

    @ApiModelProperty(value = "当前标签的兄弟标签集")
    private  List<BbsLabelDto> brotherLabels;

    @ApiModelProperty(value = "用户发帖标签数")
    private Integer userLabelNum;



}
