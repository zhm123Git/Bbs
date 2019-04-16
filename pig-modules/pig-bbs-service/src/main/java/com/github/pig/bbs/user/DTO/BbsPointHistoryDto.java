package com.github.pig.bbs.user.DTO;

import com.github.pig.bbs.user.entity.BbsPointHistory;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * Created by Administrator on 2018/12/5 0005.
 */
@Data
public class BbsPointHistoryDto extends BbsPointHistory {
    private Integer pageSize;

    private Integer pageNum;
    private Integer limit;
    private Integer offset;
    private String nickName;
    private String openId;
    private String avatarUrl;
    private String province;
    private String city;
    private String country;
    private Integer consumTimes;
    private Integer totalPay;//总积分支出
    private  Integer totalIncome;//总积分收入
    private  String date; //日期
    private  String time; //时间
    private  String img; //礼物图片地址

}
