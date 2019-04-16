package com.github.pig.admin.model.dom;

import com.github.pig.admin.model.entity.SysDept;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by Administrator on 2018/12/7 0007.
 */
@Data
@ApiModel
public class DeptDom extends SysDept{
    private Integer limit;
    private Integer offset;
}
