package com.github.pig.bbs.label.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.bbs.label.DOM.BbsLabelDom;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.entity.BbsLabel;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.common.vo.BbsLabelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-22
 */
@Mapper
public interface BbsLabelMapper extends BaseMapper<BbsLabel> {

    List selectAllLabel();

    List selectLabel(@Param("bbsLabel") BbsLabel bbsLabel, @Param("status") Integer status);

    List openLabel(@Param("id") Integer id);

    List selectTypeLabel(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> selectLabelInfo(@Param("bl") BbsUserLabelDom bbsUserLabelDom);

    BbsLabelDto getLabelById(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getBrotherLabelListByLabelId(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    List<BbsPostsDto> getPostsListByLabelId(BbsPostsDom bbsPostsDom);

    Integer selectAllPostsNumByLabelId(@Param("bbsPostsDom") BbsPostsDom bbsPostsDom);

    Long getattentionNumByLabelId(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    Boolean addConcernByLabelId(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    Boolean delConcernByLabelId(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getHotestLabelList(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    Integer selectConcernStatusByUserIdAndLableId(@Param("bbsLabelDom") BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getLabelListByTypeId(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getLabelListByGroom(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getLabelListByGroomAfterNull(BbsLabelDom bbsLabelDom);

    /**
     * 根据用户Id查询用户分类最多信息和给分类数
     * @param userId
     * @return
     */
    BbsLabelVO selectMaxUserLabel(@Param("userId") Integer userId);
}
