package com.github.pig.bbs.posts.DOM;

import com.github.pig.bbs.posts.entity.BbsPosts;
import com.github.pig.bbs.util.Constant;
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
public class BbsPostsDom extends BbsPosts {

    private Integer userSessionId ; //当前用户登录的id  先不写
    private Integer userId = Constant.USER_ID ;  //不一定用哪个
    private Integer sort;
    private Integer userLookTime;
    private Integer replyCount;
    private List<String> querys;
    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页多少")
    private Integer pageSize;

    private String query;//查询条件

    private String queryIds;
    private String queryIdsTwo;
    private String replyIds;

    private String awardNumIds;
    @ApiModelProperty(value = "标签详情页使用属性")
    private Integer labelId;

    @ApiModelProperty(value = "是否关注当前创建帖子人")
    private Integer ifConcer;

    @ApiModelProperty(value = "当前帖子有多少人收藏")
    private Integer enshriNum;

    @ApiModelProperty(value = "当前帖子被当前登录人是否收藏")
    private Integer enshriStatue;

    @ApiModelProperty(value = "多少个人阅读过")
    private Integer postReadTime;

}
