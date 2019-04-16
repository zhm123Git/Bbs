package com.github.pig.bbs.posts.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.posts.DOM.BbsPostsDom;
import com.github.pig.bbs.posts.DOM.EnshrinePostsDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.bbs.posts.entity.BbsPosts;
import com.github.pig.bbs.posts.service.BbsPostsService;
import com.github.pig.bbs.user.service.BbsPointHistoryService;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.bbs.util.Constant;
import com.github.pig.bbs.util.StringUtil;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.*;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Api(value = "/bbsPosts",description="帖子")
@RestController
@RequestMapping("/bbsPosts")
public class BbsPostsController extends BaseController {
    @Autowired private BbsPostsService bbsPostsService;

    @Autowired
    private BbsPointHistoryService bbsPointHistoryService;





    @PostMapping("/addPosts")
    @ApiOperation(value = "帖子发布接口")
    public R addPostsMethod(@RequestBody BbsPostsDto posts,HttpServletRequest request){
        Integer userId=UserUtils.getUserId(request);
  //      Integer userId= 1;
        if(userId==null){
            throw new RuntimeException("用户未登录");
        }
        if(posts.getIsPublic()==1 && posts.getArticleType()==null){
            throw new RuntimeException("请给帖子选择一个分类");
        }
        if (posts.getArticleType()!=null && posts.getArticleType()==8) {
            //给发帖人减积分
            Boolean res = bbsPointHistoryService.updatePoint(0-(posts.getRewardPoint()), "悬赏扣除" + posts.getRewardPoint() + "分", userId);
        }
        //发帖加10积分,草稿帖子不加分
        if(posts.getArticleType()!=null && posts.getArticleType()!=8){
            bbsPointHistoryService.updatePoint(Constant.addPostsPoint,Constant.addPostsMsg,userId);
        }
        posts.setUserId(userId);
        posts.setCreateUserId(userId);
        posts.setCreateDate(new Date());
        bbsPostsService.addPostMethod(posts);
        return new R<>() ;
    }//addPostsMethod end

    /**
     *
     * @param
     * @return
     * postsId
     * status  1赞 2 踩  3 取消赞 4 取消踩
     *   */
    @ApiOperation(value = "根据帖子id 对帖子进行赞，踩，取消赞踩的操作",notes = "/PostSatisfaction \n  id:帖子id" +
            "  status  1赞 2 踩  3 取消赞 4 取消踩}")
    @PostMapping("/PostSatisfaction")
    public  R<BbsPostsDto> postsSatisfaction(@RequestBody BbsPostsDto bbsPostsDto,HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
//        Integer userId = 12;
        bbsPostsDto.setUserSessionId(userId);
        BbsPostsDto postsDto = bbsPostsService.postsSatisfaction(bbsPostsDto.getId(), bbsPostsDto.getStatus(), bbsPostsDto.getUserSessionId());
        return new R<>(postsDto);
    }//postsSatisfaction end



    @PostMapping("/selectPosts")
    @ApiOperation(value = "帖子列表",notes = "query:查询条件（会在标签，标题，和正文中同时查询符合条件的数据）\n " +
            "sort：排序（1发布时间，2查看次数，3点赞次数，4帖子回复数，5打赏次数），" +
            "\n pageNum：当前页，pageSize：每页多少" +
            "\n articleType 帖子分类（值为0或者null时 不分类）"+
            "\n labelId 标签id（值为0或者null时 查找所有）"
            )//排序5分页问题
    public R selectPosts(@RequestBody BbsPostsDto bbsPostsDto,HttpServletRequest request){
        if (bbsPostsDto.getSort()==null)bbsPostsDto.setSort(1);//给一个排序初始值
        if(bbsPostsDto.getUserId()==null){
            Integer useId= 1;
            bbsPostsDto.setUserSessionId(useId);
        }else{
            bbsPostsDto.setUserSessionId(UserUtils.getUserId(request));
        }
        return new R(bbsPostsService.selectPosts(bbsPostsDto));
    }//selectPosts end

    /**
    * @Description :获取被点赞和被收藏的总数
    * @Param
    * @return
    **/
    @ApiOperation(value = "根据用户获取帖子被点赞和被收藏的总数",notes = "1")
    @PostMapping("/getEnshrineandLikeNum")
    @ApiImplicitParam(name = "bbsPostsDom", value = "点赞和被收藏的总数", required = true, dataType = "BbsPostsDom")
    public R<Long> getEnshrineandLikeNum(@RequestBody BbsPostsDom bbsPostsDom){
        R<Long> r = new R<Long>();
        Integer id = bbsPostsDom.getUserId();
        Long  result1 = bbsPostsService.getEnshrineNum(id);
        //获取收藏数量
        Long  result2 = bbsPostsService.getLikeNum(id);
        Long result = result1 + result2;
        r.setData(result);
        return r ;
    }

