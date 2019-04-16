package com.github.pig.bbs.label.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.label.DOM.BbsLabelDom;
import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsLabelDto;
import com.github.pig.bbs.label.entity.BbsLabel;
import com.github.pig.bbs.label.service.BbsLabelService;
import com.github.pig.bbs.label.service.BbsUserLabelService;
import com.github.pig.bbs.posts.DTO.BbsPostsDto;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.*;
import com.github.pig.common.vo.BbsLabelVO;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-11-22
 */
@Api(value = "/bbsLabel",description="标签")
@RestController
@RequestMapping("/bbsLabel")
public class BbsLabelController extends BaseController {

    @Autowired
    private BbsLabelService bbsLabelService;
    @Autowired
    private BbsUserLabelService bbsUserLabelService;

    /**
     * 通过用户ID查询表签信息列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户Id查询标签信息列表",notes = "返回标签信息列表")
    @ApiImplicitParam(name = "bbsUserLabelDom", value = "标签信息列表", required = true, dataType = "BbsUserLabelDom")
    @PostMapping("/getLabelList")
    public ResultVO getLabelList(@RequestBody BbsUserLabelDom bbsUserLabelDom ,HttpServletRequest request) {
        PageResult<BbsLabelDto> pageResult = new PageResult<>();
        ResultVO r = new ResultVO();
        Integer userId = UserUtils.getUserId(request);
 //       Integer userId = 2;
        Integer uId = bbsUserLabelDom.getUserId();
        List<BbsLabelDto> list = bbsLabelService.getLabelInfo(bbsUserLabelDom);
        for (BbsLabelDto bbsLabelDto :list){
            Integer id = bbsLabelDto.getId();
            bbsUserLabelDom.setId(id);
            bbsUserLabelDom.setUserId(userId);
            Long labelNum = bbsUserLabelService.selectCounts(bbsUserLabelDom);
            bbsLabelDto.setAttentionUsers(labelNum.intValue());
            int i = bbsUserLabelService.getLabelStatus(bbsUserLabelDom);
            if (i == 1){
                bbsLabelDto.setArttentionStatus(CommonConstant.Concern_Status_one);
            }else {
                bbsLabelDto.setArttentionStatus(CommonConstant.Concern_Status_zero);

            }
        }
        Integer labelNum = bbsUserLabelService.selectCountsById(uId);
        pageResult.setPageNum(bbsUserLabelDom.getPageNum()/bbsUserLabelDom.getPageSize()+1);
        pageResult.setPageSize(bbsUserLabelDom.getPageSize());
        pageResult.setAllNum(labelNum);
        pageResult.setAllPage((labelNum+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }

    @PostMapping("/selectTypeLabel")
    @ApiOperation(value = "根据类型查询标签",notes = "type:typeId")
    public R selectTypeLabel(@RequestBody BbsLabelDto bbsLabelDto){
        System.out.println(bbsLabelService);
        List list =  bbsLabelService.selectTypeLabel(bbsLabelDto);
        return new R(list);
    }



    @GetMapping("/selectAllLabel")
    @ApiOperation(value = "查询所有标签",notes = "无参数")
    public R selectAllLabel(){
        System.out.println(bbsLabelService);
        return new R(bbsLabelService.selectAllLabel());
    }

    /**
     *
     * @param id
     * @param status 1是查找上级  2是查找下级  3是查找平级
     *  改动 1
     * @return
     */
    @ApiOperation(value = " 根据id查找上下级标签和平级标签",notes = "selectLabel/{标签id}/{1是查找上级  2是查找下级  3是查找平级}")
    @GetMapping("/selectLabel/{id}/{status}")
    public R selectLabel(@PathVariable Integer id ,@PathVariable Integer status){
        return  new R(bbsLabelService.selectLabel(id,status));
    }


    /**
     * 展开下一个节点 或者打开根节点
     */
    @ApiOperation(value = "展开下一个节点，或者打开根节点",notes = "openLabel/{id}")
    @GetMapping("/openLabel/{id}")
    public R openLabel(@PathVariable Integer id){
        return new R( bbsLabelService.openLabel(id));
    }




    /**
    * 通过ID查询标签基本详情
    *
    * @param bbsLabelDom
    * @return BbsLabel
    */
    @ApiOperation(value = "根据Id查询标签信息",notes = "例如:{\"id\":\"1\"},返回标签信息")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/getLabelById")
    public ResultVO<BbsLabelDto> getLabelById(@RequestBody BbsLabelDom bbsLabelDom, HttpServletRequest request) {
        Integer userId=UserUtils.getUserId(request);
//        Integer userId=1;
        bbsLabelDom.setCreateUserId(userId);
        return ResultVOUtil.success(bbsLabelService.getLabelById(bbsLabelDom));
    }

