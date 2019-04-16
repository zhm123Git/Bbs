package com.github.pig.bbs.label.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.label.DOM.BbsArticleTypeDom;
import com.github.pig.bbs.label.DTO.BbsArticleTypeDto;
import com.github.pig.bbs.label.entity.BbsArticleType;
import com.github.pig.bbs.label.mapper.BbsArticleTypeMapper;
import com.github.pig.bbs.label.service.BbsArticleTypeService;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.common.vo.BbsArticleTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-12-04
 */
@Service
@Transactional
public class BbsArticleTypeServiceImpl extends ServiceImpl<BbsArticleTypeMapper, BbsArticleType> implements BbsArticleTypeService {

    @Autowired
    private  BbsArticleTypeMapper bbsArticleTypeMapper;


    @Override
    public List selectType(BbsArticleTypeDto bbsArticleTypeDto) {
        BbsArticleTypeDom  bbsArticleTypeDom =   BbsUtil.getDomOrDto(bbsArticleTypeDto,BbsArticleTypeDom.class);
        return bbsArticleTypeMapper.selectType(bbsArticleTypeDom);
    }

    @Override
    public void attentionType(BbsArticleTypeDto bbsArticleTypeDto) {
        BbsArticleTypeDom  bbsArticleTypeDom =   BbsUtil.getDomOrDto(bbsArticleTypeDto,BbsArticleTypeDom.class);
        bbsArticleTypeMapper.attentionType(bbsArticleTypeDom);
    }

    @Override
    public List selectAttentionType(BbsArticleTypeDto bbsArticleTypeDto) {
        BbsArticleTypeDom  bbsArticleTypeDom =   BbsUtil.getDomOrDto(bbsArticleTypeDto,BbsArticleTypeDom.class);
        return bbsArticleTypeMapper.selectAttentionType(bbsArticleTypeDom);
    }

    @Override
    public List<BbsArticleTypeDto> getEnshrineType(BbsArticleTypeDom bbsArticleTypeDom) {
        if (bbsArticleTypeDom.getPageSize()==null||bbsArticleTypeDom.getPageSize()<1||bbsArticleTypeDom.getPageSize()>50)
            bbsArticleTypeDom.setPageSize(10);
        if (null == bbsArticleTypeDom.getPageNum() || bbsArticleTypeDom.getPageNum() <= 0){
            bbsArticleTypeDom.setPageNum(0);
        }else {
            bbsArticleTypeDom.setPageNum((bbsArticleTypeDom.getPageNum()-1)*bbsArticleTypeDom.getPageSize());
        }

        return bbsArticleTypeMapper.selectEnshrineType(bbsArticleTypeDom);
    }

    @Override
    public Integer getEnshrineNum(BbsArticleTypeDom bbsArticleTypeDom) {
        return bbsArticleTypeMapper.selectEnshrineNum(bbsArticleTypeDom);
    }

    @Override
    public BbsArticleTypeVO selectMaxUserType(Integer userId) {
        return bbsArticleTypeMapper.selectMaxvUserType(userId);
    }


}
