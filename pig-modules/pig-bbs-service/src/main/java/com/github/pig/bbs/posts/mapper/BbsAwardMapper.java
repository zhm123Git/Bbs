package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DOM.BbsAwardDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.entity.BbsAward;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Mapper
public interface BbsAwardMapper extends BaseMapper<BbsAward> {

    Boolean giveAward(BbsAwardDom bbsAwardDom);
}