    /**
     * 通过ID查询标签的兄弟标签,按照type查询
     *
     * @param bbsLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "通过ID查询标签的兄弟标签",notes = "例如:{\"id\":\"1\"},返回标签列表信息")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/getBrotherLabelListByLabelId")
    public ResultVO<List<BbsLabelDto>> getBrotherLabelListByLabelId(@RequestBody BbsLabelDom bbsLabelDom) {
        return ResultVOUtil.success(bbsLabelService.getBrotherLabelListByLabelId(bbsLabelDom));
    }


    /**
     * 通过ID查询标签有多少人关注
     *
     * @param bbsLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "通过ID查询标签有多少人关注",notes = "例如:{\"id\":\"1\"},返回num")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/getattentionNumByLabelId")
    public ResultVO<Long> getattentionNumByLabelId(@RequestBody BbsLabelDom bbsLabelDom) {
        return ResultVOUtil.success(bbsLabelService.getattentionNumByLabelId(bbsLabelDom));
    }

    /**
     * 点击关注标签
     *
     * @param bbsLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "通过ID关注此标签",notes = "例如:{\"id\":\"1\"},返回true,false")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/addConcernByLabelId")
    public ResultVO<Boolean> addConcernByLabelId(@RequestBody BbsLabelDom bbsLabelDom,HttpServletRequest request) {
        Integer userId= UserUtils.getUserId(request);
        bbsLabelDom.setCreateUserId(userId);
        return ResultVOUtil.success(bbsLabelService.addConcernByLabelId(bbsLabelDom));
    }

    /**
     * 点击取消关注标签
     *
     * @param bbsLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "通过ID点击取消关注此标签",notes = "例如:{\"id\":\"1\"},返回true,false")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/delConcernByLabelId")
    public ResultVO<Boolean> delConcernByLabelId(@RequestBody BbsLabelDom bbsLabelDom,HttpServletRequest request) {
        Integer userId= UserUtils.getUserId(request);
        bbsLabelDom.setCreateUserId(userId);
        return ResultVOUtil.success(bbsLabelService.delConcernByLabelId(bbsLabelDom));
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
        return bbsLabelService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsLabel  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsLabel bbsLabel) {
        return new R<>(bbsLabelService.insert(bbsLabel));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    //@ApiOperation(value = "根据id逻辑删除")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsLabel bbsLabel = new BbsLabel();
        bbsLabel.setId(id);
        bbsLabel.setUpdateTime(new Date());
        bbsLabel.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(bbsLabelService.updateById(bbsLabel));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/delete/{id}")
    //@ApiOperation(value = "根据id真实删除")
    public R<Boolean> deleteT(@PathVariable Integer id) {
        BbsLabel bbsLabel = new BbsLabel();
        bbsLabel.setId(id);
        return new R<>(bbsLabelService.deleteById(bbsLabel));
    }

    /**
     * 编辑
     * @param  bbsLabel  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsLabel bbsLabel) {
        bbsLabel.setUpdateTime(new Date());
        return new R<>(bbsLabelService.updateById(bbsLabel));
    }


    /**
     * 通过ID查询标签下的帖子
     *
     * @param bbsPostsDto
     * @return BbsLabel
     */
    @ApiOperation(value = "根据Id查询标签下的所有帖子",notes = "例如:{\"labelId\":\"1\"},返回帖子信息")
    @ApiImplicitParam(name = "bbsPostsDto", value = "帖子信息", required = true, dataType = "BbsPostsDto")
    @PostMapping("/getPostsListByLabelId")
    public ResultVO<BbsPostsDto> getPostsListByLabelId(@RequestBody BbsPostsDto bbsPostsDto) {
        if (bbsPostsDto.getSort()==null)bbsPostsDto.setSort(1);//给一个排序初始值
        return ResultVOUtil.success(bbsLabelService.getPostsListByLabelId(bbsPostsDto));
    }


    /**
     * 查询最热标签
     *
     * @param
     * @return BbsLabel
     */
    @ApiOperation(value = "查询最热标签",notes = "返回标签信息")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/getHotestLabelList")
    public ResultVO<List<BbsLabelDto>> getHotestLabelList(@RequestBody BbsLabelDom bbsLabelDom, HttpServletRequest request) {
        Integer userId=UserUtils.getUserId(request);
        bbsLabelDom.setCreateUserId(userId);
        return ResultVOUtil.success(bbsLabelService.getHotestLabelList(bbsLabelDom));
    }


    /**
     * 通过分类ID查询分类下的标签
     *
     * @param bbsLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "通过分类ID查询分类下的标签",notes = "例如:{\"type\":\"1\"},返回标签列表信息")
    @ApiImplicitParam(name = "bbsLabelDom", value = "标签信息", required = true, dataType = "BbsLabelDom")
    @PostMapping("/getLabelListByTypeId")
    public ResultVO<List<BbsLabelDto>> getLabelListByTypeId(@RequestBody BbsLabelDom bbsLabelDom) {
        return ResultVOUtil.success(bbsLabelService.getLabelListByTypeId(bbsLabelDom));
    }

    /**
     * 推荐给当前登录人的标签
     *
     * @param
     * @return BbsLabel
     */
    @ApiOperation(value = "推荐给当前登录人的标签",notes = "返回标签列表信息")
    @PostMapping("/getLabelListByGroom")
    public ResultVO<List<BbsLabelDto>> getLabelListByGroom(HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        BbsLabelDom bbsLabelDom = new BbsLabelDom();
        bbsLabelDom.setCreateUserId(userId);
        return ResultVOUtil.success(bbsLabelService.getLabelListByGroom(bbsLabelDom));
    }


    /**
     *根据用户查询用户标签最多的标签信息
     *
     * @param
     * @return BbsLabel
     */
    @ApiOperation(value = "根据用户查询用户标签最多的标签信息",notes = "根据用户查询用户标签最多的标签信息和数量")
    @GetMapping("/selectMaxUserLabel/{userId}")
    public BbsLabelVO selectMaxUserLabel(@PathVariable ("userId")Integer userId) {
        return bbsLabelService.selectMaxUserLabel(userId);
    }


}
