package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DTO.BbsAuditPostsDto;
import com.github.pig.bbs.posts.entity.BbsAuditPosts;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-12-06
 */
@Mapper
public interface BbsAuditPostsMapper extends BaseMapper<BbsAuditPosts> {

    void updatePosts(BbsAuditPostsDto bbsAuditPostsDto);

    Integer isAudit(@Param("postsId") Integer postsId);

    void updateAudit(BbsAuditPosts bbsAuditPosts);
}
