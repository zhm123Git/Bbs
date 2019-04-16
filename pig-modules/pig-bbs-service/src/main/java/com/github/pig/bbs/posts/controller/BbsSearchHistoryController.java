package com.github.pig.bbs.posts.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.posts.DOM.BbsSearchHistoryDom;
import com.github.pig.bbs.posts.DTO.BbsSearchHistoryDto;
import com.github.pig.bbs.posts.entity.BbsSearchHistory;
import com.github.pig.bbs.posts.service.IBbsSearchHistoryService;
import com.github.pig.bbs.util.BbsUtil;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.util.ResultVO;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户搜索历史记录表 前端控制器
 * </p>
 *
 * @author zhm
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/bbsSearchHistory")
@Api(value = "bbsSearchHistory",description="搜索功能")
public class BbsSearchHistoryController extends BaseController {
    @Autowired private IBbsSearchHistoryService bbsSearchHistoryService;

    /**
     * 添加搜索记录
     * @param  bbsSearchHistoryDom
     * @return success/false
     */
    @ApiOperation(value = "添加搜索记录",notes = "添加搜索记录")
    @ApiImplicitParam(name = "bbsSearchHistoryDom", value = "添加搜索记录", dataType = "BbsSearchHistoryDom")
    @PostMapping("/addSearchHistory")
    public R<Boolean> addSearchHistory(@RequestBody BbsSearchHistoryDom bbsSearchHistoryDom, HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        bbsSearchHistoryDom.setUserId(userId);
        bbsSearchHistoryDom.setSearchTime(new Date());
        Integer i = bbsSearchHistoryService.selectSearchHistoryNum(bbsSearchHistoryDom);
        if (i < 1) {
            BbsSearchHistory bbssh = BbsUtil.getDomOrDto(bbsSearchHistoryDom, BbsSearchHistory.class);
            return new R<>(bbsSearchHistoryService.insert(bbsSearchHistoryDom));
        }else {
            Boolean b = true;
            return new R<>(b);
        }

    }

    /**
     * 搜索历史列表
     * @param request
     * @return
     */
    @ApiOperation(value = "搜索历史记录",notes = "搜索历史记录")
    @ApiImplicitParam(name = "bbsSearchHistoryDom", value = "搜索历史记录", dataType = "BbsSearchHistoryDom")
    @PostMapping("/getSearchHistory")
    public ResultVO<List<BbsSearchHistoryDto>> getSearchHistory(@RequestBody BbsSearchHistoryDom bbsSearchHistoryDom, HttpServletRequest request) {
        ResultVO<List<BbsSearchHistoryDto>> r  = new ResultVO<List<BbsSearchHistoryDto>>();
        Integer userId = UserUtils.getUserId(request);
        bbsSearchHistoryDom.setUserId(userId);
        List<BbsSearchHistoryDto> list = bbsSearchHistoryService.selectByUserId(userId);
        r.setData(list);
        return r ;
    }

    /**
     * 搜索热门
     * @param
     * @return
     */
    @ApiOperation(value = "搜索热门推送",notes = "搜索热门")
    @ApiImplicitParam(name = "bbsSearchHistoryDom", value = "搜索热门推送", dataType = "BbsSearchHistoryDom")
    @PostMapping("/getHotSearchHistory")
    public R<List<BbsSearchHistoryDto>> getHotSearchHistory() {
        R<List<BbsSearchHistoryDto>> r = new R<List<BbsSearchHistoryDto>>();
        List<BbsSearchHistoryDto> list = bbsSearchHistoryService.selectHotSearchHistory();
        r.setData(list);
        return r;
    }


    /**
     * 删除历史搜索记录
     * @param
     * @return
     */
    @ApiOperation(value = "删除历史搜索记录",notes = "删除历史搜索记录")
    @ApiImplicitParam(name = "bbsSearchHistoryDom", value = "删除历史搜索记录", dataType = "BbsSearchHistoryDom")
    @PostMapping("/removeHotSearchHistory")
    public ResultVO<Boolean> removeHotSearchHistory(HttpServletRequest request) {
        ResultVO<Boolean> r = new ResultVO<Boolean>();
        Integer userId = UserUtils.getUserId(request);
        Boolean result = bbsSearchHistoryService.deleteSearchHistory(userId);
        r.setData(result);
        return r;
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
        return bbsSearchHistoryService.selectPage(new Query<>(params), new EntityWrapper<>());
    }


    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsSearchHistory bbsSearchHistory = new BbsSearchHistory();
        bbsSearchHistory.setId(Long.valueOf(id));
        bbsSearchHistory.setSearchTime(new Date());
        return new R<>(bbsSearchHistoryService.updateById(bbsSearchHistory));
    }


}
