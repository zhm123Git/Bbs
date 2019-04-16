package com.github.pig.bbs.user.service.impl;

import com.github.pig.bbs.user.DTO.BbsNotifyDto;
import com.github.pig.bbs.user.entity.BbsNotify;
import com.github.pig.bbs.user.mapper.BbsNotifyMapper;
import com.github.pig.bbs.user.service.BbsNotifyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
public class BbsNotifyServiceImpl extends ServiceImpl<BbsNotifyMapper, BbsNotify> implements BbsNotifyService {

    @Autowired
    private BbsNotifyMapper bbsNotifyMapper;

    @Override
    public Pair<List<BbsNotify>, Long> findNotifyList(BbsNotifyDto bbsNotifyDto, PageParams pageParams) {
        BbsNotify bbsNotify = new BbsNotify();
        BeanCopierUtils.copy(bbsNotifyDto,bbsNotify);
        List<BbsNotify> bbsNotifyList =  bbsNotifyMapper.findNotifyList(bbsNotify,pageParams);
        Long count  =  bbsNotifyMapper.selectBbsNotifyCount(bbsNotify);
        return ImmutablePair.of(bbsNotifyList, count);
    }
}
