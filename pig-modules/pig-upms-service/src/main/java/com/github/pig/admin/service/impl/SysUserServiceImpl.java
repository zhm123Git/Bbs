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

package com.github.pig.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.common.UserException;
import com.github.pig.admin.common.util.BeanCopierUtils;
import com.github.pig.admin.common.util.HttpRequest;
import com.github.pig.admin.feign.BbsArticleTypeFeign;
import com.github.pig.admin.feign.BbsLabelFeign;
import com.github.pig.admin.mapper.SysUserMapper;
import com.github.pig.admin.model.Userinfo;
import com.github.pig.admin.model.WeiXinProperties;
import com.github.pig.admin.model.dom.UserDom;
import com.github.pig.admin.model.dto.BbsUserConcernDto;
import com.github.pig.admin.model.dto.SysUserDto;
import com.github.pig.admin.model.dto.UserDTO;
import com.github.pig.admin.model.dto.UserInfo;
import com.github.pig.admin.model.entity.SysDeptRelation;
import com.github.pig.admin.model.entity.SysUser;
import com.github.pig.admin.model.entity.SysUserRole;
import com.github.pig.admin.service.SysDeptRelationService;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.admin.service.SysUserService;
import com.github.pig.common.bean.interceptor.DataScope;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.constant.enums.EnumSmsChannelTemplate;
import com.github.pig.common.util.*;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.common.vo.*;
import com.google.common.collect.ImmutableMap;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final SysMenuService sysMenuService;
    private final RedisTemplate redisTemplate;
    private final SysUserMapper sysUserMapper;
    private final RabbitTemplate rabbitTemplate;
    private final SysUserRoleService sysUserRoleService;
    private final SysDeptRelationService sysDeptRelationService;
    @Autowired
    private WeiXinProperties weiXinProperties;
    @Autowired
    private BbsArticleTypeFeign bbsArticleTypeFeign;
    @Autowired
    private BbsLabelFeign bbsLabelFeign;


    @Override
    public UserInfo findUserInfo(UserVO userVo) {

        SysUser condition = new SysUser();
        condition.setUsername(userVo.getUsername());
        SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));

        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        List<String> roleCodes = userVo.getRoleList().stream()
                .filter(sysRole -> !StrUtil.equals(SecurityConstants.BASE_ROLE, sysRole.getRoleCode()))
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
        String[] roles = ArrayUtil.toArray(roleCodes, String.class);
        userInfo.setRoles(roles);

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        Arrays.stream(roles).forEach(role -> {
            List<MenuVO> menuVos = sysMenuService.findMenuByRoleName(role);
            List<String> permissionList = menuVos.stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission).collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    @Override
    @Cacheable(value = "user_details", key = "#username")
    public UserVO findUserByUsername(String username) {
        return sysUserMapper.selectUserVoByUsername(username);
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param
     * @return 用户信息
     */
    @Override
    // @Cacheable(value = "user_details_mobile", key = "#map.code")
    public UserVO findUserByMobileOrCode(Map map) {
        UserVO userVO = sysUserMapper.selectUserVoByMobile("17034633333");
        return userVO;
    }

    /**
     * 通过openId查询用户
     *
     * @param openId openId
     * @return 用户信息
     */
    @Override
  //  @Cacheable(value = "user_details_openid", key = "#openId")
    public UserVO findUserByOpenId(String openId) {
        return sysUserMapper.selectUserVoByOpenId(openId);
    }

    @Override
    public Page selectWithRolePage(Query query, UserVO userVO) {
        DataScope dataScope = new DataScope();
        dataScope.setScopeName("deptId");
        dataScope.setIsOnly(true);
        dataScope.setDeptIds(getChildDepts(userVO));
        Object username = query.getCondition().get("username");
        query.setRecords(sysUserMapper.selectUserVoPageDataScope(query, username, dataScope));
        return query;
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return sysUserMapper.selectUserVoById(id);
    }

    /**
     * 1.首先通过缓存获取,设置缓存时间5分钟
     * 2.没有再从数据库获取
     * 3.将用户写入缓存,生成token
     * 4.返回对象
     * @param openId
     * @return
     */
    @Override
    public SysUser selectUserByOpenId(String openId) {
        SysUser user = sysUserMapper.selectUserByOpenId(openId);//? 这里是数据库
        if(user!=null){
            onLogin(user);
            return user;
        }else{
            return null;
        }


    }
    @Override
    public void onLogin(SysUser user) {
        //token里放三个字段,1:微信openID,2:昵称,3:时间戳
        String token =  JwtHelper.genToken(ImmutableMap.of("openId", user.getOpenId(), "nickName", user.getNickName(),"ts", Instant.now().getEpochSecond()+""));
        renewToken(token,user.getOpenId());
       // user.setToken(token);
        //把用户信息放入redis缓存
        String key="user:"+user.getUserId();
        String json = JSON.toJSONString(user);
        redisTemplate.opsForValue().set(key,json);//放入redis缓存中
        redisTemplate.expire(key,5,TimeUnit.MINUTES);//过期时间为5分钟
    }
    private String renewToken(String token, String openId) {
        redisTemplate.opsForValue().set(openId, token);
        redisTemplate.expire(openId, 30, TimeUnit.MINUTES);//放到redis里,设置失效时间30分钟
        return token;
    }
    @Override
    public Boolean updateWeiXinInfo(Userinfo userinfo) {
        SysUser user = selectUserByOpenId(userinfo.getOpenId());
        BeanUtils.copyProperties(userinfo, user);
        user.setUpdateTime(new Date());
        //    onLogin(user);
        return sysUserMapper.updateWeiXinInfo(user);
    }

    @Override
    public Boolean updateUserAllowFlag(SysUserDto sysUserDto) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUserDto,user);
        return sysUserMapper.updateUserAllowFlag(user);
    }

    @Override
    public Boolean updateUserForbideFlag(SysUserDto sysUserDto) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUserDto,user);
        return sysUserMapper.updateUserForbideFlag(user);
    }

    @Override
    public Boolean updateUserAmount(SysUser sysUser) {
        SysUser user = sysUserMapper.selectById(sysUser.getUserId());
        int finalAmount=user.getAmount()+sysUser.getAmount();
        if(finalAmount<0){
            throw new  RuntimeException("没有足够积分");
        }
        return sysUserMapper.updateUserAmount(sysUser);
    }

    @Override
    public R<List<SysUserDto>> findUserByNickname(SysUserDto sysUserDto) {
        List<SysUser> sysUsers = sysUserMapper.findUserByNickname(sysUserDto);
        List<SysUserDto> userVOList =BeanCopierUtils.copyList(sysUsers,SysUserDto.class);
        return new R<>(userVOList);
    }

    @Override
    public SysUserDto getLoginedUserByToken(String token) {
        Map<String, String> map = null;
        try {
            map = JwtHelper.verifyToken(token);
        } catch (Exception e) {
            throw new UserException(UserException.Type.USER_NOT_LOGIN,"User not login");
        }
        String openId =  map.get("openId");
        Long expired = redisTemplate.getExpire(openId);
        if (expired > 0L) {
            renewToken(token, openId);
            SysUser user = getUserByOpenId(openId);
            SysUserDto userDto = new SysUserDto();
            BeanCopierUtils.copy(user,userDto);
            userDto.setToken(token);
            return userDto;
        }
        throw new UserException(UserException.Type.USER_NOT_LOGIN,"user not login");
    }

    @Override
    public void invalidate(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("openId"));
    }

    @Override
    public ListResponse<UserDTO> getAllUsers(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUser> users =  sysUserMapper.selectUser(userDom);
        Long count  =  sysUserMapper.selectUserCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public  ListResponse<UserDTO> getAllUsersByAmount(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUser> users =  sysUserMapper.selectUsersByAmount(userDom);
        Long count  =  sysUserMapper.selectUserByAmountCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserRankByPost(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserRankByPost(userDom);
        Long count  =  sysUserMapper.findUserRankByPostCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserRankByReply(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserRankByReply(userDom);
        Long count  =  sysUserMapper.findUserRankByReplyCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserRankByAward(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserRankByAward(userDom);
        Long count  =  sysUserMapper.findUserRankByAwardCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserRankByReplyAccept(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserRankByReplyAccept(userDom);
        Long count  =  sysUserMapper.findUserRankByReplyAcceptCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserRankByBrowsePost(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserRankByBrowsePost(userDom);
        Long count  =  sysUserMapper.findUserRankByBrowsePostCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public Boolean updateUserPersonSignatureName(SysUser user) {
        String personSignatureName = user.getPersonSignatureName();
        if(StringUtils.isNotEmpty(personSignatureName)){
            if(personSignatureName.length()>150){
                throw new RuntimeException("个性签名最长不能超过150个字符");
            }
        }
        return sysUserMapper.updateUserPersonSignatureName(user);
    }

    @Override
    public String getSessionKeyOropenid(String code) {
        //小程序唯一标识   (在微信小程序管理后台获取)
        String wxspAppid = weiXinProperties.getAppid();
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = weiXinProperties.getSecret();
        //授权（必填）
        String grant_type = weiXinProperties.getGrantType();
        //1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(sr);
        //获取会话密钥（session_key）
        String session_key = "";
        if(json.get("session_key")!=null){
            session_key =  json.get("session_key").toString();
        }
        //用户的唯一标识（openid）
        String openid = "";
          if(json.get("openid")!=null){
              openid =  (String) json.get("openid");
          }
        return openid;
    }

    @Override
    public ListResponse<UserDTO> findConcernUserListByUser(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findConcernUserListByUser(userDom);
        Long count  =  sysUserMapper.findConcernUserListByUserCount(userDom);
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public Long findConcernedCount(UserDom userDom) {
        return sysUserMapper.findConcernedCount(userDom);
    }

    @Override
    public ListResponse<UserDTO> findUserByWeight(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserByWeight(userDom);
        Integer userId =userDom.getUserId();
        //当前登录用户关注的所有用户
        List<SysUserDto> sysUserDtos = sysUserMapper.selectConcernInfo(userDom);
        for(int i=0; i<users.size();i++){
            boolean flag=false;
            for(int m=0; m<sysUserDtos.size();m++){
                if(users.get(i).getUserId().equals(sysUserDtos.get(m).getUserId())){
                    flag=true;
                }
            }
            if(flag){
                users.get(i).setConcernStatus(1);
            }else{
                users.get(i).setConcernStatus(0);
            }
        }
        Long count  =  sysUserMapper.findUserByWeightCount();
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public Boolean addConcern(BbsUserConcernDto bbsUserConcernDto) {
        return sysUserMapper.addConcern(bbsUserConcernDto);
    }

    @Override
    public Boolean delConcern(BbsUserConcernDto bbsUserConcernDto) {
        return sysUserMapper.delConcern(bbsUserConcernDto);
    }

    @Override
    public void insetUserAndAuth(Integer userId, Integer role) {
        sysUserMapper.insertUserAndAuth(userId,role);
    }



    private SysUser getUserByOpenId(String openId) {
        return  sysUserMapper.selectUserByOpenId(openId);
    }


    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param randomStr 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String randomStr, String imageCode) {
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }

        SysUser params = new SysUser();
        params.setPhone(mobile);
        List<SysUser> userList = this.selectList(new EntityWrapper<>(params));

        if (CollectionUtil.isEmpty(userList)) {
            log.error("根据用户手机号{}查询用户为空", mobile);
            return new R<>(false, "手机号不存在");
        }

        String code = RandomUtil.randomNumbers(4);
        JSONObject contextJson = new JSONObject();
        contextJson.put("code", code);
        contextJson.put("product", "Pig4Cloud");
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_CODE_QUEUE,
                new MobileMsgTemplate(
                        mobile,
                        contextJson.toJSONString(),
                        CommonConstant.ALIYUN_SMS,
                        EnumSmsChannelTemplate.LOGIN_NAME_LOGIN.getSignName(),
                        EnumSmsChannelTemplate.LOGIN_NAME_LOGIN.getTemplate()
                ));
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        return new R<>(true);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.deleteById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")//清除缓存
    public R<Boolean> updateUserInfo(UserDTO userDto, String username) {
        UserVO userVo = this.findUserByUsername(username);
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getPassword())
                && StrUtil.isNotBlank(userDto.getNewpassword1())) {
            if (ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
                sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
            } else {
                log.warn("原密码错误，修改密码失败:{}", username);
                return new R<>(Boolean.FALSE, "原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVo.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return new R<>(this.updateById(sysUser));
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateUser(UserDTO userDto, String username) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(new Date());
        this.updateById(sysUser);

        SysUserRole condition = new SysUserRole();
        condition.setUserId(userDto.getUserId());
        sysUserRoleService.delete(new EntityWrapper<>(condition));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * 获取当前用户的子部门信息
     *
     * @param userVO 用户信息
     * @return 子部门列表
     */
    private List<Integer> getChildDepts(UserVO userVO) {
        UserVO userVo = findUserByUsername(userVO.getUsername());
        Integer deptId = userVo.getDeptId();

        //获取当前部门的子部门
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setAncestor(deptId);
        List<SysDeptRelation> deptRelationList = sysDeptRelationService.selectList(new EntityWrapper<>(deptRelation));
        List<Integer> deptIds = new ArrayList<>();
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            deptIds.add(sysDeptRelation.getDescendant());
        }
        return deptIds;
    }

    @Override
    public Long getConcernNum(Integer id) {
        return sysUserMapper.selectConcernNum(id);
    }

    @Override
    public Long getLabelNum(Integer id) {
        return sysUserMapper.selectLabelNum(id);
    }

    @Override
    public ListResponse<UserDTO> findUserByScore(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserByScore(userDom);
        Long count  =  sysUserMapper.findUserByScoreCount();
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        for (SysUserDto user :users ){
            Integer userId= user.getUserId();
            BbsArticleTypeVO bbsArticleTypeVO = sysUserMapper.selectMaxvUserType(userId);
            if (bbsArticleTypeVO !=  null) {
                user.setMaxUserType(bbsArticleTypeVO.getName());
                user.setMaxUserTypeNum(bbsArticleTypeVO.getArticleTypeNum());
            }
            BbsLabelVO bbsLabelVO = sysUserMapper.selectMaxUserLabel(userId);
            if (bbsLabelVO != null){
                user.setMaxUserLabel(bbsLabelVO.getName());
                user.setMaxUserLabelNum(bbsLabelVO.getUserLabelNum());
            }
        }
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;
    }

    @Override
    public ListResponse<UserDTO> findUserByConsum(UserDom userDom) {
        Integer currentPage=userDom.getCurrentPage();
        Integer pageSize = userDom.getPageSize();
        if(currentPage !=null && pageSize!=null && (currentPage>0 && pageSize>0)){
            userDom.setCurrentPage((currentPage-1)*pageSize);
            userDom.setPageSize(pageSize);
        }else {
            userDom.setCurrentPage(0);
            userDom.setPageSize(10);
        }
        List<SysUserDto> users =  sysUserMapper.findUserByConsum(userDom);
        Long count  =  sysUserMapper.findUserByScoreCount();
        ListResponse<UserDTO> userDTOListResponse = new ListResponse<>();
        List<UserDTO> userDTOs = BeanCopierUtils.copyList(users, UserDTO.class);
        ListResponseUtil.build(userDTOListResponse,userDTOs,count);
        return userDTOListResponse;

    }

    @Override
    public List<SysUserDto> getConcernInfo(UserDom userDom) {
        if (null == userDom.getPageSize() || userDom.getPageSize()<= 0 || userDom.getPageSize()>50)
            userDom.setPageSize(10);
        if (null == userDom.getCurrentPage() || userDom.getCurrentPage() <= 0) {
            userDom.setCurrentPage(0);
        }else {
            userDom.setCurrentPage((userDom.getCurrentPage()-1)*userDom.getPageSize());
        }
        return sysUserMapper.selectConcernInfo(userDom);
    }

    @Override
    public List<SysUserDto> findConcernedInfo(UserDom userDom) {
        if (null == userDom.getPageSize() || userDom.getPageSize()<= 0 || userDom.getPageSize()>50)
            userDom.setPageSize(10);
        if (null == userDom.getCurrentPage() || userDom.getCurrentPage() <= 0) {
            userDom.setCurrentPage(0);
        }else {
            userDom.setCurrentPage((userDom.getCurrentPage()-1)*userDom.getPageSize());
        }

        return sysUserMapper.selectFindConcernedInfo(userDom);
    }

    @Override
    public int getConcernStatus(BbsUserConcernDto buc) {
        return sysUserMapper.selectConcernStatus(buc);
    }

    @Override
    public List<SysUserDto> getInterestingUsers(UserDom userDom) {
        return sysUserMapper.getInterestingUsers(userDom);
    }

    @Override
    public Integer addConcerns(List<BbsUserConcernDto> bbsUserConcernDto) {
        return sysUserMapper.insertConcerns(bbsUserConcernDto);
    }

}
