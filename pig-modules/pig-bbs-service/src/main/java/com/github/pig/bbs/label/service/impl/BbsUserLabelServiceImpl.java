package com.github.pig.bbs.label.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.DTO.BbsUserLabelDto;
import com.github.pig.bbs.label.entity.BbsUserLabel;
import com.github.pig.bbs.label.mapper.BbsUserLabelMapper;
import com.github.pig.bbs.label.service.BbsUserLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * ClassName:bbsUserLabelServiceImpl
 * Authot:ZhangHongMeng
 * Date:2018/12/13 11:19
 * project: (pig)
 */
@Service
public class BbsUserLabelServiceImpl extends ServiceImpl<BbsUserLabelMapper,BbsUserLabel> implements BbsUserLabelService {



    @Autowired
    private BbsUserLabelMapper bbsUserLabelMapper;


    @Override
    public Long selectCounts(BbsUserLabelDom bbsUserLabelDom) {
        return bbsUserLabelMapper.selectCounts(bbsUserLabelDom);
    }

    @Override
    public List<BbsLabelDto> selcetLabelList(Integer id) {
        return bbsUserLabelMapper.selcetLabelById(id);
    }

    @Override
    public List<BbsUserLabelDto> getBcyID(BbsUserLabelDom bbsUserLabelDom) {
        Integer id = bbsUserLabelDom.getUserId();
        return bbsUserLabelMapper.selectbyID(id);
    }

    @Override
    public int getLabelStatus(BbsUserLabelDom bbsUserLabelDom) {
        return bbsUserLabelMapper.selectLabelStatus(bbsUserLabelDom);
    }

    @Override
    public boolean addLabel(BbsUserLabel bbsUserLabel) {
        return bbsUserLabelMapper.insertLabel(bbsUserLabel);
    }

    @Override
    public boolean deleteLabel(BbsUserLabel bbsUserLabel) {
        return bbsUserLabelMapper.delLabel(bbsUserLabel);
    }

    @Override
    public Integer selectCountsById(Integer uId) {
        return bbsUserLabelMapper.selectCountsById(uId);
    }


}
