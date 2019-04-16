package com.github.pig.bbs.posts.controller;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.bbs.posts.entity.BbsTopPosts;
import com.github.pig.bbs.posts.service.BbsTopPostsService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/bbsTopPosts")
public class BbsTopPostsController extends BaseController {
    @Autowired private BbsTopPostsService bbsTopPostsService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsTopPosts
    */
    @GetMapping("/{id}")
    public R<BbsTopPosts> get(@PathVariable Integer id) {
        return new R<>(bbsTopPostsService.selectById(id));
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
        return bbsTopPostsService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsTopPosts  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsTopPosts bbsTopPosts) {
        return new R<>(bbsTopPostsService.insert(bbsTopPosts));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsTopPosts bbsTopPosts = new BbsTopPosts();
        bbsTopPosts.setId(id);
    //    bbsTopPosts.setUpdateTime(new Date());
        bbsTopPosts.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsTopPostsService.updateById(bbsTopPosts));
    }

    /**
     * 编辑
     * @param  bbsTopPosts  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsTopPosts bbsTopPosts) {
     //   bbsTopPosts.setUpdateTime(new Date());
        return new R<>(bbsTopPostsService.updateById(bbsTopPosts));
    }
}
