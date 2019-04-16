package com.github.pig.bbs.posts.DTO;


import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.posts.entity.BbsContent;
import com.github.pig.bbs.posts.entity.BbsPosts;
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
public class BbsPostsDto extends BbsPosts {

    private List<BbsContent> filePathList;

    private List<Integer> tagsList;

    private List<BbsLabelDto> bbsLabels ;

    private Integer sort  ;

    @ApiModelProperty(value = "用户浏览过该帖子几次")
    private Integer userLookTime;

    @ApiModelProperty(value = "该帖子的回复数量")
    private Integer replyCount;

    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页多少")
    private Integer pageSize;

    private String query;//查询条件
    private  Integer status;

    @ApiModelProperty(value = "分类名字")
    private String articleTypeName;

    @ApiModelProperty(value = "分类梳理")
    private String awardNum;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userUrl;

    @ApiModelProperty(value = "用户的点赞状态")
    private Integer praiseStatus=0;

    private List<BbsPostsDto> data;

    @ApiModelProperty(value = "总条数")
    private Integer allNum;

    @ApiModelProperty(value = "总页数")
    private Integer allPage=0;

    @ApiModelProperty(value = "收藏当前帖子人数")
    private Integer collectCount;

    @ApiModelProperty(value = "是否收藏了当前帖子",example = "0:未收藏 ,1:已收藏")
    private Integer collectStatus;

    @ApiModelProperty(value = "标签详情页使用属性")
    private Integer labelId;

    private Integer userSessionId ; //当前用户登录的id  先不写

    @ApiModelProperty(value = "是否关注当前创建帖子人")
    private Integer ifConcer;

    @ApiModelProperty(value = "当前帖子有多少人收藏")
    private Integer enshriNum;

    @ApiModelProperty(value = "帖子收藏状态" ,name = "enshriStatue",example = "0:未收藏 ,1:已收藏")
    private Integer enshriStatue;

    @ApiModelProperty(value = "该帖子共被多少个人阅读过")
    private Integer postReadTime;

}
