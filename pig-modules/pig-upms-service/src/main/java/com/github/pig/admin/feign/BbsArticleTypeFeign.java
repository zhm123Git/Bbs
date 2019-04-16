package com.github.pig.admin.feign;

import com.github.pig.admin.feign.fallback.bbsArticleTypeServiceFallbackImpl;
import com.github.pig.common.vo.BbsArticleTypeVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pig-bbs-service", fallback = bbsArticleTypeServiceFallbackImpl.class)
public interface BbsArticleTypeFeign {

    /**
     * 根据用户Id查询用户分类最多信息和给分类数
     *
     */
    @GetMapping("/bbsArticleType/selectMaxUserType/{userId}")
    BbsArticleTypeVO getMaxUserType(@PathVariable("userId") Integer userId);

}
