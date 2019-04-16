package com.github.pig.bbs.user.DTO;

import com.github.pig.bbs.user.entity.BbsNotify;
import lombok.Data;

/**
 * Created by yyn on 2018/12/4 0004.
 */
@Data
public class BbsNotifyDto extends BbsNotify {
    private Integer limit;
    private Integer offset;
}
