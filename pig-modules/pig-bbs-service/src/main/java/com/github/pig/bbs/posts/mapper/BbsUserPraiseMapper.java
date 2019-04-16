package com.github.pig.bbs.posts.mapper;

import com.github.pig.bbs.posts.entity.BbsUserPraise;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2018-11-30
 */
@Mapper
public interface BbsUserPraiseMapper extends BaseMapper<BbsUserPraise> {

/*
    Object selectPreisa(@Param("postsId") Integer postsId,
                        @Param("userSessionId") Integer userSessionId,
                        @Param("status") Integer status
                        );
*/

    Integer isPreisa(@Param("postsId") Integer postsId,
                    @Param("userSessionId") Integer userSessionId
                        );

    void insertPraise(@Param("postsId") Integer postsId,
                      @Param("userSessionId") Integer userSessionId,
                      @Param("status") Integer status);

    void updatePraise(@Param("postsId") Integer postsId,
                      @Param("userSessionId") Integer userSessionId,
                      @Param("status") Integer status);






    void deletePraise(@Param("postsId") Integer postsId,
                      @Param("userSessionId") Integer userSessionId);

    void deleteTrample(@Param("postsId") Integer postsId,
                       @Param("userSessionId") Integer userSessionId);


}
