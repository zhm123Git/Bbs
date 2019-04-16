package com.github.pig.bbs.posts.service.impl;


import com.github.pig.bbs.posts.DOM.BbsPostsLabelDom;
import com.github.pig.bbs.posts.DTO.BbsPostsLabelDto;
import com.github.pig.bbs.posts.entity.BbsPostsLabel;
import com.github.pig.bbs.posts.mapper.BbsPostsLabelMapper;
import com.github.pig.bbs.posts.service.BbsPostsLabelService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.util.Constant;
import com.github.pig.common.util.PageResult;
import com.github.pig.common.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Service
public class BbsPostsLabelServiceImpl extends ServiceImpl<BbsPostsLabelMapper, BbsPostsLabel> implements BbsPostsLabelService {

    private final static Logger log= LoggerFactory.getLogger(BbsPostsLabelServiceImpl.class);

    @Autowired
    private BbsPostsLabelMapper bbsPostsLabelMapper;

   @Override
    public R<PageResult<BbsPostsLabelDto>> pageinfo(BbsPostsLabelDom bbsPostsLabelDom) {
        Integer page = bbsPostsLabelDom.getPage();
        Integer pageSize = bbsPostsLabelDom.getPagesize();
        if(bbsPostsLabelDom.getLabelId() == null ){
            log.info("LabelId为空!");
        }
        if(page == null || page <= 0){
            log.info("page页码为空或小于零,设置默认值为1!");
            bbsPostsLabelDom.setPage(Constant.PAGENUM_ONE);
        }
        if(pageSize == null || pageSize <= 0){
            log.info("pageSize为空或小于零,设置默认值为10!");
            bbsPostsLabelDom.setPagesize(Constant.PAGESIZE_TEN);
        }
        R<PageResult<BbsPostsLabelDto>> r = new R<PageResult<BbsPostsLabelDto>>();
        PageResult<BbsPostsLabelDto> pageResult = new PageResult<>();
        List<BbsPostsLabelDto> list = bbsPostsLabelMapper.selectLabelInfo(bbsPostsLabelDom);
        pageResult.setData(list);
        pageResult.setPageNum(bbsPostsLabelDom.getPage());
        pageResult.setPageSize(bbsPostsLabelDom.getPagesize());
        pageResult.setAllNum(list.size());
        pageResult.setAllPage((list.size() + bbsPostsLabelDom.getPagesize() -1) / bbsPostsLabelDom.getPagesize());
        r.setData(pageResult);
        return r;
    }
}
