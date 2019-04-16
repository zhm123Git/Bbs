package com.github.pig.bbs.posts.controller;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.github.pig.bbs.posts.DTO.BbsAwardDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.bbs.posts.entity.BbsGift;
import com.github.pig.bbs.posts.service.BbsGiftService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Api(value = "/bbsGift",description="礼物")
@RestController
@RequestMapping("/bbsGift")
public class BbsGiftController extends BaseController {
    @Autowired private BbsGiftService bbsGiftService;


    @PostMapping("/selectGift")
    @ApiOperation(value = "获取礼物列表")
    public R selectGift(){
        List list =  bbsGiftService.selectGift();
        return new R(list);
    }


    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsGift
    */
    @GetMapping("/{id}")
    public R<BbsGift> get(@PathVariable Integer id) {
        return new R<>(bbsGiftService.selectById(id));
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
        return bbsGiftService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsGift  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsGift bbsGift) {
        return new R<>(bbsGiftService.insert(bbsGift));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsGift bbsGift = new BbsGift();
        bbsGift.setId(id);
      //  bbsGift.setUpdateTime(new Date());
        bbsGift.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsGiftService.updateById(bbsGift));
    }

    /**
     * 编辑
     * @param  bbsGift  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsGift bbsGift) {
      //  bbsGift.setUpdateTime(new Date());
        return new R<>(bbsGiftService.updateById(bbsGift));
    }
}
