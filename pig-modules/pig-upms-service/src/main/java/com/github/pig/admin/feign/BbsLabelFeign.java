package com.github.pig.admin.feign;

import com.github.pig.admin.feign.fallback.bbsLabelServiceFallbackImpl;
import com.github.pig.common.vo.BbsLabelVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pig-bbs-service", fallback = bbsLabelServiceFallbackImpl.class)
public interface BbsLabelFeign {

    /**
     * 根据用户Id查询用户标签最多信息和给标签数
     *
     */
    @GetMapping("/bbsLabel/selectMaxUserLabel/{userId}")
    BbsLabelVO getMaxUserLabel(@PathVariable("userId") Integer userId);
}
