package com.github.pig.bbs.posts.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.posts.DTO.BbsReplyDto;
import com.github.pig.bbs.posts.entity.BbsReply;
import com.github.pig.bbs.posts.service.BbsReplyService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.util.ResultVO;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/bbsReply")
@Api(value = "bbsReply",description="帖子回复")
public class BbsReplyController extends BaseController {
    @Autowired private BbsReplyService bbsReplyService;






/*
    结构为树
    查看帖子回复列表时  查看 postsid 和帖子id相同的  同时pid为null的顶级节点
    在查看评论的回复时  可以使用热加载 点击一个  加载一个  查询pid 为评论id的下一个节点
    新增评论时  如果传过来 评论id 就是 回复评论
    如果没有评论id 就默认为 回复的是帖子  pid 为null

    需要改 获取帖子回复列表（包括展开）
           新增评论

*/

    /*
    * 帖子回复
    * */
    @PostMapping(value="/insertReply")
    @ApiOperation(value = "新增帖子回复" ,notes = "" +
            "\n postsId:对应帖子Id  " +
            "\n content : 内容 " +
            "\n pid :为一级评论的id（此pid为空时 回复帖子是一级评论 回复帖子） " +
            "\n bid  被评论人的id  " +
            "\n  bname   被评论人的用户名 " +
            "\n  当新增的数据为一级评论时   新增content（内容）和postsId（帖子id）就够了  " +
            "\n  当新增的数据为二级评论时   需要：" +
            "\n pid ： pid 为一级评论的id  这里是评论id"+
            "\n bid ：被评论人的id    这里是userid"+
            "\n bname ：被评论人的名字 "+
            "\n content: 内容" +
            "\n postsId: 帖子id "
    )
    public R insertReply(@RequestBody BbsReplyDto bbsReplyDto, HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
        bbsReplyDto.setUserId(userId);
        bbsReplyService.insertReply(bbsReplyDto);
        return  new R();
    }//end


    /*
    * 查看回复列表
    * */
    @PostMapping(value = "/selectReply")
    @ApiOperation(value = "根据帖子Id  查看回复列表",notes = "" +
            "postsId:帖子Id " +
            "\n pid:评论id(此id为空时查询回复帖子根回复)" +
            "\n 查询一级评论 不需要pid  查询二级评论时需要一级评论的id作为pid传到服务器   ")
    public R selectReply(@RequestBody BbsReplyDto bbsReplyDto){
        /*
        * 当pid 为null时 查询根回帖  即：pid为null  postsId 为帖子id的数据 根据发布时间排序 可分页
        * 当pid 为评论id时  展开二级品论 postsid为帖子id   pid为一级评论id  返回评论人与被评论人名字和头像 连接sysuser表
        * */

        return new R(bbsReplyService.selectReply(bbsReplyDto));
    }//end





    @PostMapping(value = "/deleteReply")
    @ApiOperation(value = "根据Id删除回复",notes = "id：回复的id")
    public R deleteReply(@RequestBody BbsReplyDto bbsReplyDto,HttpServletRequest request){
//        Integer userId = 21;
        Integer userId = UserUtils.getUserId(request);
        //判断删除人是否与创建人一致
        if( userId==null ||  !bbsReplyDto.getUserId().equals(userId)){
            throw new RuntimeException("非本人,不能删除评论或回复");
        }
        if(bbsReplyDto.getPid()!=null){
            //删除回复只删除自身
            bbsReplyService.deleteById(bbsReplyDto);
        }else{
            //删除当前评论删除它的所有回复以及自身
            bbsReplyService.deleteById(bbsReplyDto);
            bbsReplyService.deleteByIdAndPid(bbsReplyDto);
        }

        return new R();
    }

    @PostMapping(value = "/updateReply")
    @ApiOperation(value = "修改评论 ，只能修改内容" ,notes = "id ：回复的Id； content:回复的内容")
    public R updatReply(@RequestBody BbsReplyDto bbsReplyDto){
        bbsReplyService.updateReply(bbsReplyDto);
        return new R();
    }

    /**
     * 采纳评论
     * @param bbsReplyDto
     * @return
     */
    @PostMapping(value = "/acceptReply")
    @ApiOperation(value = "采纳问答帖子评论",notes = "id: 回复的Id ；postsId 帖子的Id")
    public ResultVO acceptReply(@RequestBody BbsReplyDto bbsReplyDto ,HttpServletRequest request)throws Exception{
        ResultVO r = bbsReplyService.acceptReply(bbsReplyDto);
        return r;
    }




    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsReply
    */
    @GetMapping("/{id}")
    public R<BbsReply> get(@PathVariable Integer id) {
        return new R<>(bbsReplyService.selectById(id));
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
        return bbsReplyService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsReply  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsReply bbsReply) {
        return new R<>(bbsReplyService.insert(bbsReply));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsReply bbsReply = new BbsReply();
        bbsReply.setId(id);
     //   bbsReply.setUpdateTime(new Date());
        bbsReply.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsReplyService.updateById(bbsReply));
    }

    /**
     * 编辑
     * @param  bbsReply  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsReply bbsReply) {
      //  bbsReply.setUpdateTime(new Date());
        return new R<>(bbsReplyService.updateById(bbsReply));
    }


}
