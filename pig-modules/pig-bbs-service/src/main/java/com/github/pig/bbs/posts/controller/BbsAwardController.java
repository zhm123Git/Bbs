package com.github.pig.bbs.posts.controller;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.github.pig.bbs.posts.DOM.BbsAwardDom;
import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import com.github.pig.common.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.posts.entity.BbsAward;
import com.github.pig.bbs.posts.service.BbsAwardService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Api(value = "/bbsAward",description="打赏")
@RestController
@RequestMapping("/bbsAward")
public class BbsAwardController extends BaseController {
    @Autowired
    private BbsAwardService bbsAwardService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsAward
    */
    @GetMapping("/{id}")
    public R<BbsAward> get(@PathVariable Integer id) {
        return new R<>(bbsAwardService.selectById(id));
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
        return bbsAwardService.selectPage(new Query<>(params), new EntityWrapper<>());
    }




    /**
     * 添加
     * @param  bbsAward  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsAward bbsAward) {
        return new R<>(bbsAwardService.insert(bbsAward));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsAward bbsAward = new BbsAward();
        bbsAward.setId(id);
       // bbsAward.setUpdateTime(new Date());
        bbsAward.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsAwardService.updateById(bbsAward));
    }

    /**
     * 编辑
     * @param  bbsAward  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsAward bbsAward) {
    //    bbsAward.setUpdateTime(new Date());
        return new R<>(bbsAwardService.updateById(bbsAward));
    }

    @ApiOperation(value = "给某个用户送礼物",notes = "例子:{\n" +
            "  \"content\": \"送玫瑰\",\n" +
            "  \"createPostUserId\": 2,\n" +
            "  \"giftId\": 1\n" +
            "}返回true,false")
    @PostMapping("/awardUserWithAward")
    @ApiImplicitParam(name = "bbsAwardDom", value = "打赏", required = true, dataType = "BbsAwardDom")
    public ResultVO<Boolean> awardUserWithAward(@RequestBody BbsAwardDom bbsAwardDom, HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
//        Integer userId = 22;
        if(bbsAwardDom.getPostsId()==null){
            throw  new  RuntimeException("请选择一条帖子");
        }
        if(bbsAwardDom.getCreatePostUserId()==null){
            throw  new  RuntimeException("请选择发帖人");
        }
        if(userId==null){
            throw  new  RuntimeException("未登录,不能打赏");
        }
        bbsAwardDom.setCreateId(userId);
        return ResultVOUtil.success(bbsAwardService.awardUserWithAward(bbsAwardDom));
    }
}
