package com.github.pig.bbs.posts.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.posts.entity.BbsUserPosts;
import com.github.pig.bbs.posts.service.BbsUserPostsService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
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
@Api(value = "/bbsUserPosts",description="用户帖子")
@RestController
@RequestMapping("/bbsUserPosts")
public class BbsUserPostsController extends BaseController {
    @Autowired private BbsUserPostsService bbsUserPostsService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsUserPosts
    */
    @GetMapping("/{id}")
    public R<BbsUserPosts> get(@PathVariable Integer id) {
        return new R<>(bbsUserPostsService.selectById(id));
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
        return bbsUserPostsService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsUserPosts  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsUserPosts bbsUserPosts) {
        return new R<>(bbsUserPostsService.insert(bbsUserPosts));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsUserPosts bbsUserPosts = new BbsUserPosts();
        bbsUserPosts.setId(id);
     //   bbsUserPosts.setUpdateTime(new Date());
      //  bbsUserPosts.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsUserPostsService.updateById(bbsUserPosts));
    }

    /**
     * 编辑
     * @param  bbsUserPosts  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsUserPosts bbsUserPosts) {
     //   bbsUserPosts.setUpdateTime(new Date());
        return new R<>(bbsUserPostsService.updateById(bbsUserPosts));
    }


}
