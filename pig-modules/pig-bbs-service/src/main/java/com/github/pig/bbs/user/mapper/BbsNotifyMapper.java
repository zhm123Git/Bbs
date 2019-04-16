package com.github.pig.bbs.user.mapper;

import com.github.pig.bbs.user.entity.BbsNotify;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.common.util.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
@Mapper
public interface BbsNotifyMapper extends BaseMapper<BbsNotify> {

    List<BbsNotify> findNotifyList(@Param("bbsNotify") BbsNotify bbsNotify, @Param("pageParams") PageParams pageParams);

    Long selectBbsNotifyCount(@Param("bbsNotify") BbsNotify bbsNotify);
}
