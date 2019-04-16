package com.github.pig.bbs.posts.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.bbs.posts.DOM.BbsAwardDom;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DOM.EnshrinePostsDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsContent;
import com.github.pig.bbs.posts.entity.BbsPosts;
import com.github.pig.bbs.posts.entity.BbsPostsLabel;
import com.github.pig.bbs.posts.entity.BbsUserPosts;
import com.github.pig.bbs.posts.mapper.*;
import com.github.pig.bbs.posts.service.BbsPostsService;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.bbs.util.Constant;
import com.github.pig.bbs.util.SensitiveTextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
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
public class BbsPostsServiceImpl extends ServiceImpl<BbsPostsMapper, BbsPosts> implements BbsPostsService {

    @Autowired
    private BbsPostsMapper bbsPostsMapper;

    @Autowired
    private BbsContentMapper bbsContentMapper;

    @Autowired
    private BbsPostsLabelMapper bbsPostsLabelMapper;

    @Autowired
    private BbsUserPraiseMapper bbsUserPraiseMapper;

    @Autowired
    private BbsAwardMapper bbsAwardMapper;

    @Autowired
    private BbsUserPostsMapper bbsUserPostsMapper;




    @Override
    public void addPostMethod(BbsPostsDto posts) {
        String  filterStr = SensitiveTextUtil.replaceSensitiveWord(posts.getContent(), '*', SensitiveTextUtil.MinMatchTYpe);
        posts.setContent(filterStr);//过滤后的数据保存到数据库
        if(posts.getContent().length()>100){
            posts.setPostsDesc(posts.getContent().substring(0,100) );//截取前100个字放入描述
        }else{
            posts.setPostsDesc(posts.getContent().substring(0,posts.getContent().length()) );//截取前100个字放入描述
        }
        //将帖子信息保存到Posts表中
        BbsPosts bbsPosts = BbsUtil.getDomOrDto(posts,BbsPosts.class);
        Integer postId=posts.getId();//帖子Id
        Integer isPublic=posts.getIsPublic();//帖子类型
        //查看当前帖子是否有主键id,若有,并且帖子is_public的值为4,则为草稿帖子,把草稿改为正式帖子
        if(postId==null){
            Integer insert = bbsPostsMapper.insert(bbsPosts);
            //           bbsPostsMapper.insertPosts(bbsPosts);
            Integer postsId =  bbsPosts.getId();
            //将内容的List 保存到content表中  通过posts的Id关联
            List<BbsContent> filePathList = posts.getFilePathList();
            if (filePathList!=null) {
                for (BbsContent map : filePathList) {
                    map.setPostsId(postsId);
                    bbsContentMapper.insertPosts(map);
                }
            }
            //将标签的list保存到 posts_label 表中 postsId
            List<Integer> labels = posts.getTagsList();
            if(labels!=null) {
                BbsPostsLabel bbsPostsLabel = new BbsPostsLabel();
                for (Integer label : labels) {
                    bbsPostsLabel.setPostsId(postsId);
                    bbsPostsLabel.setLabelId(label);
                    bbsPostsLabelMapper.insertPosts(bbsPostsLabel);
                }
            }
        }else{

            //将内容的List 保存到content表中  通过posts的Id关联,先删除所有这个帖子下的内容,再增加
            bbsContentMapper.deleteContentByPostId(postId);
            List<BbsContent> filePathList = posts.getFilePathList();
            if (filePathList!=null) {
                for (BbsContent map : filePathList) {
                    map.setPostsId(postId);
                    bbsContentMapper.insertPosts(map);
                }
            }
            //将标签的list保存到 posts_label 表中 postsId,先删除所有这个帖子下的内容,再增加
            bbsPostsLabelMapper.deleteByPostsId(postId);
            List<Integer> labels = posts.getTagsList();
            if(labels!=null) {
                BbsPostsLabel bbsPostsLabel = new BbsPostsLabel();
                for (Integer label : labels) {
                    bbsPostsLabel.setPostsId(postId);
                    bbsPostsLabel.setLabelId(label);
                    bbsPostsLabelMapper.insertPosts(bbsPostsLabel);
                }
            }
            posts.setUpdateDate(new Date());
            updatePostById(posts);
        }
    }//addPostMethod end

    @Override
    public List selectTopPosts() {
        Integer  userid = Constant.USER_ID;
        Integer  pagesize = 5;
        return bbsPostsMapper.selectTopPosts(userid , pagesize);
    }

    @Override
    public void giveAward(BbsAwardDto bbsAwardDto) {
        BbsAwardDom bbsAwardDom =  BbsUtil.getDomOrDto(bbsAwardDto, BbsAwardDom.class);
        bbsAwardMapper.giveAward(bbsAwardDom);
    }

    @Override
    public BbsPostsDto getPosts(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom =  BbsUtil.getDomOrDto(bbsPostsDto, BbsPostsDom.class);
        //记录用户浏览操作
        recordUser(bbsPostsDto);
        BbsPostsDto posts = bbsPostsMapper.getPosts(bbsPostsDom);
        List<BbsContent> list  = bbsContentMapper.getContent(bbsPostsDom);
        if(posts!=null){
            posts.setFilePathList(list);
        }
        return posts;
    }



