package com.github.pig.bbs.posts.service.impl;

import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.entity.BbsGift;
import com.github.pig.bbs.posts.mapper.BbsGiftMapper;
import com.github.pig.bbs.posts.service.BbsGiftService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Service
public class BbsGiftServiceImpl extends ServiceImpl<BbsGiftMapper, BbsGift> implements BbsGiftService {

    @Autowired
    private BbsGiftMapper bbsGiftMapper;

    @Override
    public List selectGift() {
        return bbsGiftMapper.selectGift();
    }
}
