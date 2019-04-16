package com.github.pig.bbs.posts.DTO;

import com.github.pig.bbs.posts.entity.BbsReply;
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
 * @since 2018-11-30
 */
@Data
@ApiModel
public class BbsReplyDto extends BbsReply {

    @ApiModelProperty(value = "当前页")
    private Integer pageNum=1; //当前页

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize=10;

    @ApiModelProperty(value = "前台数据")
    private List<BbsReplyDto> data;

    @ApiModelProperty(value = "总条数")
    private Integer allNum;

    @ApiModelProperty(value = "总页数")
    private Integer allPage=0;

    @ApiModelProperty(value = "用户名字")
    private String  userName;//

    @ApiModelProperty(value = "用户头像")
    private String userUrl;



}
