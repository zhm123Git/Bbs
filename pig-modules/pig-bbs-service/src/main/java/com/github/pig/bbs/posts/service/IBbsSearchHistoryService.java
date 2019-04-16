package com.github.pig.bbs.posts.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.posts.DOM.BbsSearchHistoryDom;
import com.github.pig.bbs.posts.DTO.BbsSearchHistoryDto;
import com.github.pig.bbs.posts.entity.BbsSearchHistory;

import java.util.List;

/**
 * <p>
 * 用户搜索历史记录表 服务类
 * </p>
 *
 * @author zhm
 * @since 2019-01-28
 */
public interface IBbsSearchHistoryService extends IService<BbsSearchHistory> {

    Integer selectSearchHistoryNum(BbsSearchHistoryDom bbsSearchHistoryDom);

    List<BbsSearchHistoryDto> selectByUserId(Integer userId);

    List<BbsSearchHistoryDto> selectHotSearchHistory();

    Boolean deleteSearchHistory(Integer userId);
}
