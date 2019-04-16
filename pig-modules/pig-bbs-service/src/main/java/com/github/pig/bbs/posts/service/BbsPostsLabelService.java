package com.github.pig.bbs.posts.service;

import com.github.pig.bbs.posts.DOM.BbsPostsLabelDom;
import com.github.pig.bbs.posts.DTO.BbsPostsLabelDto;
import com.github.pig.bbs.posts.entity.BbsPostsLabel;
import com.baomidou.mybatisplus.service.IService;
import com.github.pig.common.util.PageResult;
import com.github.pig.common.util.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
public interface BbsPostsLabelService extends IService<BbsPostsLabel> {
    /**
    * @Description :查询标签的帖子详情
    * @Param [page, pageSize, labelId]
    * @return R<PageResult<BbsPostsLabelDto>>
    * @Author : zhanghongmeng
    * @Date 15:01 2018/12/14
    **/

    R<PageResult<BbsPostsLabelDto>> pageinfo(BbsPostsLabelDom bbsPostsLabelDom);
}

