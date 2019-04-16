package com.github.pig.bbs.label.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.bbs.label.DOM.BbsArticleTypeDom;
import com.github.pig.bbs.label.DTO.BbsArticleTypeDto;
import com.github.pig.bbs.label.entity.BbsArticleType;
import com.github.pig.common.vo.BbsArticleTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-12-04
 */
@Mapper
public interface BbsArticleTypeMapper extends BaseMapper<BbsArticleType>,Serializable {

    Integer selectEnshrineNum(@Param("bat") BbsArticleTypeDom bbsArticleTypeDom) ;

    List selectType(BbsArticleTypeDom bbsArticleTypeDom);

    void attentionType(BbsArticleTypeDom bbsArticleTypeDom);

    List selectAttentionType(BbsArticleTypeDom bbsArticleTypeDom);

    List<BbsArticleTypeDto> selectEnshrineType(@Param("bat") BbsArticleTypeDom bbsArticleTypeDom);

    /**
     * 根据用户Id查询用户分类最多信息和给分类数
     * @param userId
     * @return
     */
    BbsArticleTypeVO selectMaxvUserType(@Param("userId") Integer userId);
}
