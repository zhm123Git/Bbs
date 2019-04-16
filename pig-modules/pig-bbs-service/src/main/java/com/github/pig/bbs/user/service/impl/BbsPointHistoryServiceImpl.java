package com.github.pig.bbs.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.user.DTO.BbsPointHistoryDto;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.github.pig.bbs.user.mapper.BbsPointHistoryMapper;
import com.github.pig.bbs.user.service.BbsPointHistoryService;
import com.github.pig.common.util.BeanCopierUtils;
import com.github.pig.common.util.PageParams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
@Service
public class BbsPointHistoryServiceImpl extends ServiceImpl<BbsPointHistoryMapper, BbsPointHistory> implements BbsPointHistoryService {

    @Autowired
    private BbsPointHistoryMapper bbsPointHistoryMapper;
    @Override
    public Pair<List<BbsPointHistoryDto>, Long> findPointHistoryList(BbsPointHistoryDto bbsPointHistoryDto, PageParams pageParams) {
        BbsPointHistory bbsPointHistory = new BbsPointHistory();
        BeanCopierUtils.copy(bbsPointHistoryDto,bbsPointHistory);
        List<BbsPointHistoryDto> bbsPointHistoryList =  bbsPointHistoryMapper.findPointHistoryList(bbsPointHistory,pageParams);
        Long total  =  bbsPointHistoryMapper.selectBbsPointHistoryCount(bbsPointHistory);
        return ImmutablePair.of(bbsPointHistoryList, total);
    }

    @Override
    public Boolean updateUserAmount(BbsPointHistory bbsPointHistory) {
        try{
            Integer userYuanAmount = bbsPointHistoryMapper.selectUserAmount(bbsPointHistory);
            int finalAmount=userYuanAmount+bbsPointHistory.getAmount();
            if(finalAmount<0){
                throw new  RuntimeException("没有足够积分");
            }
            insert(bbsPointHistory);
            bbsPointHistory.setAmount(finalAmount);
            bbsPointHistoryMapper.updateUserAmount(bbsPointHistory);
        }catch (Exception e){
            e.getMessage();
        }
        return  true;
    }

    public Boolean updatePoint( Integer point,String msg,Integer userId) {
        BbsPointHistory bbsPointHistory = new BbsPointHistory();
        bbsPointHistory.setRemark(msg);
        bbsPointHistory.setAmount(point);
        bbsPointHistory.setUserId(userId);
        Integer userYuanAmount = bbsPointHistoryMapper.selectUserAmount(bbsPointHistory);
        int finalAmount=userYuanAmount+point;
        if(finalAmount<=0){
            throw new  RuntimeException("没有足够积分");
        }
        insert(bbsPointHistory);
        bbsPointHistory.setAmount(finalAmount);
        Boolean res = bbsPointHistoryMapper.updateUserAmount(bbsPointHistory);
        return  res;
    }

    @Override
    public Pair<List<BbsPointHistoryDto>, Long> findPointRankList(BbsPointHistoryDto bbsPointHistoryDto, PageParams pageParams) {
        List<BbsPointHistoryDto> bbsPointHistoryDtoList =  bbsPointHistoryMapper.findPointRankList(bbsPointHistoryDto,pageParams);
        Long count  =  bbsPointHistoryMapper.selectBbsPointRankCount(bbsPointHistoryDto);
        return ImmutablePair.of(bbsPointHistoryDtoList, count);

    }


}
