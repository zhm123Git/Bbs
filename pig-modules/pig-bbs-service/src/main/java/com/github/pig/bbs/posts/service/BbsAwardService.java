package com.github.pig.bbs.posts.service;

import com.github.pig.bbs.posts.DOM.BbsAwardDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.entity.BbsAward;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
public interface BbsAwardService extends IService<BbsAward> {

    Boolean awardUserWithAward(BbsAwardDom bbsAwardDom);
}
