/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.admin.model.dom.UserDom;
import com.github.pig.admin.model.dto.BbsUserConcernDto;
import com.github.pig.admin.model.dto.SysUserDto;
import com.github.pig.admin.model.entity.SysUser;
import com.github.pig.common.bean.interceptor.DataScope;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.BbsArticleTypeVO;
import com.github.pig.common.vo.BbsLabelVO;
import com.github.pig.common.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO selectUserVoByUsername(String username);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param query     查询条件
     * @param username  用户名
     * @param dataScope 数据权限
     * @return list
     */
    List selectUserVoPageDataScope(Query query, @Param("username") Object username, DataScope dataScope);

    /**
     * 通过手机号查询用户信息（含有角色信息）
     *
     * @param mobile 用户名
     * @return userVo
     */
    UserVO selectUserVoByMobile(String mobile);

    /**
     * 通过openId查询用户信息
     *
     * @param openId openid
     * @return userVo
     */
    UserVO selectUserVoByOpenId(String openId);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return userVo
     */
    UserVO selectUserVoById(Integer id);

    /**
     * 通过openId查询用户信息
     * @param openId
     * @return
     */
    SysUser selectUserByOpenId(String openId);

    Boolean updateWeiXinInfo(SysUser user);

    Boolean updateUserAllowFlag(SysUser user);

    Boolean updateUserForbideFlag(SysUser user);

    Boolean updateUserAmount(SysUser user);

    List<SysUser> findUserByNickname(SysUserDto sysUserDto);

    List<SysUser> selectUser(@Param("userDom") UserDom userDom);

    Long selectUserCount(@Param("userDom") UserDom userDom);
    //用户积分排行
    List<SysUser> selectUsersByAmount(@Param("userDom") UserDom userDom);
    Long selectUserByAmountCount(@Param("userDom") UserDom userDom);
    //按照发帖数排行
    List<SysUserDto> findUserRankByPost(@Param("userDom") UserDom userDom);

    Long findUserRankByPostCount(@Param("userDom") UserDom userDom);
    //按照回帖数排行
    List<SysUserDto> findUserRankByReply(@Param("userDom") UserDom userDom);

    Long findUserRankByReplyCount(@Param("userDom") UserDom userDom);
    //按照打赏次数排行
    List<SysUserDto> findUserRankByAward(@Param("userDom") UserDom userDom);

    Long findUserRankByAwardCount(@Param("userDom") UserDom userDom);
    //按照采纳数排行
    List<SysUserDto> findUserRankByReplyAccept(@Param("userDom") UserDom userDom);

    Long findUserRankByReplyAcceptCount(@Param("userDom") UserDom userDom);

    List<SysUserDto> findUserRankByBrowsePost(@Param("userDom") UserDom userDom);

    Long findUserRankByBrowsePostCount(@Param("userDom") UserDom userDom);

    Boolean updateUserPersonSignatureName(SysUser user);

    List<SysUserDto> findConcernUserListByUser(@Param("userDom") UserDom userDom);

    Long findConcernUserListByUserCount(@Param("userDom") UserDom userDom);

    Long findConcernedCount(@Param("U") UserDom userDom);

    List<SysUserDto> findUserByWeight(@Param("userDom") UserDom userDom);

    Long findUserByWeightCount();

    Boolean addConcern(@Param("bbsUserConcernDto") BbsUserConcernDto bbsUserConcernDto);

    Boolean delConcern(@Param("bbsUserConcernDto") BbsUserConcernDto bbsUserConcernDto);

    void insertUserAndAuth(@Param("userId")Integer userId,@Param("roleId") Integer roleId);

    Long selectConcernNum(@Param("id") Integer id);

    Long selectLabelNum(@Param("id") Integer id);


    List<SysUserDto> findUserByScore(@Param("userDom") UserDom userDom);

    Long findUserByScoreCount();

    List<SysUserDto> findUserByConsum(@Param("userDom") UserDom userDom);


    List<SysUserDto> selectConcernInfo(@Param("userDom") UserDom userDom);

    List<SysUserDto> selectFindConcernedInfo(@Param("userDom") UserDom userDom);

    int selectConcernStatus(@Param("buc") BbsUserConcernDto buc);

    List<SysUserDto> getInterestingUsers(UserDom userDom);

    Integer insertConcerns(@Param("list") List<BbsUserConcernDto> list);

    BbsLabelVO selectMaxUserLabel(@Param("userId") Integer userId);

    BbsArticleTypeVO selectMaxvUserType(@Param("userId")Integer userId);
}