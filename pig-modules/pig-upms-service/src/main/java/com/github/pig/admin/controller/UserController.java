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

package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.common.util.BeanCopierUtils;
import com.github.pig.admin.model.Userinfo;
import com.github.pig.admin.model.WeiXinProperties;
import com.github.pig.admin.model.dom.UserDom;
import com.github.pig.admin.model.dto.BbsUserConcernDto;
import com.github.pig.admin.model.dto.SysUserDto;
import com.github.pig.admin.model.dto.UserDTO;
import com.github.pig.admin.model.dto.UserInfo;
import com.github.pig.admin.model.entity.BbsConcern;
import com.github.pig.admin.model.entity.SysUser;
import com.github.pig.admin.model.entity.SysUserRole;
import com.github.pig.admin.service.IBbsConcernService;
import com.github.pig.admin.service.SysUserService;
import com.github.pig.common.bean.config.FdfsPropertiesConfig;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.*;
import com.github.pig.common.vo.UserVO;
import com.github.pig.common.web.BaseController;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.xiaoleilu.hutool.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@Api(value = "用户",description = "用户")
@RestController
@RequestMapping("/user")
@EnableConfigurationProperties(WeiXinProperties.class)
public class UserController extends BaseController {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private SysUserService userService;
    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;

    @Autowired
    private WeiXinProperties weiXinProperties;
    @Autowired
    private IBbsConcernService iBbsConcernService;

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String MOBILE_NICK_NAME = "nickName";//名字
    public static final String MOBILE_AVATAR_URL = "avatarUrl";//头像地址
    public static final String MOBILE_CITY= "city";//城市
    public static final String MOBILE_CODE= "code";//code
    public static final String MOBILE_COUNTRY= "country";//国家
    public static final String MOBILE_GENDER= "gender";//性别
    public static final String MOBILE_LANGUAGE= "language";//语言
    public static final String MOBILE_PROVINCE= "province";//城市

//    @Autowired
//    private BbsPointHistoryService bbsPointHistoryService;


    @GetMapping("test/hello")
    public String test(){
        return "test pig";
    }
    /**
     * 获取当前用户信息（角色、权限）
     * 并且异步初始化用户部门信息
     *
     * @param userVo 当前用户信息
     * @return 用户名
     */
    @GetMapping("/info")
    public R<UserInfo> user(UserVO userVo) {
        UserInfo userInfo = userService.findUserInfo(userVo);
        return new R<>(userInfo);
    }



    /**
     * 获取当前用户信息
     *
     *
     * @param userDom 当前用户信息
     * @return 用户名
     */
    @ApiOperation(value = "根据openId获取当前用户信息",notes = "例如:{\n" +
            "\"  \\\"openId\\\": \\\"123456\\\"\\n\" +\n" +
            "\"},返回当前用户信息")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/selectUserByOpenId")
    public ResultVO<UserDTO> selectUserByOpenId(@RequestBody UserDom userDom) {
        if(!StringUtils.isNotEmpty(userDom.getOpenId())){
            throw new RuntimeException("用户的openID不能为空");
        }
        SysUser sysUser = userService.selectUserByOpenId(userDom.getOpenId());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(sysUser,userDTO);
        return ResultVOUtil.success(userDTO);
    }

    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public UserVO user(@PathVariable Integer id) {
        return userService.selectUserVoById(id);
    }

