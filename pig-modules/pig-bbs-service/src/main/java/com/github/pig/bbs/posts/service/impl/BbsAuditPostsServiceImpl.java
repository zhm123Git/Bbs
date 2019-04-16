package com.github.pig.bbs.posts.service.impl;

import com.github.pig.bbs.posts.DTO.BbsAuditPostsDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsAuditPosts;
import com.github.pig.bbs.posts.mapper.BbsAuditPostsMapper;
import com.github.pig.bbs.posts.service.BbsAuditPostsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.util.BbsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-12-06
 */
@Service
public class BbsAuditPostsServiceImpl extends ServiceImpl<BbsAuditPostsMapper, BbsAuditPosts> implements BbsAuditPostsService {

    @Autowired
    private BbsAuditPostsMapper bbsAuditPostsMapper;

    @Override
    public void auditPosts(BbsAuditPostsDto bbsAuditPostsDto) {
        BbsAuditPosts bbsAuditPosts =   BbsUtil.getDomOrDto(bbsAuditPostsDto,BbsAuditPosts.class);
        Integer auId =  bbsAuditPostsMapper.isAudit(bbsAuditPostsDto.getPostsId());
       if(auId==null){
           //保存审核表
           insert(bbsAuditPosts);
       }else{
           //更新审核表
           bbsAuditPostsMapper.updateAudit(bbsAuditPosts);
       }
        //更新posts表
        bbsAuditPostsMapper.updatePosts(bbsAuditPostsDto);

    }
}
