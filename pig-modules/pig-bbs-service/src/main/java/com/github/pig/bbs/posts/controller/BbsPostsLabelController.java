package com.github.pig.bbs.posts.controller;
import com.github.pig.bbs.posts.DOM.BbsPostsLabelDom;
import com.github.pig.bbs.posts.DTO.BbsPostsLabelDto;
import com.github.pig.common.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.util.R;
import com.github.pig.bbs.posts.entity.BbsPostsLabel;
import com.github.pig.bbs.posts.service.BbsPostsLabelService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-23
 */
@Api(value = "/bbsPostsLabel",description="标签帖子")
@RestController
@RequestMapping("/bbsPostsLabel")
public class BbsPostsLabelController extends BaseController {
    private final static Logger log= LoggerFactory.getLogger(BbsPostsLabelController.class);
    @Autowired private BbsPostsLabelService bbsPostsLabelService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsPostsLabelDom
    */
    @GetMapping("/{id}")
    public R<BbsPostsLabel> get(@PathVariable Integer id) {
        return new R<>(bbsPostsLabelService.selectById(id));
    }

    /**
    * 分页查询信息
    *
    * @param bbsPostsLabelDom 分页对象
    * @return 分页对象
    */
    @ApiOperation(value = "根据标签查询帖子详情",notes = "1")
    @ApiImplicitParam(name = "bbsPostsLabelDom", value = "标签和帖子信息", required = true, dataType = "BbsPostsLabelDom")
    @PostMapping("/getpage")
    public R<PageResult<BbsPostsLabelDto>> getpage(@RequestBody BbsPostsLabelDom bbsPostsLabelDom) {

        return bbsPostsLabelService.pageinfo(bbsPostsLabelDom);
    }

    /**
     * 添加
     * @param  bbsPostsLabel  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsPostsLabel bbsPostsLabel) {
        return new R<>(bbsPostsLabelService.insert(bbsPostsLabel));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsPostsLabel bbsPostsLabel = new BbsPostsLabel();
        bbsPostsLabel.setId(id);
        return new R<>(bbsPostsLabelService.updateById(bbsPostsLabel));
    }

    /**
     * 编辑
     * @param  bbsPostsLabel  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsPostsLabel bbsPostsLabel) {
        return new R<>(bbsPostsLabelService.updateById(bbsPostsLabel));
    }
}
