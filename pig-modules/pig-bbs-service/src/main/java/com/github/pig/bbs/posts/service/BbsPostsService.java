package com.github.pig.bbs.posts.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DOM.EnshrinePostsDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsPosts;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
public interface BbsPostsService extends IService<BbsPosts> {


    BbsPostsDto postsSatisfaction(Integer postsId, Integer status,Integer userSessionId);

    BbsPostsDto selectPosts(BbsPostsDto bbsPostsDto);

    void addPostMethod(BbsPostsDto posts);

    List selectTopPosts();

    void giveAward(BbsAwardDto bbsAwardDto);

    BbsPostsDto getPosts(BbsPostsDto bbsPostsDto);

    void attentionPosts(BbsPostsDto bbsPostsDto);

    Boolean enshrinePosts(BbsPostsDto bbsPostsDto);

    List selectAttentionPosts(BbsPostsDto bbsPostsDto);

    List selectEnshrinePosts(BbsPostsDto bbsPostsDto);

    List selectCorrlation(BbsPostsDto bbsPostsDto);

    Long getEnshrineNum(Integer id);

    Long getLikeNum(Integer id);

    List<BbsPostsDto> getPostsInfo(BbsPostsDom bbsPostsDom);

    BbsPostsDto selectenshrinePost(BbsPostsDom bbsPostsDom);

    List<BbsPostsDto> getEnshrinePostsList(BbsPostsDom bbsPostsDom);


    List<BbsPostsDto> getDraftsPostsByUserId(Integer userId);

    List<String> getPostsByParmas(BbsPostsDom bbsPostsDom);

    void updatePostById(BbsPostsDto posts);


    Long getEnshrineStatus(EnshrinePostsDom ePDom);

    Integer getPostsNum(BbsPostsDom bbsPostsDom);

    Integer getEnshrinePostsNum(BbsPostsDom bbsPostsDom);

    List<String> getPostsByParma(BbsPostsDom bbsPostsDom);
}
