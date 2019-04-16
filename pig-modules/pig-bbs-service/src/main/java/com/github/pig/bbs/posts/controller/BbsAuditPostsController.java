package com.github.pig.bbs.posts.controller;
import java.util.Map;
import java.util.Date;

import com.github.pig.bbs.posts.DTO.BbsAuditPostsDto;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.bbs.posts.entity.BbsAuditPosts;
import com.github.pig.bbs.posts.service.BbsAuditPostsService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-12-06
 */
@RestController
@RequestMapping("/bbsAuditPosts")
@Api(value = "审核")
public class BbsAuditPostsController extends BaseController {
    @Autowired

    private BbsAuditPostsService bbsAuditPostsService;



    @PostMapping("/isPostsPublic")
    @ApiOperation(value = "审核帖子",notes = "id")
    public R isPostsPublic(@RequestBody BbsAuditPostsDto bbsAuditPostsDto){
        bbsAuditPostsService.auditPosts(bbsAuditPostsDto);
        return new R();
    }


    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsAuditPosts
    */
    @GetMapping("/{id}")
    public R<BbsAuditPosts> get(@PathVariable Integer id) {
        return new R<>(bbsAuditPostsService.selectById(id));
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
        return bbsAuditPostsService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsAuditPosts  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsAuditPosts bbsAuditPosts) {
        return new R<>(bbsAuditPostsService.insert(bbsAuditPosts));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsAuditPosts bbsAuditPosts = new BbsAuditPosts();
        bbsAuditPosts.setId(id);
        return new R<>(bbsAuditPostsService.updateById(bbsAuditPosts));
    }

    /**
     * 编辑
     * @param  bbsAuditPosts  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsAuditPosts bbsAuditPosts) {
        return new R<>(bbsAuditPostsService.updateById(bbsAuditPosts));
    }
}
