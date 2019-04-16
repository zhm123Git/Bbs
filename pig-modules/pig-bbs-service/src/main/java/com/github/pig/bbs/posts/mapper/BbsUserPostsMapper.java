package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DOM.BbsUserPostsDom;
import com.github.pig.bbs.posts.DTO.BbsPostsLabelDto;
import com.github.pig.bbs.posts.entity.BbsUserPosts;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.common.util.R;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Mapper
public interface BbsUserPostsMapper extends BaseMapper<BbsUserPosts> {

    R<BbsPostsLabelDto> selectPostsInfo(@Param("up") BbsUserPostsDom bbsUserPostsDom);


    Integer selectByUserIdAndPostsId(@Param("userId") Integer userId, @Param("postsId")Integer postsId);
}
