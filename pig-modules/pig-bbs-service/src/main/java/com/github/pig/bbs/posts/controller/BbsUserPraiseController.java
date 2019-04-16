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
import com.github.pig.bbs.posts.entity.BbsUserPraise;
import com.github.pig.bbs.posts.service.BbsUserPraiseService;
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
@RequestMapping("/bbsUserPraise")
public class BbsUserPraiseController extends BaseController {
    @Autowired private BbsUserPraiseService bbsUserPraiseService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsUserPraise
    */
    @GetMapping("/{id}")
    public R<BbsUserPraise> get(@PathVariable Integer id) {
        return new R<>(bbsUserPraiseService.selectById(id));
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
        return bbsUserPraiseService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsUserPraise  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsUserPraise bbsUserPraise) {
        return new R<>(bbsUserPraiseService.insert(bbsUserPraise));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsUserPraise bbsUserPraise = new BbsUserPraise();
        bbsUserPraise.setId(id);
      //  bbsUserPraise.setUpdateTime(new Date());
        bbsUserPraise.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsUserPraiseService.updateById(bbsUserPraise));
    }

    /**
     * 编辑
     * @param  bbsUserPraise  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsUserPraise bbsUserPraise) {
      //  bbsUserPraise.setUpdateTime(new Date());
        return new R<>(bbsUserPraiseService.updateById(bbsUserPraise));
    }
}
