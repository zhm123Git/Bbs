package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.entity.BbsContent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface BbsContentMapper extends BaseMapper<BbsContent> {

    void insertPosts(BbsContent map);

    List getContent(BbsPostsDom bbsPostsDom);

    void deleteContentByPostId(Integer postId);
}
