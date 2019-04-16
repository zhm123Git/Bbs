package com.github.pig.bbs.posts.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DOM.EnshrinePostsDom;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsPosts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Mapper
public interface BbsPostsMapper extends BaseMapper<BbsPosts> {

     void insertPosts(BbsPosts bbsPosts);

    void postsSatisfaction(
            @Param("postsId") Integer postsId,
            @Param("status") Integer status);

    List<BbsPostsDto> selectPosts(BbsPostsDom bbsPostsDom);

    List getPostsIds(BbsPostsDom bbsPostsDom);

    List selectTopPosts(@Param("userId") Integer userId ,
                        @Param("pageSize") Integer pageSize
    );//置顶的帖子列表

    BbsPostsDto getPosts(BbsPostsDom bbsPostsDom);

    void attentionPosts(BbsPostsDom bbsPostsDom);

    Boolean enshrinePosts(BbsPostsDom bbsPostsDom);

    Integer isAttentionPosts(BbsPostsDom bbsPostsDom);

    Integer isEnshrinePosts(BbsPostsDom bbsPostsDom);

    List selectAttentionPostsIds(BbsPostsDom bbsPostsDom);

    List selectEnshrinePostsIds(BbsPostsDom bbsPostsDom);

    List selectEa(BbsPostsDom bbsPostsDom);

    List getAwardNumIds(BbsPostsDom bbsPostsDom);

    Integer selectAllNum(BbsPostsDom bbsPostsDom);

    Long selectEAndLNum(@Param("id") Integer id);

    Long selectLikeNum(@Param("id") Integer id);

    List<BbsPostsDto> selectPostsInfo(@Param("bp")BbsPostsDom bbsPostsDom);

    BbsPostsDto selectenshrinePost(BbsPostsDom bbsPostsDom);

    Boolean delEnshrinePosts(BbsPostsDom bbsPostsDom);

    List<BbsPostsDto> selectEnshrinePosts(@Param("bep") BbsPostsDom bbsPostsDom);

    void updateLookTimeById(Integer id);


    List<BbsPostsDto> getDraftsPostsByUserId(Integer userId);

    void updatePostById(BbsPosts bbsPosts);

    Long selectEStatus(@Param("ePDom")EnshrinePostsDom ePDom);

    List getPostsIdsByLabelId(BbsPostsDom bbsPostsDom);

    List<BbsPostsDto> selectPostsByLabelId(BbsPostsDom bbsPostsDom);

    Integer selectPostsNum(@Param("bp") BbsPostsDom bbsPostsDom);

    Integer selectEnshrinePostsNum(@Param("bep") BbsPostsDom bbsPostsDom);

    List<String> selectPostsByParma(@Param("list")BbsPostsDom bbsPostsDom);

    Integer selectPostsById(@Param("userId") Integer userId);

    Boolean updateUserRewardPoint(@Param("pd") BbsPostsDto dto);

    Integer selectpostsrewardPoint(@Param("postsId") Integer postsId);

    void updateAccept(@Param("id") Integer id);
}
