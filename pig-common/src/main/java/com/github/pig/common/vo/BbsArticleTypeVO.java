package com.github.pig.common.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class BbsArticleTypeVO implements Serializable {
    private static final long serialVersionUID=1L;     @TableId(value = "id" , type = IdType.AUTO)

    /**
     * 分类名字
     *
     */
    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "用户发帖分类数")
    private Integer ArticleTypeNum;
}
