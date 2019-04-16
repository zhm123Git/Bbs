package com.github.pig.bbs.user.controller;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pig.bbs.user.DTO.BbsNotifyDto;
import com.github.pig.common.util.ListResponse;
import com.github.pig.common.util.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.bbs.user.entity.BbsNotify;
import com.github.pig.bbs.user.service.BbsNotifyService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 */
@Api(value = "bbsNotify",description="消息通知")
@RestController
@RequestMapping("/bbsNotify")
public class BbsNotifyController extends BaseController {
    @Autowired
    private BbsNotifyService bbsNotifyService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsNotify
    */
    @ApiOperation(value = "获取当前通知详情,例子:1",notes = "返回当前通知详情")
    @ApiImplicitParam(name = "id", value = "通知主键", required = true, dataType = "int")
    @GetMapping("/{id}")
    public R<BbsNotify> get(@PathVariable Integer id) {

        return new R<>(bbsNotifyService.selectById(id));
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
        return bbsNotifyService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsNotifyDto  实体
     * @return success/false
     */
    @ApiOperation(value = "添加通知,例子:{\n" +
            "  \"content\": \"通知内容\",\n" +
            "  \"notifyType\": 0,\n" +
            "  \"status\": 0,\n" +
            "  \"userId\": 9\n" +
            "}",notes = "返回boolean")
    @ApiImplicitParam(name = "bbsNotifyDto", value = "通知主键", required = true, dataType = "BbsNotifyDto")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody BbsNotifyDto bbsNotifyDto) {
        BbsNotify bbsNotify = new BbsNotify();
        BeanUtils.copyProperties(bbsNotifyDto,bbsNotify);
        bbsNotify.setSendTime(new Date());
        return new R<>(bbsNotifyService.insert(bbsNotify));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        BbsNotify bbsNotify = new BbsNotify();
        bbsNotify.setNotifyId(id);
        return new R<>(bbsNotifyService.deleteById(bbsNotify));
    }

    /**
     * 编辑
     * @param  bbsNotifyDto  实体
     * @return success/false
     */
    @ApiOperation(value = "修改通知",notes = "返回boolean")
    @ApiImplicitParam(name = "bbsNotifyDto", value = "通知", required = true, dataType = "BbsNotifyDto")
    @PostMapping
    public R<Boolean> edit(@RequestBody BbsNotifyDto bbsNotifyDto) {
        BbsNotify bbsNotify = new BbsNotify();
        BeanUtils.copyProperties(bbsNotifyDto,bbsNotify);
        return new R<>(bbsNotifyService.updateById(bbsNotify));
    }

    /**
     * 查询所有通知
     *
     * @param bbsNotifyDto 通知
     * @return BbsNotifyDto
     */
    @ApiOperation(value = "",notes = "返回通知列表")
    @ApiImplicitParam(name = "bbsNotifyDto", value = "通知列表", required = true, dataType = "BbsNotifyDto")
    @PostMapping("/findNotifyList")
    public R<ListResponse<BbsNotify>> findNotifyList(@RequestBody BbsNotifyDto bbsNotifyDto) {
        PageParams pageParams = new PageParams();
        pageParams.setLimit(bbsNotifyDto.getLimit());
        pageParams.setOffset(bbsNotifyDto.getOffset());
        Pair<List<BbsNotify>, Long> pair = bbsNotifyService.findNotifyList(bbsNotifyDto,pageParams);
        ListResponse<BbsNotify> response = ListResponse.build(pair.getKey(), pair.getValue());
        return new R<>(response);
    }
}