    @PostMapping("/giveAward")
    @ApiOperation(value = "打赏帖子",notes = "postsId:帖子id;giftId：礼物id; content：打赏内容,createId:打赏人id")
    public R giveAward(@RequestBody BbsAwardDto bbsAwardDto){
        //通过auth 权限字段来排序  可改
        bbsPostsService.giveAward(bbsAwardDto);
        return new R();
    }

    @GetMapping("/selectTopPosts")
    @ApiOperation(value = "获取置顶的帖子列表",notes = "获取置顶的帖子列表，最多5个")
    public R selectTopPosts(){
        //通过auth 权限字段来排序  可改
        return new R(bbsPostsService.selectTopPosts());
    }


    @PostMapping("/selectPostsOne")
    @ApiOperation(value = "帖子详情",notes = "id  ：帖子id")
    public R selectPostsOne(@RequestBody BbsPostsDto bbsPostsDto,HttpServletRequest request){
        Integer useId= UserUtils.getUserId(request);
//        Integer useId= 16;
        bbsPostsDto.setUserSessionId(useId);
        BbsPostsDto bbsPosts =  bbsPostsService.getPosts(bbsPostsDto);
        return new R(bbsPosts);
    }

    @PostMapping("/attentionPosts")
    @ApiOperation(value = "关注帖子",notes = "id  ：帖子id,userId:关注人id")
    public R attentionPosts(@RequestBody BbsPostsDto bbsPostsDto){
         bbsPostsService.attentionPosts(bbsPostsDto);
        return new R();
    }
    @PostMapping("/selectAttentionPosts")
    @ApiOperation(value = "获取关注帖子列表",notes = "userId:关注人id")
    public R selectAttentionPosts(@RequestBody BbsPostsDto bbsPostsDto){
        List list =     bbsPostsService.selectAttentionPosts(bbsPostsDto);
        return new R(list);
    }


    @PostMapping("/enshrinePosts")
    @ApiOperation(value = "收藏帖子",notes = "id  ：帖子id")
    public ResultVO<BbsPostsDto> enshrinePosts(@RequestBody BbsPostsDto bbsPostsDto, HttpServletRequest request){
                 Integer userId= UserUtils.getUserId(request);
//         Integer userId= 16;
         bbsPostsDto.setUserSessionId(userId);
        BbsPostsDom bbsPostsDom= BbsUtil.getDomOrDto(bbsPostsDto,BbsPostsDom.class);
        bbsPostsService.enshrinePosts(bbsPostsDto);
        return ResultVOUtil.success(bbsPostsService.selectenshrinePost(bbsPostsDom));
    }

    @PostMapping("/selectEnshrinePosts")
    @ApiOperation(value = "获取收藏帖子列表,当前登录人一共收藏多少帖子",notes = "userId:收藏人id")
    public R selectEnshrinePosts(@RequestBody BbsPostsDto bbsPostsDto,HttpServletRequest request){
        Integer userId= UserUtils.getUserId(request);
        bbsPostsDto.setUserSessionId(userId);
        List list =  bbsPostsService.selectEnshrinePosts(bbsPostsDto);
        return new R(list);
    }


    @PostMapping("/selectCorrelation")
    @ApiOperation(value = "获取相关的帖子",notes = "id:帖子id")
    public R selectCorrelation(@RequestBody BbsPostsDto bbsPostsDto){
        if(bbsPostsDto==null)if(bbsPostsDto.getId()==null)return new R();
        List list = bbsPostsService.selectCorrlation(bbsPostsDto);
        return  new R(list);
    }





    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @GetMapping("/delete/{id}")
    //@ApiOperation(value = "根据id删除")
    public R<Boolean> deletePosts(@PathVariable Integer id) {
        BbsPosts bbsPosts = new BbsPosts();
        bbsPosts.setId(id);
        return new R<>(bbsPostsService.deleteById(bbsPosts));
    }






    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsPostsDom
    */
    @GetMapping("/{id}")
    public R<BbsPosts> get(@PathVariable Integer id) {
        return new R<>(bbsPostsService.selectById(id));
    }


