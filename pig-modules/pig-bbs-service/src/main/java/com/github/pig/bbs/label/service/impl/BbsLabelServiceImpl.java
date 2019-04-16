package com.github.pig.bbs.label.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.label.DOM.BbsLabelDom;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.entity.BbsLabel;
import com.github.pig.bbs.label.mapper.BbsLabelMapper;
import com.github.pig.bbs.label.service.BbsLabelService;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.mapper.BbsPostsMapper;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.common.vo.BbsLabelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-11-22
 */
@Service
public class BbsLabelServiceImpl extends ServiceImpl<BbsLabelMapper, BbsLabel> implements BbsLabelService {

    @Autowired
    private  BbsLabelMapper bbsLabelMapper;
    @Autowired
    private BbsPostsMapper bbsPostsMapper;


    @Override
    public List selectAllLabel() {

        return bbsLabelMapper.selectAllLabel();
    }

    @Override
    public List selectLabel(Integer id, Integer status) {

        BbsLabel bbsLabel = bbsLabelMapper.selectById(id);
        List list = new ArrayList();

        switch (status){//
            case 1://查询所有上级 pid = id
                for (int i=bbsLabel.getLevel(); i>1; i--){
                    List<BbsLabel> list1 = bbsLabelMapper.selectLabel(bbsLabel,status);
                    bbsLabel = list1.get(0);
                    list.addAll(list1);
                }
                return list;

            case 2://查询所有下级 id = pid
                //查询下级 调用mysql函数 具体方法 在mysql数据库中的 getLabelTree 方法中
                list.addAll(bbsLabelMapper.selectLabel(bbsLabel,status));
                return list;

            case 3://查询 所有平级
                return bbsLabelMapper.selectLabel(bbsLabel,status);

            default:
                return null;
        }

    }// end

    @Override
    public List openLabel(Integer id) {
        return bbsLabelMapper.openLabel(id);
    }

    @Override
    public List selectTypeLabel(BbsLabelDto bbsLabelDto) {
        BbsLabelDom bbsLabelDom=  BbsUtil.getDomOrDto(bbsLabelDto,BbsLabelDom.class);

        return bbsLabelMapper.selectTypeLabel(bbsLabelDom);
    }

    @Override
    public List<BbsLabelDto> getLabelInfo(BbsUserLabelDom bbsUserLabelDom) {
        if (null == bbsUserLabelDom.getPageSize() || bbsUserLabelDom.getPageSize()<= 0 || bbsUserLabelDom.getPageSize()>50)
            bbsUserLabelDom.setPageSize(10);
        if (null == bbsUserLabelDom.getPageNum() || bbsUserLabelDom.getPageNum() <= 0){
            bbsUserLabelDom.setPageNum(0);
        }else {
            bbsUserLabelDom.setPageNum((bbsUserLabelDom.getPageNum()-1)*bbsUserLabelDom.getPageSize());
        }

        return bbsLabelMapper.selectLabelInfo(bbsUserLabelDom);
    }

    @Override
    public BbsLabelDto getLabelById(BbsLabelDom bbsLabelDom) {
        return bbsLabelMapper.getLabelById(bbsLabelDom);
    }

    @Override
    public List<BbsLabelDto> getBrotherLabelListByLabelId(BbsLabelDom bbsLabelDom) {
        return bbsLabelMapper.getBrotherLabelListByLabelId(bbsLabelDom);
    }

    @Override
    public BbsPostsDto getPostsListByLabelId(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom = BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);


        /**排序方式
         * sort
         * 1 发布时间（createDate）默认  降序
         * 2 热议  （浏览数 look_time）
         * 3 好评  （评论数 或者点赞 ？）
         * 4 高关注度 回帖数排序
         */

        List<BbsPostsDto> list = bbsLabelMapper.getPostsListByLabelId(bbsPostsDom);
        bbsPostsDto.setData(list);
        bbsPostsDto.setAllNum(bbsLabelMapper.selectAllPostsNumByLabelId(bbsPostsDom));
        if(bbsPostsDto.getPageSize()!=0&&bbsPostsDto.getAllNum()!=null&&bbsPostsDto.getAllNum()!=0){
            bbsPostsDto.setAllPage((bbsPostsDto.getAllNum()+bbsPostsDto.getPageSize()-1)/bbsPostsDto.getPageSize());
        }
        return bbsPostsDto;
    }

    @Override
    public Long getattentionNumByLabelId(BbsLabelDom bbsLabelDom) {
        return  bbsLabelMapper.getattentionNumByLabelId(bbsLabelDom);
    }

    @Override
    public Boolean addConcernByLabelId(BbsLabelDom bbsLabelDom) {
        //先判断当前用户是否已经关注此标签,如果关注了,则不再关注
       Integer count= bbsLabelMapper.selectConcernStatusByUserIdAndLableId(bbsLabelDom);
        if(count==0){
            return bbsLabelMapper.addConcernByLabelId(bbsLabelDom);
        }else{
            return bbsLabelMapper.delConcernByLabelId(bbsLabelDom);
        }

    }

    @Override
    public Boolean delConcernByLabelId(BbsLabelDom bbsLabelDom) {
        return bbsLabelMapper.delConcernByLabelId(bbsLabelDom);
    }

    @Override
    public List<BbsLabelDto> getHotestLabelList(BbsLabelDom bbsLabelDom) {
        return bbsLabelMapper.getHotestLabelList(bbsLabelDom);
    }

    @Override
    public List<BbsLabelDto> getLabelListByTypeId(BbsLabelDom bbsLabelDom) {
        return bbsLabelMapper.getLabelListByTypeId(bbsLabelDom);
    }

    @Override
    public List<BbsLabelDto> getLabelListByGroom(BbsLabelDom bbsLabelDom) {
        List<BbsLabelDto> bbsLabelDtoList=bbsLabelMapper.getLabelListByGroom(bbsLabelDom);
        if(bbsLabelDtoList==null || bbsLabelDtoList.size()==0){
            bbsLabelDtoList=bbsLabelMapper.getLabelListByGroomAfterNull(bbsLabelDom);
        }
        return bbsLabelDtoList;
    }

    @Override
    public BbsLabelVO selectMaxUserLabel(Integer userId) {
        return bbsLabelMapper.selectMaxUserLabel(userId);
    }

}
