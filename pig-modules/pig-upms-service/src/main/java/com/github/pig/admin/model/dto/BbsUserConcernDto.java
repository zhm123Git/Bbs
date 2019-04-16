package com.github.pig.admin.model.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by yyn on 2018/12/18 0018.
 */
@Data
public class BbsUserConcernDto {

    /**
    * 关注人
    */
    private  Integer concernId;
    /**
     * 被关注人
     */
    private  Integer concernedId;
    /**
     * 被关注人
     */
    private  String[] concernedIds;
    /**
     * 关注状态
     */
    private Integer concernStatus;
    private List<String> concernedIdss;
}