    @Override
    public void attentionPosts(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom= BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        if(bbsPostsMapper.isAttentionPosts(bbsPostsDom)==null)
            bbsPostsMapper.attentionPosts(bbsPostsDom);
    }//关注操作

    @Override
    public Boolean enshrinePosts(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom= BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        if(bbsPostsMapper.isEnshrinePosts(bbsPostsDom)==null){
            return  bbsPostsMapper.enshrinePosts(bbsPostsDom);
        }else{
            return  bbsPostsMapper.delEnshrinePosts(bbsPostsDom);
        }
    }//收藏操作

    @Override
    public List selectAttentionPosts(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom = BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        List list =  bbsPostsMapper.selectAttentionPostsIds(bbsPostsDom);
        bbsPostsDom.setQueryIds(  String.join(",",list).equals("")?"0":String.join(",",list) );
        return  bbsPostsMapper.selectEa(bbsPostsDom);
    }

    @Override
    public List selectEnshrinePosts(BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom = BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        List list =  bbsPostsMapper.selectEnshrinePostsIds(bbsPostsDom);
        bbsPostsDom.setQueryIds( String.join(",",list).equals("")?"0":String.join(",",list));
        return bbsPostsMapper.selectEa(bbsPostsDom);
    }


    @Override
    public BbsPostsDto postsSatisfaction(Integer postsId, Integer status,Integer userSessionId) {
        BbsPostsDom bbsPostsDom  = new BbsPostsDom();
        bbsPostsDom.setId(postsId);
        bbsPostsDom.setUserSessionId(userSessionId);
        // 1赞 2 踩  3 取消赞 4 取消踩
        //确认不是重复操作
        /*
        * 用户操作 先判断是不是重复操作  如果不是  就执行该操作
        * 在用户操作表中添加记录  更改posts表中对应字段数据
        * */

        Integer preisa = bbsUserPraiseMapper.isPreisa(postsId, userSessionId);
        if(preisa!=null){
            if (preisa==1){//当前状态为赞  可以取消赞
                if(status==3){
                    updatePraise(postsId, status, userSessionId);
                }
            }else
            if(preisa==2){//当前状态为踩  可以取消踩
                if(status==4){
                    updatePraise(postsId, status, userSessionId);
                }
            }else
            if(preisa==3 || preisa==4){//当前状态为取消赞  可以赞 或者踩
                if(status==1 || status==2){
                    updatePraise(postsId, status, userSessionId);
                }
            }

        }else{
            //为null时 说明该用户未对该帖子做任何操作 所以可以进行点赞或者踩
            if(status==1 ||status==2){
                bbsUserPraiseMapper.insertPraise(postsId, userSessionId, status);
                bbsPostsMapper.postsSatisfaction(postsId,status);//改变posts表
            }
        }
        BbsPostsDto posts = bbsPostsMapper.getPosts(bbsPostsDom);
        return posts;
    }//postsSatisfaction End

    @Override //获得相关的帖子
    public List selectCorrlation(BbsPostsDto bbsPostsDto) {
        //获取相关帖子id
        BbsPostsDom bbsPostsDom = BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        List list =  bbsPostsLabelMapper.getPostsIds(bbsPostsDom);
        String ids =  String.join(",",list);
        bbsPostsDom.setQueryIds(ids);
        if(!ids.equals(""))return bbsPostsMapper.selectEa(bbsPostsDom);
        return null;
    }

    @Override
    public Long getEnshrineNum(Integer id) {
        return bbsPostsMapper.selectEAndLNum(id);
    }

    @Override
    public Long getLikeNum(Integer id) {
        return bbsPostsMapper.selectLikeNum(id);
    }

    @Override
    public List<BbsPostsDto> getPostsInfo(BbsPostsDom bbsPostsDom) {
        if (null == bbsPostsDom.getPageSize() || bbsPostsDom.getPageSize()<= 0 || bbsPostsDom.getPageSize()>50)
            bbsPostsDom.setPageSize(10);
        if (null == bbsPostsDom.getPageNum() || bbsPostsDom.getPageNum() <= 0){
            bbsPostsDom.setPageNum(0);
        }else {
            bbsPostsDom.setPageNum((bbsPostsDom.getPageNum()-1)*bbsPostsDom.getPageSize());
        }

        return bbsPostsMapper.selectPostsInfo(bbsPostsDom);
    }

    @Override
    public BbsPostsDto selectenshrinePost(BbsPostsDom bbsPostsDom) {
        return bbsPostsMapper.selectenshrinePost(bbsPostsDom);
    }

