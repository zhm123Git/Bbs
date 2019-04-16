package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DOM.BbsPostsLabelDom;
import com.github.pig.bbs.posts.DTO.BbsPostsLabelDto;
import com.github.pig.bbs.posts.entity.BbsPostsLabel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Mapper
public interface BbsPostsLabelMapper extends BaseMapper<BbsPostsLabel> {

    void insertPosts(@Param("pl")BbsPostsLabel bbsPostsLabel);

    List<BbsPostsLabelDto> selectLabelInfo(@Param("bpl") BbsPostsLabelDom bbsPostsLabelDom);

    List getPostsIds(BbsPostsDom bbsPostsDom);

    List<String> selectPostsIdByLabelId(@Param("labelId") Integer labelId);

    void deleteByPostsId(Integer postId);
}
