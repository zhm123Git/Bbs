package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DOM.BbsReplyDom;
import com.github.pig.bbs.posts.DTO.BbsReplyDto;
import com.github.pig.bbs.posts.entity.BbsReply;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Mapper
public interface BbsReplyMapper extends BaseMapper<BbsReply> {

    List selectReply(BbsReplyDom bbsReplyDom);

    void updateReply(BbsReplyDom replyDom);

    BbsReplyDom  isAccept(@Param("brd")BbsReplyDom bbsReplyDom);

    boolean acceptReply(@Param("brd") BbsReplyDom bbsReplyDom);

    Integer selectNum(BbsReplyDom bbsReplyDom);

    List<BbsReplyDto> selectReplyTwo(BbsReplyDom bbsReplyDom1);

    void deleteByIdAndPid(@Param("bbsReplyDom") BbsReplyDom bbsReplyDom);

    void acceptReplys(BbsReplyDom bbsReplyDom);
}
