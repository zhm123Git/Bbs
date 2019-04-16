package com.github.pig.bbs.posts.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.posts.DOM.BbsReplyDom;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.DTO.BbsReplyDto;
import com.github.pig.bbs.posts.entity.BbsPosts;
import com.github.pig.bbs.posts.entity.BbsReply;
import com.github.pig.bbs.posts.mapper.BbsPostsMapper;
import com.github.pig.bbs.posts.mapper.BbsReplyMapper;
import com.github.pig.bbs.posts.service.BbsReplyService;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.github.pig.bbs.user.mapper.BbsPointHistoryMapper;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.common.util.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Service
public class BbsReplyServiceImpl extends ServiceImpl<BbsReplyMapper, BbsReply> implements BbsReplyService {

    @Autowired
    private  BbsReplyMapper bbsReplyMapper;
    @Autowired
    private BbsPostsMapper bbsPostsMapper;
    @Autowired
    private BbsPointHistoryMapper bbsPointHistoryMapper;



    @Override
    public BbsReplyDto selectReply(BbsReplyDto bbsReplyDto) {
        BbsReplyDom bbsReplyDom = BbsUtil.getDomOrDto(bbsReplyDto, BbsReplyDom.class);

        List<BbsReplyDto> list = bbsReplyMapper.selectReply(bbsReplyDom);

        for ( BbsReplyDto bs:list) {
            BbsReplyDom bbsReplyDom1 = BbsUtil.getDomOrDto(bs, BbsReplyDom.class);
            bs.setData( bbsReplyMapper.selectReplyTwo(bbsReplyDom1));
        }

        //应前台需求  返回一个map  map中添加当前页数  每页多少   总页数  以及总条数
        bbsReplyDto.setAllNum(bbsReplyMapper.selectNum(bbsReplyDom));
        bbsReplyDto.setData(list);

        if(bbsReplyDto.getPageSize()!=0&&bbsReplyDto.getAllNum()!=null&&bbsReplyDto.getAllNum()!=0){
            bbsReplyDto.setAllPage((bbsReplyDto.getAllNum()+bbsReplyDto.getPageSize()-1)/bbsReplyDto.getPageSize());
        }



        return bbsReplyDto;
    }

    @Override
    public void updateReply(BbsReplyDto bbsReplyDto) {
        BbsReplyDom replyDom =  BbsUtil.getDomOrDto(bbsReplyDto,BbsReplyDom.class);
        bbsReplyMapper.updateReply(replyDom);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public ResultVO acceptReply(BbsReplyDto bbsReplyDto)throws Exception {
        ResultVO r = new ResultVO();
        BbsPointHistory bbsPointHistory = new BbsPointHistory();
        BbsReplyDom bbsReplyDom = BbsUtil.getDomOrDto(bbsReplyDto, BbsReplyDom.class);
        //首先查询该帖子是否已经采纳回帖
        BbsReplyDom brd = bbsReplyMapper.isAccept(bbsReplyDom);
        BbsPosts bbsPosts = bbsPostsMapper.selectById(brd.getPostsId());
        if(1==bbsPosts.getAccept() ){
            r.setData("失败");
            r.setCode(1);
            r.setMsg("该帖子已经采纳过评论");
            throw  new RuntimeException("该帖子已经采纳过评论");
        }
        //isAccept  为0时 表示无采纳的回帖
        try {
            if (brd.getAccept() == null || brd.getAccept()==0 ) {
                boolean reply = bbsReplyMapper.acceptReply(bbsReplyDom);
                if (reply == true) {
                    BbsPostsDto bpt = new BbsPostsDto();
                    Integer amount = bbsPostsMapper.selectPostsById(brd.getUserId());
                    Integer rewardPoint = bbsPosts.getRewardPoint();
                    bpt.setRewardPoint(amount + rewardPoint);
                    bpt.setUserId(brd.getUserId());
                    Boolean i = bbsPostsMapper.updateUserRewardPoint(bpt);
                    if (i == true) {
                        //更新帖子的采纳状态为1
                        bbsPostsMapper.updateAccept(bbsPosts.getId());
                        bbsPointHistory.setUserId(brd.getUserId());
                        bbsPointHistory.setAmount(rewardPoint);
                        bbsPointHistory.setRemark("悬赏采纳奖赏" +rewardPoint+ "分");
                        bbsPointHistoryMapper.insert(bbsPointHistory);
                        r.setData("成功");
                        r.setCode(0);
                        r.setMsg("问答采纳成功");
                    } else {
                        bbsReplyMapper.acceptReplys(bbsReplyDom);
                        r.setData("失败");
                        r.setCode(1);
                        r.setMsg("问答采纳失败");
                    }
                }
            }else {
                r.setData("失败");
                r.setCode(1);
                r.setMsg("问答采纳重复");
            }
        }catch (Exception e){
            r.setCode(1);
            r.setMsg(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return r;
    }

    @Override
    public void insertReply(BbsReplyDto bbsReplyDto) {
        bbsReplyDto.setCreateDate(new Date());//当前时间
        insert(bbsReplyDto);
    }

    @Override
    public void deleteByIdAndPid(BbsReplyDto bbsReplyDto) {
        BbsReplyDom bbsReplyDom = BbsUtil.getDomOrDto(bbsReplyDto,BbsReplyDom.class);
        bbsReplyMapper.deleteByIdAndPid(bbsReplyDom);
    }


}
