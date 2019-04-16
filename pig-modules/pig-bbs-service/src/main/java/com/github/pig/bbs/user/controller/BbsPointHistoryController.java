package com.github.pig.bbs.user.controller;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.github.pig.bbs.user.DTO.BbsPointHistoryDto;
import com.github.pig.common.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pig.common.constant.CommonConstant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.user.entity.BbsPointHistory;
import com.github.pig.bbs.user.service.BbsPointHistoryService;
import com.github.pig.common.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuyanan
 * @since 2018-12-04
 * 积分
 */
@Api(value = "bbsPointHistory",description="用户积分记录")
@RestController
@RequestMapping("/bbsPointHistory")
public class BbsPointHistoryController extends BaseController {
    @Autowired
    private BbsPointHistoryService bbsPointHistoryService;

    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsPointHistory
    */
    @GetMapping("/{id}")
    public R<BbsPointHistory> get(@PathVariable Integer id) {
        return new R<>(bbsPointHistoryService.selectById(id));
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
        return bbsPointHistoryService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsPointHistoryDto  实体
     * @return success/false
     */
    @ApiOperation(value = "添加积分",notes = "返回boolean")
    @ApiImplicitParam(name = "bbsPointHistoryDto", value = "积分", required = true, dataType = "BbsPointHistoryDto")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody BbsPointHistoryDto bbsPointHistoryDto) {
        BbsPointHistory bbsPointHistory = new BbsPointHistory();
        BeanCopierUtils.copy(bbsPointHistoryDto,bbsPointHistory);
        bbsPointHistory.setCreateTime(new Date());
        return new R<>(bbsPointHistoryService.updateUserAmount(bbsPointHistory));
    }


    /**
     * 查询所有积分记录
     *
     * @param bbsPointHistoryDto
     * @return BbsNotifyDto
     */
    @ApiOperation(value = "查询某个用户积分历史,例如:{\"limit\": 10,\"offset\": 0,\"userId\": 1}",notes = "返回积分历史记录列表")
    @ApiImplicitParam(name = "bbsPointHistoryDto", value = "积分实体", required = true, dataType = "BbsPointHistoryDto")
    @PostMapping("/findPointHistoryList")
    public R<ListResponse<BbsPointHistoryDto>> findPointHistoryList(@RequestBody BbsPointHistoryDto bbsPointHistoryDto) {
        PageParams pageParams = new PageParams();
        pageParams.setPageNum(bbsPointHistoryDto.getPageNum());
        pageParams.setPageSize(bbsPointHistoryDto.getPageSize());
        Pair<List<BbsPointHistoryDto>, Long> pair = bbsPointHistoryService.findPointHistoryList(bbsPointHistoryDto,pageParams);
        Integer allPage=(pair.getValue().intValue()+pageParams.getPageSize()-1)/pageParams.getPageSize();
        ListResponse<BbsPointHistoryDto> response = ListResponse.build(pair.getKey(), pair.getValue(),allPage);
        //response.setAllPage(allPage);
        return new R<>(response);
    }



    /**
     * 查询所有积分记录
     *
     * @param bbsPointHistoryDto
     * @return BbsNotifyDto
     */
    @ApiOperation(value = "查询积分消费排行,按照消费次数排行,例如:{\"limit\": 10,\"offset\": 0}",notes = "返回积分历史记录排行列表")
    @ApiImplicitParam(name = "bbsPointHistoryDto", value = "积分实体", required = true, dataType = "BbsPointHistoryDto")
    @PostMapping("/findPointRankList")
    public R<ListResponse<BbsPointHistoryDto>> findPointRankList(@RequestBody BbsPointHistoryDto bbsPointHistoryDto) {
        PageParams pageParams = new PageParams();
        pageParams.setLimit(bbsPointHistoryDto.getLimit());
        pageParams.setOffset(bbsPointHistoryDto.getOffset());
        Pair<List<BbsPointHistoryDto>, Long> pair = bbsPointHistoryService.findPointRankList(bbsPointHistoryDto,pageParams);
        ListResponse<BbsPointHistoryDto> response = ListResponse.build(pair.getKey(), pair.getValue());
        return new R<>(response);
    }


}
