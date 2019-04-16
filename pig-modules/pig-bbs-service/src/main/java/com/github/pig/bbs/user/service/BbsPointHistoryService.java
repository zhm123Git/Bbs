package com.github.pig.bbs.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.user.DTO.BbsPointHistoryDto;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.github.pig.common.util.PageParams;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
//@FeignClient(value = "bbsPointHistory")
public interface BbsPointHistoryService extends IService<BbsPointHistory> {

    Pair<List<BbsPointHistoryDto>,Long> findPointHistoryList(BbsPointHistoryDto bbsPointHistoryDto, PageParams pageParams);

    Boolean updateUserAmount(BbsPointHistory bbsPointHistory);

    Boolean updatePoint(Integer point,String msg,Integer userId);

    Pair<List<BbsPointHistoryDto>,Long> findPointRankList(BbsPointHistoryDto bbsPointHistoryDto, PageParams pageParams);

}
