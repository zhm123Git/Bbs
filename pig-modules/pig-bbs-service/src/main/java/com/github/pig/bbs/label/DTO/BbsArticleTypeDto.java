package com.github.pig.bbs.label.DTO;

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
public class BbsArticleTypeDto extends BbsArticleType{

    @ApiModelProperty(value = "用户发帖分类数")
    private Integer ArticleTypeNum;

}
