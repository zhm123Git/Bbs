package com.github.pig.bbs.label.DOM;

import com.github.pig.bbs.label.entity.BbsArticleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-12-04
 */
@Data
@ApiModel
public class BbsArticleTypeDom extends BbsArticleType {

//    private Integer userId  = Constant.USER_ID; //此id不被使用,用到的根据需求更改!
    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
}
