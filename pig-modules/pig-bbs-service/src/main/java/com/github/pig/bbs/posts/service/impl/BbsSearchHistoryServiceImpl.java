package com.github.pig.bbs.posts.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.posts.DOM.BbsSearchHistoryDom;
import com.github.pig.bbs.posts.DTO.BbsSearchHistoryDto;
import com.github.pig.bbs.posts.entity.BbsSearchHistory;
import com.github.pig.bbs.posts.mapper.BbsSearchHistoryMapper;
import com.github.pig.bbs.posts.service.IBbsSearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户搜索历史记录表 服务实现类
 * </p>
 *
 * @author zhm
 * @since 2019-01-28
 */
@Service
public class BbsSearchHistoryServiceImpl extends ServiceImpl<BbsSearchHistoryMapper, BbsSearchHistory> implements IBbsSearchHistoryService {
    @Autowired
    private BbsSearchHistoryMapper bbsSearchHistoryMapper;

    @Override
    public Integer selectSearchHistoryNum(BbsSearchHistoryDom bbsSearchHistoryDom) {
        return bbsSearchHistoryMapper.selectSearchHistoryNum(bbsSearchHistoryDom);
    }

    @Override
    public List<BbsSearchHistoryDto> selectByUserId(Integer userId) {
        return bbsSearchHistoryMapper.selectByUserId(userId);
    }

    @Override
    public List<BbsSearchHistoryDto> selectHotSearchHistory() {
        return bbsSearchHistoryMapper.selectHotSearchHistory();
    }

    @Override
    public Boolean deleteSearchHistory(Integer userId) {
        return bbsSearchHistoryMapper.deleteSearchHistory(userId);
    }
}
