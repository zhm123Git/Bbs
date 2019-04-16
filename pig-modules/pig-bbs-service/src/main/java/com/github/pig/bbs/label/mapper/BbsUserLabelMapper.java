package com.github.pig.bbs.label.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.DTO.BbsUserLabelDto;
import com.github.pig.bbs.label.entity.BbsUserLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Desc:
 * ClassName:BbsUserLabelMapper
 * Authot:ZhangHongMeng
 * Date:2018/12/13 11:24
 * project: (pig)
 */
@Mapper
public interface BbsUserLabelMapper extends BaseMapper<BbsUserLabel> {

    Long selectCounts(@Param("bul") BbsUserLabelDom bbsUserLabelDom);

    List<BbsLabelDto> selcetLabelById(@Param("id") Integer id);

    List<BbsUserLabelDto> selectbyID(@Param("id") Integer id);

    int selectLabelStatus(@Param("bul") BbsUserLabelDom bbsUserLabelDom);

    boolean insertLabel(@Param("bul") BbsUserLabel bbsUserLabel);

    boolean delLabel(@Param("bul") BbsUserLabel bbsUserLabel);

    Integer selectCountsById(@Param("uId") Integer uId);
}

