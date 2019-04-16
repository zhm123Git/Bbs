package com.github.pig.admin.feign.fallback;

import com.github.pig.admin.feign.BbsLabelFeign;
import com.github.pig.common.vo.BbsLabelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class bbsLabelServiceFallbackImpl implements BbsLabelFeign {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BbsLabelVO getMaxUserLabel(Integer userId) {
        return null;
    }
}
