package com.github.pig.bbs.posts.service.impl;

import com.github.pig.bbs.posts.DOM.BbsAwardDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.entity.BbsAward;
import com.github.pig.bbs.posts.entity.BbsGift;
import com.github.pig.bbs.posts.mapper.BbsAwardMapper;
import com.github.pig.bbs.posts.mapper.BbsGiftMapper;
import com.github.pig.bbs.posts.service.BbsAwardService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.github.pig.bbs.user.mapper.BbsPointHistoryMapper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class BbsAwardServiceImpl extends ServiceImpl<BbsAwardMapper, BbsAward> implements BbsAwardService {

    @Autowired
    private BbsAwardMapper bbsAwardMapper;

    @Autowired
    private BbsGiftMapper bbsGiftMapper;

    @Autowired
    private BbsPointHistoryMapper bbsPointHistoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean awardUserWithAward(BbsAwardDom bbsAwardDom) {
        BbsGift bbsGift = bbsGiftMapper.selectById(bbsAwardDom.getGiftId());//查询礼物等同积分
        String payName=bbsPointHistoryMapper.getNickName(bbsAwardDom.getCreateId());
        String incomeName=bbsPointHistoryMapper.getNickName(bbsAwardDom.getCreatePostUserId());
            //减积分
            BbsPointHistory bbsPointHistorydel = new BbsPointHistory();
            BbsPointHistory bbsPointHistoryAdd = new BbsPointHistory();
            bbsPointHistorydel.setUserId(bbsAwardDom.getCreateId());
            Integer userYuanAmount = bbsPointHistoryMapper.selectUserAmount(bbsPointHistorydel);
            int finalAmountDel=userYuanAmount-bbsGift.getScore();

            if(finalAmountDel<0){
                throw new  RuntimeException("没有足够积分");
            }
            bbsPointHistorydel.setAmount(-bbsGift.getScore());
            if(bbsGift.getGiftDesc().equals("玫瑰")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一朵");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一朵");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一朵玫瑰");
            }else if(bbsGift.getGiftDesc().equals("钻石")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一颗");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一颗");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一颗钻石");
            }else if(bbsGift.getGiftDesc().equals("玩偶")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一个");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一个");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一个玩偶");
            }else if(bbsGift.getGiftDesc().equals("彩蛋")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一颗");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一颗");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一颗彩蛋");
            }else if(bbsGift.getGiftDesc().equals("皇冠")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一顶");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一顶");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一顶皇冠");
            }else if(bbsGift.getGiftDesc().equals("跑车")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一辆");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一辆");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一辆跑车");
            }else if(bbsGift.getGiftDesc().equals("火箭")){
                bbsPointHistorydel.setRemark("阅读"+incomeName+"的帖子打赏一支");
                bbsPointHistoryAdd.setRemark(payName+"阅读我的帖子打赏一支");
                bbsAwardDom.setContent(payName+"阅读"+incomeName+"的帖子打赏一支火箭");
            }
            bbsPointHistorydel.setGiftId(bbsGift.getId());
            bbsPointHistorydel.setCreateTime(new Date());
            bbsPointHistoryMapper.insert(bbsPointHistorydel);
            bbsPointHistorydel.setAmount(finalAmountDel);
            bbsPointHistoryMapper.updateUserAmount(bbsPointHistorydel);
            //加积分

            bbsPointHistoryAdd.setUserId(bbsAwardDom.getCreatePostUserId());
            Integer userYuanAmountAdd = bbsPointHistoryMapper.selectUserAmount(bbsPointHistoryAdd);
            int finalAmountAdd=userYuanAmountAdd+bbsGift.getScore();
            bbsPointHistoryAdd.setAmount(bbsGift.getScore());
            bbsPointHistoryAdd.setGiftId(bbsGift.getId());
            bbsPointHistoryAdd.setCreateTime(new Date());
            bbsPointHistoryMapper.insert(bbsPointHistoryAdd);
            bbsPointHistoryAdd.setAmount(finalAmountAdd);
            bbsPointHistoryMapper.updateUserAmount(bbsPointHistoryAdd);

        return bbsAwardMapper.giveAward(bbsAwardDom);//打赏记录;
    }
}
