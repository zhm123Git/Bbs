package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.entity.BbsGift;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface BbsGiftMapper extends BaseMapper<BbsGift> {

    List selectGift();
}
