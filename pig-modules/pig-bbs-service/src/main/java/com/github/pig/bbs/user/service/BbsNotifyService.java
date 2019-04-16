package com.github.pig.bbs.user.service;

import com.github.pig.bbs.user.DTO.BbsNotifyDto;
import com.github.pig.bbs.user.entity.BbsNotify;
import com.baomidou.mybatisplus.service.IService;
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
public interface BbsNotifyService extends IService<BbsNotify> {

    Pair<List<BbsNotify>,Long> findNotifyList(BbsNotifyDto bbsNotifyDto, PageParams pageParams);
}
