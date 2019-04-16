package com.github.pig.bbs.label.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.DTO.BbsUserLabelDto;
import com.github.pig.bbs.label.entity.BbsUserLabel;

import java.util.List;

/**
 * Desc:
 * ClassName:BbsUserLabelService
 * Authot:ZhangHongMeng
 * Date:2018/12/13 11:18
 * project: (pig)
 */
public interface BbsUserLabelService extends IService<BbsUserLabel>  {

    Long selectCounts(BbsUserLabelDom bbsUserLabelDom);

    List<BbsLabelDto> selcetLabelList(Integer id);

    List<BbsUserLabelDto> getBcyID(BbsUserLabelDom bbsUserLabelDom);
    int getLabelStatus(BbsUserLabelDom bbsUserLabelDom);

    boolean addLabel(BbsUserLabel bbsUserLabel);

    boolean deleteLabel(BbsUserLabel bbsUserLabel);

    Integer selectCountsById(Integer uId);
}
