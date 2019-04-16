package com.github.pig.bbs.label.controller;

import com.github.pig.bbs.label.DOM.BbsUserLabelDom;
import com.github.pig.bbs.label.DTO.BbsUserLabelDto;
import com.github.pig.bbs.label.entity.BbsUserLabel;
import com.github.pig.bbs.label.service.BbsUserLabelService;
import com.github.pig.bbs.posts.service.impl.BbsPostsLabelServiceImpl;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.R;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Desc:
 * ClassName:BbsUserLabel
 * Authot:ZhangHongMeng
 * Date:2018/12/13 10:55
 * project: (pig)
 */
@Api(value = "/bbsUserLabel",description="用户与标签")
@RestController
@RequestMapping("/bbsUserLabel")
public class BbsUserLabelController extends BaseController {

    private final static Logger log= LoggerFactory.getLogger(BbsPostsLabelServiceImpl.class);

    @Autowired
    private BbsUserLabelService bbsUserLabelService;



    /**
     * 通过用户ID查询表签数 或 通过用户签ID查询用户数
     *
     * @param  bbsUserLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "根据Id查询标签数或用户数",notes = "返回标签数")
    @ApiImplicitParam(name = "bbsUserLabelDom", value = "返回标签数", required = true, dataType = "BbsUserLabelDom")
    @PostMapping("/getCounts")
    public R getCounts(@RequestBody BbsUserLabelDom bbsUserLabelDom) {
        R<Object> r = new R<>();
        Long i = bbsUserLabelService.selectCounts(bbsUserLabelDom);
        r.setData(i);
        return r;
    }

    /**
     * 通过用户ID查询表签ids
     *
     * @param  bbsUserLabelDom
     * @return BbsLabel
     */
    @ApiOperation(value = "根据Id查询标签ids",notes = "返回标签")
    @ApiImplicitParam(name = "bbsUserLabelDom", value = "根据Id查询标签ids", required = true, dataType = "BbsUserLabelDom")
    @PostMapping("/getLabelIds")
    public R<List<BbsUserLabelDto>> getLabelIds(@RequestBody BbsUserLabelDom bbsUserLabelDom) {
        R<List<BbsUserLabelDto>> r = new R<List<BbsUserLabelDto>>();
        List<BbsUserLabelDto> bul = bbsUserLabelService.getBcyID(bbsUserLabelDom);
        r.setData(bul);
        return r;
    }


    /**
     * @Description :用户关注或取消标签
     * @Param bbsUserLabelDom
     * @return R
     * @Author : zhanghongmeng
     * @Date 15:49 2018/12/17
     **/
    @ApiOperation(value = "用户关注或取消标签")
    @ApiImplicitParam(name = "bbsUserLabelDom", value = "用户关注标签", required = true, dataType = "BbsUserLabelDom")
    @PostMapping("/addOrDelete")
    public R<Integer> addOrDelete(@RequestBody BbsUserLabelDom bbsUserLabelDom , HttpServletRequest request) {
        R<Integer> r = new R<>();
        Integer userId = UserUtils.getUserId(request);
//        Integer userId = 9;
        BbsUserLabel bbsUserLabel = new BbsUserLabel();
        if(bbsUserLabelDom.getConcernLabelStatus() == 0){
            bbsUserLabelDom.setUserId(userId);
            BeanUtils.copyProperties(bbsUserLabelDom ,bbsUserLabel);
            int i = bbsUserLabelService.getLabelStatus(bbsUserLabelDom);
            if (i >= 1){
                r.setData(CommonConstant.Concern_Status_one);
            }else {
                boolean insert = bbsUserLabelService.addLabel(bbsUserLabel);
                if (insert == true)
                    r.setData(CommonConstant.Concern_Status_one);
            }
        }else if(bbsUserLabelDom.getConcernLabelStatus() == 1) {
            bbsUserLabelDom.setUserId(userId);
            BeanUtils.copyProperties(bbsUserLabelDom, bbsUserLabel);
            boolean delete = bbsUserLabelService.deleteLabel(bbsUserLabel);
            if (delete == true)
                r.setData(CommonConstant.Concern_Status_zero);
        }
        return r;
    }


}
