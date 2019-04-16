package com.github.pig.bbs.label.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.label.DOM.BbsArticleTypeDom;
import com.github.pig.bbs.label.DTO.BbsArticleTypeDto;
import com.github.pig.bbs.label.entity.BbsArticleType;
import com.github.pig.common.vo.BbsArticleTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-12-04
 */
public interface BbsArticleTypeService extends IService<BbsArticleType> {

    List selectType(BbsArticleTypeDto bbsArticleTypeDto);

    void attentionType(BbsArticleTypeDto bbsArticleTypeDto);

    List selectAttentionType(BbsArticleTypeDto bbsArticleTypeDto);

    List<BbsArticleTypeDto> getEnshrineType(BbsArticleTypeDom bbsArticleTypeDom);

    Integer getEnshrineNum(BbsArticleTypeDom bbsArticleTypeDom);

    BbsArticleTypeVO selectMaxUserType(@Param("userId") Integer userId);
}
