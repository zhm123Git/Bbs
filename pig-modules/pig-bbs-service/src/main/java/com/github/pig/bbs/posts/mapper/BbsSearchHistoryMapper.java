package com.github.pig.bbs.posts.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.bbs.posts.DOM.BbsSearchHistoryDom;
import com.github.pig.bbs.posts.DTO.BbsSearchHistoryDto;
import com.github.pig.bbs.posts.entity.BbsSearchHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户搜索历史记录表 Mapper 接口
 * </p>
 *
 * @author zhm
 * @since 2019-01-28
 */
@Mapper
public interface BbsSearchHistoryMapper extends BaseMapper<BbsSearchHistory> {

    Integer selectSearchHistoryNum(@Param("bbsh") BbsSearchHistoryDom bbsSearchHistoryDom);

    List<BbsSearchHistoryDto> selectByUserId(@Param("userId") Integer userId);

    List<BbsSearchHistoryDto> selectHotSearchHistory();

    Boolean deleteSearchHistory(@Param("userId") Integer userId);
}
