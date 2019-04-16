package com.github.pig.bbs.posts.DOM;

import com.github.pig.bbs.posts.entity.BbsAward;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Data
public class BbsAwardDom extends BbsAward {
    private Integer createPostUserId;  //发帖人

}