    @Override
    public List<BbsPostsDto> getEnshrinePostsList(BbsPostsDom bbsPostsDom) {
        if (null == bbsPostsDom.getPageSize() || bbsPostsDom.getPageSize()<= 0 || bbsPostsDom.getPageSize()>50)
            bbsPostsDom.setPageSize(10);
        if (null == bbsPostsDom.getPageNum() || bbsPostsDom.getPageNum() <= 0){
            bbsPostsDom.setPageNum(0);
        }else {
            bbsPostsDom.setPageNum((bbsPostsDom.getPageNum()-1)*bbsPostsDom.getPageSize());
        }

        return bbsPostsMapper.selectEnshrinePosts(bbsPostsDom);
    }


    @Override
    public List<BbsPostsDto> getDraftsPostsByUserId(Integer userId) {
        return bbsPostsMapper.getDraftsPostsByUserId(userId);
    }

    @Override
    public Long getEnshrineStatus(EnshrinePostsDom ePDom) {
        return bbsPostsMapper.selectEStatus(ePDom);
    }

    @Override
    public Integer getPostsNum(BbsPostsDom bbsPostsDom) {
        return bbsPostsMapper.selectPostsNum(bbsPostsDom);
    }

    @Override
    public Integer getEnshrinePostsNum(BbsPostsDom bbsPostsDom) {
        return bbsPostsMapper.selectEnshrinePostsNum(bbsPostsDom);
    }

    @Override
    public List<String> getPostsByParma(BbsPostsDom bbsPostsDom) {
        return bbsPostsMapper.selectPostsByParma(bbsPostsDom);
    }

    @Override
    public List<String> getPostsByParmas(BbsPostsDom bbsPostsDom) {
//        List<String> list = jedis.lrange("query",0,-1);
//        if (list == null){
//            synchronized (list){
//                if (list == null){
//                    List<String> list1 = bbsPostsMapper.selectPostsByParma(bbsPostsDom);
//                    for (String s: list1){
//                        jedis.lpush("query",s);
//                    }
//                }
//            }
//        }
        List<String> list = new ArrayList<>();
        return list;
    }

    @Override
    public void updatePostById(BbsPostsDto posts) {
        bbsPostsMapper.updatePostById(posts);
    }


    @Override
    public BbsPostsDto selectPosts(@RequestBody BbsPostsDto bbsPostsDto) {
        BbsPostsDom bbsPostsDom = BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);

        if(bbsPostsDom.getQuery()!=null&&!"".equals( bbsPostsDom.getQuery())){
            List list =  bbsPostsMapper.getPostsIds(bbsPostsDom);
            bbsPostsDom.setQueryIds(String.join(",",list));
        }

        if(bbsPostsDom.getSort()==5|| bbsPostsDom.getSort().equals(5)){
            List  list = bbsPostsMapper.getAwardNumIds(bbsPostsDom);
            bbsPostsDom.setAwardNumIds(String.join(",",list));
        }

        //如果需要查询某一个标签下的帖子
        if(bbsPostsDom.getLabelId()!=null){
            List  list = bbsPostsLabelMapper.selectPostsIdByLabelId(bbsPostsDom.getLabelId());
            bbsPostsDom.setQueryIdsTwo(String.join(",",list));
        }

        //置顶帖子添加到最上边来着??

        /**排序方式
         * sort
         * 1 发布时间（createDate）默认  降序
         * 2 热议  （浏览数 look_time）
         * 3 好评  （评论数 或者点赞 ？）
         * 4 高关注度 回帖数排序
         */

        List<BbsPostsDto> list = bbsPostsMapper.selectPosts(bbsPostsDom);

        bbsPostsDto.setData(list);
        bbsPostsDto.setAllNum(bbsPostsMapper.selectAllNum(bbsPostsDom));
        if(bbsPostsDto.getPageSize()!=0&&bbsPostsDto.getAllNum()!=null&&bbsPostsDto.getAllNum()!=0){
            bbsPostsDto.setAllPage((bbsPostsDto.getAllNum()+bbsPostsDto.getPageSize()-1)/bbsPostsDto.getPageSize());
        }
        return bbsPostsDto;
    }

    private void recordUser(BbsPostsDto bbsPostsDto) {
        BbsUserPosts bbsUserPosts = new BbsUserPosts();
        bbsUserPosts.setPostsId(bbsPostsDto.getId());
        bbsUserPosts.setUserId(bbsPostsDto.getUserSessionId());
        bbsUserPosts.setCreateDate(new Date());
        //查询数据库里是否有当前记录
        Integer count= bbsUserPostsMapper.selectByUserIdAndPostsId(bbsPostsDto.getUserSessionId(),bbsPostsDto.getId());
        if(!(count>0)){
            bbsUserPostsMapper.insert(bbsUserPosts);
            bbsPostsMapper.updateLookTimeById(bbsPostsDto.getId());//给浏览量加1
        }
    }

    private void updatePraise(Integer postsId, Integer status, Integer userSessionid) {
        bbsUserPraiseMapper.updatePraise(postsId, userSessionid,status);//改变记录表中的记录
        bbsPostsMapper.postsSatisfaction(postsId,status);//改变posts表
    }
}
