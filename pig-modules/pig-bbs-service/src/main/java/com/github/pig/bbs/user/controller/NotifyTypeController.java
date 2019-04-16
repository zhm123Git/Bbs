package com.github.pig.bbs.user.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.bbs.user.entity.NotifyType;
import com.github.pig.bbs.user.service.NotifyTypeService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
@RestController
@RequestMapping("/notifyType")
public class NotifyTypeController extends BaseController {
    @Autowired
    private NotifyTypeService notifyTypeService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return NotifyType
    */
    @GetMapping("/{id}")
    public R<NotifyType> get(@PathVariable Integer id) {
        return new R<>(notifyTypeService.selectById(id));
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
        return notifyTypeService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  notifyType  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody NotifyType notifyType) {
        return new R<>(notifyTypeService.insert(notifyType));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        NotifyType notifyType = new NotifyType();
        notifyType.setNotifyTypeId(id);
        return new R<>(notifyTypeService.updateById(notifyType));
    }

}
