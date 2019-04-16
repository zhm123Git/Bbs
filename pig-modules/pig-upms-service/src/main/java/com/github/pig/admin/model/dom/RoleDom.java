package com.github.pig.admin.model.dom;

import com.github.pig.admin.model.entity.SysRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by yyn on 2018/12/7 0007.
 */
@Data
@ApiModel
public class RoleDom  extends SysRole{
    private Integer limit;
    private Integer offset;
}
