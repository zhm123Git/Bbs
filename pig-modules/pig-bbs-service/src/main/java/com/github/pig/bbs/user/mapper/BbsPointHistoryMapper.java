package com.github.pig.bbs.user.mapper;

import com.github.pig.bbs.user.DTO.BbsPointHistoryDto;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.common.util.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
@Mapper
public interface BbsPointHistoryMapper extends BaseMapper<BbsPointHistory> {

    List<BbsPointHistoryDto> findPointHistoryList(@Param("bbsPointHistory") BbsPointHistory bbsPointHistory,@Param("pageParams")  PageParams pageParams);

    Long selectBbsPointHistoryCount(@Param("bbsPointHistory") BbsPointHistory bbsPointHistory);

    Boolean updateUserAmount(@Param("bph")BbsPointHistory bbsPointHistory);

    Integer selectUserAmount(BbsPointHistory bbsPointHistory);

    List<BbsPointHistoryDto> findPointRankList(@Param("bbsPointHistoryDto") BbsPointHistoryDto bbsPointHistoryDto,@Param("pageParams") PageParams pageParams);

    Long selectBbsPointRankCount(@Param("bbsPointHistoryDto") BbsPointHistoryDto bbsPointHistoryDto);

    String getNickName(Integer createId);
}
