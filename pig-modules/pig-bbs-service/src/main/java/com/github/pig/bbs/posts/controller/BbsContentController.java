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
import com.github.pig.bbs.posts.entity.BbsContent;
import com.github.pig.bbs.posts.service.BbsContentService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/bbsContent")
public class BbsContentController extends BaseController {
    @Autowired
    private BbsContentService bbsContentService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsContentDom
    */
    @GetMapping("/{id}")
    public R<BbsContent> get(@PathVariable Integer id) {
        return new R<>(bbsContentService.selectById(id));
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
        return bbsContentService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsContent  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsContent bbsContent) {
        return new R<>(bbsContentService.insert(bbsContent));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsContent bbsContent = new BbsContent();
        bbsContent.setId(id);
        bbsContent.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsContentService.updateById(bbsContent));
    }

    /**
     * 编辑
     * @param  bbsContent  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsContent bbsContent) {
        return new R<>(bbsContentService.updateById(bbsContent));
    }
}