    /**
     * 查询用户信息
     *
     * @param
     * @return 用户信息
     */
    @PostMapping("/getUser")
    @ApiOperation(value = "获取用户详情",notes = "返回用户信息")
    @ApiImplicitParam(name = "userDom", value = "查询用户详情", required = true, dataType = "UserDom")
    public R<SysUserDto> getUser(@RequestBody UserDom userDom ,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
 //       Integer userId = 5;  //测试数据
        Integer uId = userDom.getUserId();
        R<SysUserDto> r = new R<SysUserDto>();
        BbsUserConcernDto buc = new BbsUserConcernDto();
        buc.setConcernId(userId);
        buc.setConcernedId(uId);
        SysUser user= new SysUser();
        SysUserDto userDtO = new SysUserDto();
        user.setUserId(userDom.getUserId());
        SysUser users = userService.selectById(user);
        BeanUtils.copyProperties(users,userDtO);
        int i = userService.getConcernStatus(buc);
        if (i == 1){
            userDtO.setConcernStatus(CommonConstant.Concern_Status_one);
        }else {
            userDtO.setConcernStatus(CommonConstant.Concern_Status_zero);
        }
        userDtO.setIsme((userId ==uId));
        r.setData(userDtO);
        return r;
    }


    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
//    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
//    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int")
    @DeleteMapping("/{id}")
    public R<Boolean> userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.selectById(id);
        return new R<>(userService.deleteUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    public R<Boolean> user(@RequestBody UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        userService.insert(sysUser);

        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return new R<>(Boolean.TRUE);
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @PutMapping
    public R<Boolean> userUpdate(@RequestBody UserDTO userDto) {
        SysUser user = userService.selectById(userDto.getUserId());
        return new R<>(userService.updateUser(userDto, user.getUsername()));
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    @GetMapping("/findUserByUsername/{username}")
    public UserVO findUserByUsername(@PathVariable String username) {

        return userService.findUserByUsername(username);
    }

    /**
     * 通过手机号查询用户及其角色信息
     *
     * @param
     * @return UseVo 对象
     */
    @PostMapping("/findUserByMobileOrCode")
    public UserVO findUserByMobileOrCode(@RequestBody Map map) {
        logger.error("进入fegin");
        Userinfo userInfo =  obtaUserInfo(map);//将Map转为userinfo
        String openId =  getStringObjectMap(userInfo);//获取openId  同时根据前台的微信信息创建用户  同时授权 若用户存在则更新数据 否则创建用户并授权
        //   UserVO u=  userService.findUserByMobileOrCode(null);

        UserVO userVO= userService.findUserByOpenId(openId);
        return userVO;//返回一个用户的角色
    }



    private String getStringObjectMap(Userinfo userinfo) {
        if(StringUtils.isEmpty(userinfo.getCode())){
            throw new RuntimeException("微信用户code值不能为空");
        }
        Map<String ,Object> map = new HashMap<String,Object>();
//        String  openId =     userinfo.getCode();//测试 暂时搞成这样
        String  openId = userService.getSessionKeyOropenid(userinfo.getCode());//获取openId

        userinfo.setOpenId(openId);
        SysUser user = userService.selectUserByOpenId(openId);
        if(user!=null){
            if(CommonConstant.STATUS_NORMAL.equals(user.getForbideFlag())){
                //更新user数据
                userService.updateWeiXinInfo(userinfo);
                BeanUtils.copyProperties(user,userinfo);
                map.put("status","0");//更新
            }else{
                map.put("userInfo",null);
                return null;
            }
            return openId;
        }else{
            //添加数据
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(userinfo, sysUser);
            sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
            sysUser.setAllowFlag(CommonConstant.STATUS_NORMAL);
            sysUser.setForbideFlag(CommonConstant.STATUS_NORMAL);
            sysUser.setAmount(0);//新建用户积分为0
            sysUser.setUsername(UUID.randomUUID().toString().replaceAll("-", "") );
            sysUser.setPassword(UUID.randomUUID().toString().replaceAll("-", "") );

            userService.insert(sysUser);


            //给一个基础角色
            // 暂时给一个 1  管理员角色

            Integer role = 1;
            userService.insetUserAndAuth(sysUser.getUserId(),role);
            // userService.onLogin(sysUser);
            BeanUtils.copyProperties(sysUser,userinfo);
            map.put("status","1");//新增
        }
        map.put("userInfo",userinfo);
        return openId;
    }



    private Userinfo obtaUserInfo(Map ma ){
        Userinfo user = new Userinfo();
        if(ma.get(MOBILE_CODE)!=null) user.setCode((String) ma.get(MOBILE_CODE));//code
        if(ma.get(MOBILE_NICK_NAME)!=null)user.setNickName((String) ma.get(MOBILE_NICK_NAME));//名字
        if(ma.get(MOBILE_AVATAR_URL)!=null)user.setAvatarUrl((String) ma.get( MOBILE_AVATAR_URL));//头像
        if(ma.get(MOBILE_CITY)!=null)user.setCity((String) ma.get( MOBILE_CITY));//城市
        if(ma.get(MOBILE_COUNTRY )!=null)user.setCountry( (String)ma.get( MOBILE_COUNTRY ) );//国家
        if(ma.get(MOBILE_GENDER)!=null)user.setGender(  Integer.parseInt(ma.get( MOBILE_GENDER).toString()) );//性别
        if(ma.get(MOBILE_LANGUAGE)!=null)user.setLanguage((String) ma.get( MOBILE_LANGUAGE));//语言
        if(ma.get(MOBILE_PROVINCE)!=null)user.setProvince((String) ma.get( MOBILE_PROVINCE));//语言
        return user;
    }



    /**
     * 通过OpenId查询
     *
     * @param openId openid
     * @return 对象
     */
    @GetMapping("/findUserByOpenId/{openId}")
    public UserVO findUserByOpenId(@PathVariable String openId) {
        return userService.findUserByOpenId(openId);
    }

    /**
     * 分页查询用户
     *
     * @param params 参数集
     * @param userVO 用户信息
     * @return 用户集合
     */
    @RequestMapping("/userPage")
    public Page userPage(@RequestParam Map<String, Object> params, UserVO userVO) {
        return userService.selectWithRolePage(new Query(params),userVO);
    }

    /**
     * 上传用户头像
     * (多机部署有问题，建议使用独立的文件服务器)
     *
     * @param file 资源
     * @return filename map
     */
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String fileExt = FileUtil.extName(file.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(1);
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(), fileExt);
            resultMap.put("filename", fdfsPropertiesConfig.getFileHost() + storePath.getFullPath());
        } catch (IOException e) {
            logger.error("文件上传异常", e);
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @param userVo  登录用户信息
     * @return success/false
     */
    @PutMapping("/editInfo")
    public R<Boolean> editInfo(@RequestBody UserDTO userDto, UserVO userVo) {
        return userService.updateUserInfo(userDto, userVo.getUsername());
    }

    /**
     * 微信授权登录验证  不使用
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信授权登录验证",notes = "例如:{" +
            "\"  \\\"avatarUrl\\\": \\\"trtrtrtttettttttttttttttt\\\",\\\"" +
            "\"  \\\"city\\\": \\\"朝阳区\\\",\\\" +" +
            "\"  \\\"code\\\": \\\"最新获取的code\\\",\\\"" +
            "\"  \\\"country\\\": \\\"china\\\",\\\"" +
            "\"  \\\"gender\\\": 0,\\\"" +
            "\"  \\\"language\\\": \\\"zh_CN\\\"\\\"" +
            "\"}返回userinfo,status")
    @ApiImplicitParam(name = "userinfo", value = "微信用户信息", required = true, dataType = "Userinfo")
    @PostMapping("/selectUserLogin")
    public R  selectUserLogin(@RequestBody Userinfo userinfo) throws Exception{
        return null;// getStringObjectMap(userinfo);
    }



    /**
     * 未使用
     * 用户鉴权
     * @param
     * @return
     */
    @ApiOperation(value = "鉴权",notes = "例如:{\"token\":\"在用户登陆后会返回token\"},返回user")
    //@ApiImplicitParam(name = "userDom", value = "token", required = true, dataType = "UserDom")
    @PostMapping("/getUserByToken")//@RequestBody  UserDom userDom,
    public ResultVO<UserDTO> getUserByToken(HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
 //      Integer userId = 12;
        String token = UserUtils.getToken(request);
        if(userId== null){
            throw new RuntimeException("用户未授权");
        }
        SysUser sysUser = userService.selectById(userId);
        //SysUser sysUser = userService.getLoginedUserByToken(userDom.getToken());
        UserDTO userDTO = new UserDTO();
        BeanCopierUtils.copy(sysUser,userDTO);
        userDTO.setToken(token);
        return ResultVOUtil.success(userDTO);
    }

    /*
    * 未使用
    * */
    @ApiOperation(value = "退出",notes = "例如:{\"token\":\"在用户登陆后会返回token\"},返回boolean")
    @ApiImplicitParam(name = "sysUserDto", value = "token", required = true, dataType = "SysUserDto")
    @PostMapping("/logout")
    public ResultVO<Boolean> logout(@RequestBody SysUserDto sysUserDto){
        userService.invalidate(sysUserDto.getToken());
        return ResultVOUtil.success(Boolean.TRUE);
    }

//    private String getSessionKeyOropenid(String code){
//        //小程序唯一标识   (在微信小程序管理后台获取)
//        String wxspAppid = weiXinProperties.getAppid();
//        //小程序的 app secret (在微信小程序管理后台获取)
//        String wxspSecret = weiXinProperties.getSecret();
//        //授权（必填）
//        String grant_type = weiXinProperties.getGrantType();
//        //1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
//        //请求参数
//        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
//        //发送请求
//        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
//        //解析相应内容（转换成json对象）
//        JSONObject json = JSONObject.fromObject(sr);
//        //获取会话密钥（session_key）
//        String session_key = json.get("session_key").toString();
//        //用户的唯一标识（openid）
//        String openid = (String) json.get("openid");
//        return openid;
//    }


    /**
     * 修改用户个人信息
     *
     * @param  userinfo
     * @return success/false
     */
    @ApiOperation(value = "更新用户信息",notes = "例如:{" +
            "\"  \\\"avatarUrl\\\": \\\"dasddsdasasdadadadddsas\\\" +" +
            "\"  \\\"city\\\": \\\"北京,\\\" +" +
            "\"  \\\"country\\\": \\\"阿尔巴尼亚,\\\" +" +
            "\"  \\\"gender\\\": 2,\\\" +" +
            "\"  \\\"language\\\": \\\"zh_CN\\\",\\\" +" +
            "\"  \\\"nickName\\\": \\\"燕子1\\\",\\\" +" +
            "\"  \\\"openId\\\": \\\"123456\\\",\\\"  +" +
            "\"  \\\"province\\\": \\\"朝阳区\\\"\\\" +" +
            "\"},返回boolean")
    @ApiImplicitParam(name = "userinfo", value = "微信用户信息", required = true, dataType = "Userinfo")
    @PostMapping("/editUserInfo")
    public ResultVO<Boolean> editUserInfo(@RequestBody Userinfo userinfo) {
        if(StringUtils.isEmpty(userinfo.getOpenId())){
            throw new RuntimeException("用户openId不能为空");
        }
        return  ResultVOUtil.success(userService.updateWeiXinInfo(userinfo));
    }


    /**
     * 修改用户个人签名
     *
     * @param  userinfo
     * @return success/false
     */
    @ApiOperation(value = "更新用户个人签名",notes = "例如:{\"personSignatureName\": \"个性签名QQ\",\"userId\": 9},返回boolean")
    @ApiImplicitParam(name = "userinfo", value = "用户信息", required = true, dataType = "Userinfo")
    @PostMapping("/editUserPersonSignatureName")
    public ResultVO<Boolean> editUserPersonSignatureName(@RequestBody Userinfo userinfo, javax.servlet.http.HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        userinfo.setUserId(userId);
        SysUser user = userService.selectById(userId);
        //更新user数据
        BeanUtils.copyProperties(userinfo, user);
        user.setUpdateTime(new Date());
        return   ResultVOUtil.success(userService.updateUserPersonSignatureName(user));
    }

    /**
     * 修改用户是否对外公布个人信息
     *
     * @param  sysUserDto
     * @return success/false
     */
    @ApiOperation(value = "修改用户是否对外公布个人信息,0:允许查看个人主页,1:不允许查看个人主页",notes = "例子:{" +
            "\"  \\\"openId\\\": \\\"123456\\\",\\\"" +
            "\"  \\\"allowFlag\\\":\\\"1\\\"\\\"" +
            "\"},返回boolean")
    @ApiImplicitParam(name = "sysUserDto", value = "微信用户信息", required = true, dataType = "SysUserDto")
    @PostMapping("/editUserAllowFlag")
    public ResultVO<Boolean> editUserAllowFlag(@RequestBody SysUserDto sysUserDto,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        sysUserDto.setUserId(userId);
        return  ResultVOUtil.success(userService.updateUserAllowFlag(sysUserDto));
    }

    /**
     * 修改用户是否禁用
     *
     * @param  sysUserDto
     * @return success/false
     */
    @ApiOperation(value = "修改用户是否禁用,0:正常,1:禁用",notes = "例子:{" +
            "            \"  \\\"openId\\\": \\\"123456\\\",\\\"" +
            "            \"  \\\"forbideFlag\\\":\\\"1\\\"\\\"" +
            "            \"}返回boolean")
    @ApiImplicitParam(name = "sysUserDto", value = "微信用户信息", required = true, dataType = "SysUserDto")
    @PostMapping("/editUserForbideFlag")
    public ResultVO<Boolean> editUserForbideFlag(@RequestBody SysUserDto sysUserDto,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        sysUserDto.setUserId(userId);
        return  ResultVOUtil.success(userService.updateUserForbideFlag(sysUserDto));
    }



    /**
     * 通过用户昵称查询用户
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "通过用户昵称查询用户",notes = "例如:{" +
            "            \"  \\\"currentPage\\\": 1,\\\"" +
            "            \"  \\\"nickName\\\": \\\"燕子\\\",\\\"" +
            "            \"  \\\"pageSize\\\": 10\\\"" +
            "            \"}返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "微信用户昵称", required = true, dataType = "UserDom")
    @PostMapping("/findUserByNickname")
    public ResultVO<ListResponse<UserDTO>> findUserByNickname(@RequestBody UserDom userDom) {
        ListResponse<UserDTO>  userDTOListResponse= userService.getAllUsers(userDom);
        return ResultVOUtil.success(userDTOListResponse);
    }

    /**
     * 修改用户积分
     *
     * @param  sysUserDto
     * @return success/false
     */
//    @ApiOperation(value = "修改用户积分,例如:{\n" +
//            "  \"amount\": 20,\n" +
//            "  \"userId\": 9\n" +
//            "}",notes = "返回boolean")
//    @ApiImplicitParam(name = "sysUserDto", value = "用户积分信息", required = true, dataType = "SysUserDto")
    @PostMapping("/editUserPoint")
    public ResultVO<Boolean> editUserPoint(@RequestBody SysUserDto sysUserDto,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        sysUserDto.setUserId(userId);
        return  ResultVOUtil.success(userService.updateUserAmount(sysUserDto));
    }

    /**
     * 查询用户积分排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户积分排行,财富排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserByAmount")
    public ResultVO<ListResponse<UserDTO>> findUserByAmount(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> allUsersByAmount = userService.getAllUsersByAmount(userDom);
        return ResultVOUtil.success(allUsersByAmount);
    }


    /**
     * 查询用户活跃度排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照发帖数排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserRankByPost")
    public ResultVO<ListResponse<UserDTO>> findUserRankByPost(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> response  = userService.findUserRankByPost(userDom);
        return ResultVOUtil.success(response);
    }


    /**
     * 查询用户活跃度排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照回帖数排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserRankByReply")
    public ResultVO<ListResponse<UserDTO>> findUserRankByReply(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> response =  userService.findUserRankByReply(userDom);
        return ResultVOUtil.success(response);
    }

    /**
     * 查询用户活跃度排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照采纳回答数排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserRankByReplyAccept")
    public ResultVO<ListResponse<UserDTO>> findUserRankByReplyAccept(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> response = userService.findUserRankByReplyAccept(userDom);
        return ResultVOUtil.success(response);
    }

    /**
     * 查询用户活跃度排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照打赏帖子数排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserRankByAward")
    public ResultVO<ListResponse<UserDTO>> findUserRankByAward(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> response =  userService.findUserRankByAward(userDom);
        return ResultVOUtil.success(response);
    }


    /**
     * 查询用户活跃度排行
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照浏览帖子数排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserRankByBrowsePost")
    public ResultVO<ListResponse<UserDTO>> findUserRankByBrowsePost(@RequestBody UserDom userDom) {
        ListResponse<UserDTO> response =  userService.findUserRankByBrowsePost(userDom);
        return ResultVOUtil.success(response);
    }


    /**
     * 查询我关注的用户列表
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询我关注的用户列表",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findConcernUserListByUser")
    public ResultVO<ListResponse<UserDTO>> findConcernUserListByUser(@RequestBody UserDom userDom,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        userDom.setUserId(userId);
        ListResponse<UserDTO> response =  userService.findConcernUserListByUser(userDom);
        return ResultVOUtil.success(response);
    }

    /**
     * 查询我的粉丝数
     *
     * @param userDom 用户
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询我的粉丝数",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},按照积分多少返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findConcernedCount")
    public R<Long> findConcernedCount(@RequestBody UserDom userDom,HttpServletRequest request) {
        R<Long> r = new R<>();
        //        Integer userId = UserUtils.getUserId(request);
//        userDom.setUserId(userId);
        Long response =  userService.findConcernedCount(userDom);
        r.setData(response);
        return r;
    }


    /**
     * 查询用户活跃度排行,按照发帖数占权重0.4,回帖15%,浏览15%,打赏15%,采纳15%
     *
     * @param
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询用户活跃度排行,按照发帖数占权重0.4,回帖15%,浏览15%,打赏15%,采纳15%",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserByWeight")//@RequestBody UserDom userDom
    public ResultVO<ListResponse<UserDTO>> findUserByWeight(HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        UserDom userDom = new UserDom();
        userDom.setUserId(userId);
        ListResponse<UserDTO> response =  userService.findUserByWeight(userDom);
        return ResultVOUtil.success(response);
    }

    @ApiOperation(value = "点击关注,添加关注",notes = "添加关注")
    @ApiImplicitParam(name = "bbsUserConcernDto", value = "", required = true, dataType = "BbsUserConcernDto")
    @PostMapping("/addConcern")
    public ResultVO<Boolean> addConcern(@RequestBody BbsUserConcernDto bbsUserConcernDto,HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
        bbsUserConcernDto.setConcernId(userId);
//        bbsUserConcernDto.setConcernId(9);  //测试数据
        return ResultVOUtil.success(userService.addConcern(bbsUserConcernDto));
    }

    /**
     * @Description :批量添加关注用户
     * @Param bbsUserLabelDom
     * @return ResultVO
     * @Author : zhanghongmeng
     * @Date 15:49 2018/12/17
     **/
    @ApiOperation(value = "批量添加关注用户",notes = "批量添加关注")
    @ApiImplicitParam(name = "bbsUserConcernDto", dataType = "BbsUserConcernDto")
    @PostMapping("/addConcerns")
    public ResultVO<Boolean> addConcerns(@RequestBody BbsUserConcernDto bbsUserConcernDto,HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
 //       Integer userId = 9;
        ResultVO<Boolean> r = new ResultVO<>();
        bbsUserConcernDto.setConcernId(userId);
        List<BbsConcern> list  = new ArrayList<>();
        for(String s : bbsUserConcernDto.getConcernedIdss()) {
            BbsConcern bbsConcern = new BbsConcern();
            bbsConcern.setConcernId(userId);
            bbsConcern.setConcernedId(Integer.parseInt(s));
            list.add(bbsConcern);
        }
        boolean i = iBbsConcernService.insertBatch(list);
        if (i==true){
            r.setMsg("添加成功");
            r.setCode(0);
            r.setData(true);
        }else {
            r.setMsg("添加失败");
            r.setCode(1);
        }
        return r;
    }

    @ApiOperation(value = "点击取消关注,取消关注",notes = "例子:{\n" +
            "  \"concernId\": 9,\n" +
            "  \"concernedId\": 5\n" +
            "},返回true,false")
    @ApiImplicitParam(name = "bbsUserConcernDto", value = "", required = true, dataType = "BbsUserConcernDto")
    @PostMapping("/delConcern")
    public ResultVO<Boolean> delConcern(@RequestBody BbsUserConcernDto bbsUserConcernDto,HttpServletRequest request){
        Integer userId = UserUtils.getUserId(request);
        bbsUserConcernDto.setConcernId(userId);
        return ResultVOUtil.success(userService.delConcern(bbsUserConcernDto));
    }


    /**
     * @Description :添加或取消用户关注
     * @Param bbsUserLabelDom
     * @return R
     * @Author : zhanghongmeng
     * @Date 15:49 2018/12/17
     **/
    @ApiOperation(value = "添加或取消用户关注")
    @ApiImplicitParam(name = "bbsUserConcernDto", value = "添加或取消用户关注", required = true, dataType = "BbsUserConcernDto")
    @PostMapping("/addOrDelete")
    public R<Integer> addOrDelete(@RequestBody BbsUserConcernDto bbsUserConcernDto, HttpServletRequest request) {
        R<Integer> r = new R<>();
        Integer userId = UserUtils.getUserId(request);
 //       Integer userId = 9;
        if(bbsUserConcernDto.getConcernStatus() == 0){
            if (userId == bbsUserConcernDto.getConcernedId()){
                r.setData(CommonConstant.Concern_Status_err);
            }else {
                bbsUserConcernDto.setConcernId(userId);
                Boolean addConcern = userService.addConcern(bbsUserConcernDto);
                if (addConcern == true)
                    r.setData(CommonConstant.Concern_Status_one);
            }
        }else if(bbsUserConcernDto.getConcernStatus() == 1) {
            bbsUserConcernDto.setConcernId(userId);
            Boolean delConcern = userService.delConcern(bbsUserConcernDto);
            if (delConcern == true)
                r.setData(CommonConstant.Concern_Status_zero);
        }
        return r;
    }

    /**
     * 查询当前用户信息关注用户数量
     *
     * @param userDom
     * @return 用户关注数量
     */

    @ApiOperation(value = "根据用户ID获取关注数量",notes = "用户关注数量")
    @ApiImplicitParam(name = "userDom", value = "查询用户关注量", required = true, dataType = "UserDom")
    @PostMapping("/getConcernNum")
    public R<Long> getConcernNum(@RequestBody UserDom userDom) {
        R<Long> r = new R<>();
        Integer id = userDom.getUserId();
        Long res1 = userService.getConcernNum(id);
        Long res2 = userService.getLabelNum(id);
        Long result = res1 + res2 ;
        r.setData(result);
        return r;
    }

    /**
     * 查询达人排行,按照最终积分高低排行
     *
     * @param
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询达人排行,按照最终积分高低排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserByScore")
    public ResultVO<ListResponse<UserDTO>> findUserByScore(@RequestBody UserDom userDom,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
//        Integer userId = 9;
        userDom.setUserId(userId);
        ListResponse<UserDTO> response =  userService.findUserByScore(userDom);
        return ResultVOUtil.success(response);
    }

    /**
     * 查询消费排行,按照消费积分记录排行
     *
     * @param
     * @return UseVo 对象
     */
    @ApiOperation(value = "查询消费排行,按照按照消费积分记录排行",notes = "例子:{\"currentPage\": 1,\"pageSize\": 10},返回用户列表")
    @ApiImplicitParam(name = "userDom", value = "用户信息", required = true, dataType = "UserDom")
    @PostMapping("/findUserByConsum")//@RequestBody UserDom userDom
    public ResultVO<ListResponse<UserDTO>> findUserByConsum(@RequestBody UserDom userDom,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
        userDom.setUserId(userId);
        ListResponse<UserDTO> response =  userService.findUserByConsum(userDom);
        return ResultVOUtil.success(response);
    }

    /**
     * 查询当前用户信息关注用户信息列表
     *
     * @param userDom
     * @return 用户关注用户信息列表
     */
    @ApiOperation(value = "根据用户ID获取关注用户信息列表",notes = "用户关注用户信息列表")
    @ApiImplicitParam(name = "userDom", value = "查询用户关注用户信息列表", required = true, dataType = "UserDom")
    @PostMapping("/getConcernInfo")
    public ResultVO getConcernInfo(@RequestBody UserDom userDom ,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
//   Integer userId = 9;
        ResultVO<Object> r = new ResultVO<>();
        PageResult<SysUserDto> pageResult = new PageResult<>();
        List<SysUserDto> list = userService.getConcernInfo(userDom);
        for (SysUserDto sysUserDto : list){
            Integer uId = sysUserDto.getUserId();
            BbsUserConcernDto buc = new BbsUserConcernDto();
            buc.setConcernId(userId);
            buc.setConcernedId(uId);
            if (uId == userId){
                sysUserDto.setConcernStatus(CommonConstant.Concern_Status_err);
            }else {
                int i = userService.getConcernStatus(buc);
                //0 已关注 , 1 未关注
                if (i == 1) {
                    sysUserDto.setConcernStatus(CommonConstant.Concern_Status_one);
                } else {
                    sysUserDto.setConcernStatus(CommonConstant.Concern_Status_zero);
                }
            }
        }
        Long count = userService.getConcernNum(userDom.getUserId());
        pageResult.setPageNum(userDom.getCurrentPage()/userDom.getPageSize()+1);
        pageResult.setPageSize(userDom.getPageSize());
        pageResult.setAllNum(count.intValue());
        pageResult.setAllPage((count.intValue()+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }

    /**
     * 查询用户粉丝信息列表
     *
     * @param userDom
     * @return 用户粉丝信息列表
     */
    @ApiOperation(value = "根据用户ID获取粉丝信息列表",notes = "用户粉丝信息列表")
    @ApiImplicitParam(name = "userDom", value = "用户粉丝信息列表", required = true, dataType = "UserDom")
    @PostMapping("/findConcernedInfo")
    public ResultVO findConcernedInfo(@RequestBody UserDom userDom ,HttpServletRequest request) {
        Integer userId = UserUtils.getUserId(request);
 //       Integer userId = 9;
        PageResult<SysUserDto> pageResult = new PageResult<>();
        ResultVO<Object> r = new ResultVO<>();
        List<SysUserDto> list = userService.findConcernedInfo(userDom);
        for (SysUserDto sysUserDto : list){
            Integer uId = sysUserDto.getUserId();
            BbsUserConcernDto buc = new BbsUserConcernDto();
            buc.setConcernId(userId);
            buc.setConcernedId(uId);
            if (uId == userId){
                sysUserDto.setConcernStatus(CommonConstant.Concern_Status_err);
            }else {
                int i = userService.getConcernStatus(buc);
                //0 已关注 , 1 未关注
                if (i == 1){
                    sysUserDto.setConcernStatus(CommonConstant.Concern_Status_one);
                }else {
                    sysUserDto.setConcernStatus(CommonConstant.Concern_Status_zero);
                }
            }

        }
        Long count = userService.findConcernedCount(userDom);
        pageResult.setPageNum(userDom.getCurrentPage()/userDom.getPageSize()+1);
        pageResult.setPageSize(userDom.getPageSize());
        pageResult.setAllNum(count.intValue());
        pageResult.setAllPage((count.intValue()+(pageResult.getPageSize()-1))/pageResult.getPageSize());
        pageResult.setData(list);
        r.setData(pageResult);
        return r;
    }
//    /**
//     * 查询用户关注状态: 0 已关注 , 1 未关注
//     *
//     * @param userDom
//     * @return 查询用户关注状态
//     */
//    @ApiOperation(value = "根据用户ID获取关注用户状态",notes = "查询用户关注数")
//    @ApiImplicitParam(name = "userDom", value = "查询用户关注用户状态", required = true, dataType = "UserDom")
//    @PostMapping("/getConcernStatus")
//    public R getConcernStatus(@RequestBody UserDom userDom ,HttpServletRequest request) {
////        Integer userId = UserUtils.getUserId(request);
//        Integer userId = 5;
//        Integer uId = userDom.getUserId();
//        R r = new R();
//        UsserStatulDom usserStatulDom = new UsserStatulDom();
//        usserStatulDom.setU1(userId);
//        usserStatulDom.setU2(uId);
//        int i = userService.getConcernStatus(usserStatulDom);
//        //0 已关注 , 1 未关注
//        if (i == 1){
//            r.setData(CommonConstant.Concern_Status_zero);
//        }else {
//            r.setData(CommonConstant.Concern_Status_one);
//        }
//        return r;
//    }
    @ApiOperation(value = "获取当前用户感兴趣用户信息列表",notes = "用户关注用户信息列表")
    @ApiImplicitParam(name = "userDom", value = "获取当前用户感兴趣用户信息列表", required = true, dataType = "UserDom")
    @PostMapping("/getInterestingUsers")
    public ResultVO<List<SysUserDto>> getInterestingUsers(HttpServletRequest request){
        UserDom userDom = new UserDom();
        Integer userId = UserUtils.getUserId(request);
        userDom.setUserId(userId);
        return ResultVOUtil.success(userService.getInterestingUsers(userDom));
    }


}
