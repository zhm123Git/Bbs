package com.github.pig.bbs.posts.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pig.bbs.posts.DTO.BbsReplyDto;
import com.github.pig.bbs.posts.entity.BbsReply;
import com.github.pig.common.util.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
public interface BbsReplyService extends IService<BbsReply> {

    BbsReplyDto selectReply(BbsReplyDto bbsReplyDto);

    void updateReply(BbsReplyDto bbsReplyDto);

    ResultVO acceptReply(BbsReplyDto bbsReplyDto)throws Exception;

    void insertReply(BbsReplyDto bbsReplyDto);


    void deleteByIdAndPid(BbsReplyDto bbsReplyDto);
}
