package com.github.pig.admin.model.dom;

import com.github.pig.admin.model.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yyn on 2018/12/13 0013.
 */
@Data
@ApiModel
public class UserDom extends SysUser {
    @ApiModelProperty(value = "当前页")
    private Integer currentPage=1;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize=10;
    private Integer times;
}
