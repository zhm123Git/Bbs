package com.github.pig.bbs.label.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.bbs.label.DOM.BbsArticleTypeDom;
import com.github.pig.bbs.label.DTO.BbsArticleTypeDto;
import com.github.pig.bbs.label.entity.BbsArticleType;
import com.github.pig.bbs.label.service.BbsArticleTypeService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.PageResult;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.util.ResultVO;
import com.github.pig.common.vo.BbsArticleTypeVO;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-12-04
 */
@Api(value = "bbsArticleType",description="分类")
@RestController
@RequestMapping("/bbsArticleType")
public class BbsArticleTypeController extends BaseController {
    @Autowired private BbsArticleTypeService bbsArticleTypeService;



    @ApiOperation(value = "获取分类列表" , notes = " id:id值  根据分类的id寻找下级id，id为null时查询所有顶级节点")
    @PostMapping("/selectType")
    public R selectType(@RequestBody BbsArticleTypeDto bbsArticleTypeDto){
        List list = bbsArticleTypeService.selectType(bbsArticleTypeDto);
        return  new R(list);
    }

    @ApiOperation(value = "关注分类" , notes = "id :分类id ")
    @PostMapping("/attentionType")
    public R attentionType(@RequestBody BbsArticleTypeDto bbsArticleTypeDto){
       bbsArticleTypeService.attentionType(bbsArticleTypeDto);
        return  new R();
    }
    @ApiOperation(value = "获取关注的分类列表" , notes = "获取关注的分类列表")
    @PostMapping("/selectAttentionType")
    public R selectAttentionType(@RequestBody BbsArticleTypeDto bbsArticleTypeDto){
       List list =  bbsArticleTypeService.selectAttentionType(bbsArticleTypeDto);
        return  new R(list);
    }



    /**
     * 根据用户查询收藏分类列表信息
     *
     * @param bbsArticleTypeDom
     * @return
     */
    @ApiOperation(value = "根据用户查询收藏分类列表信息",notes = "1")
    @ApiImplicitParam(name = "bbsArticleTypeDom", value = "用户帖子信息", required = true, dataType = "BbsArticleTypeDom")
    @PostMapping("/getEnshrineTypeList")
    public ResultVO getEnshrineTypeList(@RequestBody BbsArticleTypeDom bbsArticleTypeDom) {
        ResultVO r = new ResultVO();
        PageResult<BbsArticleTypeDto> pageResult = new PageResult<>();
        List<BbsArticleTypeDto> list = bbsArticleTypeService.getEnshrineType(bbsArticleTypeDom);
        Integer enshrineNum = bbsArticleTypeService.getEnshrineNum(bbsArticleTypeDom);
        pageResult.setPageNum(bbsArticleTypeDom.getPageNum()/bbsArticleTypeDom.getPageSize()+1);
        pageResult.setPageSize(bbsArticleTypeDom.getPageSize());
        pageResult.setAllNum(enshrineNum);
        pageResult.setAllPage((enshrineNum+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }









    /**
    * 通过ID查询
    *
    * @param id ID
    * @return BbsArticleType
    */
    @GetMapping("/{id}")
    public R<BbsArticleType> get(@PathVariable Integer id) {
        return new R<>(bbsArticleTypeService.selectById(id));
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
        return bbsArticleTypeService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     * @param  bbsArticleType  实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody BbsArticleType bbsArticleType) {
        return new R<>(bbsArticleTypeService.insert(bbsArticleType));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        BbsArticleType bbsArticleType = new BbsArticleType();
        bbsArticleType.setId(id);
        return new R<>(bbsArticleTypeService.updateById(bbsArticleType));
    }

    /**
     * 编辑
     * @param  bbsArticleType  实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody BbsArticleType bbsArticleType) {
        return new R<>(bbsArticleTypeService.updateById(bbsArticleType));
    }

    /**
     *根据用户查询用户分类最多的分类信息
     *
     * @param
     * @return BbsLabel
     */
    @ApiOperation(value = "根据用户查询用户分类最多的分类信息",notes = "根据用户查询用户分类最多的分类信息和数量")
    @GetMapping("/selectMaxUserType/{userId}")
    public BbsArticleTypeVO selectMaxUserType(@PathVariable ("userId") Integer userId) {
        return bbsArticleTypeService.selectMaxUserType(userId);
    }

}
