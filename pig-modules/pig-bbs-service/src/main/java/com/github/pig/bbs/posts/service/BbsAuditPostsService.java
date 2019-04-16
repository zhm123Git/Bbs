package com.github.pig.bbs.posts.service;

import com.github.pig.bbs.posts.DTO.BbsAuditPostsDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsAuditPosts;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-12-06
 */
public interface BbsAuditPostsService extends IService<BbsAuditPosts> {

    void auditPosts(BbsAuditPostsDto bbsAuditPostsDto);
}
