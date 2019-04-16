package com.github.pig.bbs.label.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.label.DOM.BbsLabelDom;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.entity.BbsLabel;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.common.vo.BbsLabelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-22
 */
public interface BbsLabelService extends IService<BbsLabel> {

    List selectAllLabel();

    List selectLabel(Integer id, Integer status);

    List openLabel(Integer id);

    List selectTypeLabel(BbsLabelDto bbsLabelDto);

    List<BbsLabelDto> getLabelInfo(BbsUserLabelDom bbsUserLabelDom);

    BbsLabelDto getLabelById(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getBrotherLabelListByLabelId(BbsLabelDom bbsLabelDom);

    BbsPostsDto getPostsListByLabelId(BbsPostsDto BbsPostsDto);

    Long getattentionNumByLabelId(BbsLabelDom bbsLabelDom);


    Boolean addConcernByLabelId(BbsLabelDom bbsLabelDom);

    Boolean delConcernByLabelId(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getHotestLabelList(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getLabelListByTypeId(BbsLabelDom bbsLabelDom);

    List<BbsLabelDto> getLabelListByGroom(BbsLabelDom bbsLabelDom);
    BbsLabelVO selectMaxUserLabel(@Param("userId") Integer userId);
}