    /**
    * 分页查询信息
    *
    * @param params 分页对象
    * @return 分页对象
    */
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return bbsPostsService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsPosts  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsPosts bbsPosts) {
        return new R<>(bbsPostsService.insert(bbsPosts));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsPosts bbsPosts = new BbsPosts();
        bbsPosts.setId(id);
        bbsPosts.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsPostsService.updateById(bbsPosts));
    }

    /**
     * 编辑
     * @param  bbsPosts  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsPosts bbsPosts) {
        return new R<>(bbsPostsService.updateById(bbsPosts));
    }

//    /**
//     * 根据用户查询帖子信息
//     *
//     * @param bbsPostsDom
//     * @return
//     */
//    @ApiOperation(value = "根据用户查询帖子详情",notes = "1")
//    @ApiImplicitParam(name = "bbsPostsDom", value = "用户帖子信息", required = true, dataType = "BbsPostsDom")
//    @PostMapping("/getPostsInfo")
//    public R<BbsPostsLabelDto> getPostsInfo(@RequestBody BbsPostsDom bbsPostsDom) {
//
//        return bbsPostsService.getPostsInfo(bbsPostsDom);
//    }

    /**
     * 根据用户Id查询帖子列表信息
     *
     * @param bbsPostsDom
     * @return
     */
    @ApiOperation(value = "根据用户Id查询帖子列表信息",notes = "1")
    @ApiImplicitParam(name = "bbsPostsDom", value = "查询帖子信息", required = true, dataType = "BbsPostsDom")
    @PostMapping("/getPostsList")
    public ResultVO getPostsList(@RequestBody BbsPostsDom bbsPostsDom ,HttpServletRequest request) {
       PageResult<BbsPostsDto> pageResult = new PageResult<>();
        Integer userId = UserUtils.getUserId(request);
//        Integer userId =23;
        ResultVO r = new ResultVO();
        List<BbsPostsDto> list = bbsPostsService.getPostsInfo(bbsPostsDom);

        for (BbsPostsDto bpl: list){
            Integer uid = bpl.getUserId();
            Integer postsId = bpl.getId();
            EnshrinePostsDom ePDom = new EnshrinePostsDom();
            ePDom.setUserId(userId);
            ePDom.setPostsId(postsId);
            Long i = bbsPostsService.getEnshrineStatus(ePDom);
            if(i >= 1){
                bpl.setEnshriStatue(CommonConstant.Concern_Status_one);
            }else {
                bpl.setEnshriStatue(CommonConstant.Concern_Status_zero);
            }

        }
        Integer postsNum = bbsPostsService.getPostsNum(bbsPostsDom);
        pageResult.setPageNum(bbsPostsDom.getPageNum()/bbsPostsDom.getPageSize()+1);
        pageResult.setPageSize(bbsPostsDom.getPageSize());
        pageResult.setAllNum(postsNum);
        pageResult.setAllPage((postsNum+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }

    /**
     * 根据用户Id,分类id查询用户收藏de帖子列表信息
     *
     * @param bbsPostsDom
     * @return
     */
    @ApiOperation(value = "根据用户Id,分类id查询收藏帖子列表信息",notes = "1")
    @ApiImplicitParam(name = "bbsPostsDom", value = "查询帖子信息", required = true, dataType = "BbsPostsDom")
    @PostMapping("/getEnshrinePostsList")
    public ResultVO getEnshrinePostsList(@RequestBody BbsPostsDom bbsPostsDom ,HttpServletRequest request) {
        PageResult<BbsPostsDto> pageResult = new PageResult<>();
        Integer userId= UserUtils.getUserId(request);
//        Integer userId= 5;
        ResultVO r = new ResultVO();

        Integer EnshrinePostsNum = bbsPostsService.getEnshrinePostsNum(bbsPostsDom);
        List<BbsPostsDto> list = bbsPostsService.getEnshrinePostsList(bbsPostsDom);
        for (BbsPostsDto bpb : list){
            Integer uid = bpb.getUserId();
            Integer postsId = bpb.getId();
            EnshrinePostsDom ePDom = new EnshrinePostsDom();
            ePDom.setUserId(userId);
            ePDom.setPostsId(postsId);
            Long i = bbsPostsService.getEnshrineStatus(ePDom);
            if(i >= 1){
                bpb.setEnshriStatue(CommonConstant.Concern_Status_one);
            }else {
                bpb.setEnshriStatue(CommonConstant.Concern_Status_zero);
            }
        }
        pageResult.setPageNum(bbsPostsDom.getPageNum()/bbsPostsDom.getPageSize()+1);
        pageResult.setPageSize(bbsPostsDom.getPageSize());
        pageResult.setAllNum(EnshrinePostsNum);
        pageResult.setAllPage((EnshrinePostsNum+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }

    /**
     * 根据用户Id查看是否有当前用户创建的草稿帖子
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户Id查看是否有当前用户创建的草稿帖子",notes = "返回帖子草稿信息")
    @ApiImplicitParam(name = "bbsPostsDom", value = "查询帖子草稿信息", required = true, dataType = "BbsPostsDom")
    @PostMapping("/getDraftsPostsByUserId")
    public ResultVO<List<BbsPostsDto>> getDraftsPostsByUserId(HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        List<BbsPostsDto> bbsPostsDtoList = bbsPostsService.getDraftsPostsByUserId(userId);
        return ResultVOUtil.success(bbsPostsDtoList);
    }


    /**
     * 根据搜索输入推荐类似帖子标题
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据搜索输入推荐类似帖子标题",notes = "返回帖子名称")
    @ApiImplicitParam(name = "bbsPostsDom", value = "根据搜索输入推荐类似帖子标题", required = true, dataType = "BbsPostsDom")
    @PostMapping("/getPostsByParma")
    public ResultVO getPostsByParma(@RequestBody BbsPostsDom bbsPostsDom) {
        ResultVO r = new ResultVO();
        StringUtil util = new StringUtil();
        List<String> querys = util.sLits(bbsPostsDom.getQuery());
        bbsPostsDom.setQuerys(querys);
        List<String> list = bbsPostsService.getPostsByParma(bbsPostsDom);
        r.setData(list);
        return r;
    }



}
