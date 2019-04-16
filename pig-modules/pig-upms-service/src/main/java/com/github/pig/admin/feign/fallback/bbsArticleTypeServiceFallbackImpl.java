package com.github.pig.admin.feign.fallback;

import com.github.pig.admin.feign.BbsArticleTypeFeign;
import com.github.pig.common.vo.BbsArticleTypeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class bbsArticleTypeServiceFallbackImpl implements BbsArticleTypeFeign {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public BbsArticleTypeVO getMaxUserType(Integer userId) {
        return null;
    }
}

