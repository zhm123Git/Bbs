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

package com.github.pig.auth.component.mobile;

import com.alibaba.fastjson.JSON;
import com.github.pig.auth.util.BodyReaderHttpServletRequestWrapper;
import com.github.pig.auth.util.HttpHelper;
import com.github.pig.common.constant.SecurityConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/1/9
 * 手机号登录验证filter
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String MOBILE_NICK_NAME = "nickName";//名字
    public static final String MOBILE_AVATAR_URL = "avatarUrl";//头像地址
    public static final String MOBILE_CITY= "city";//城市
    public static final String MOBILE_CODE= "code";//code
    public static final String MOBILE_COUNTRY= "country";//国家
    public static final String MOBILE_GENDER= "gender";//性别
    public static final String MOBILE_LANGUAGE= "language";//语言
    public static final String MOBILE_PROVINCE= "province";//语言

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private boolean postOnly = true;

    public MobileAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.MOBILE_TOKEN_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

/*        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();*/
        Map mobile = obtainMap(request) ;
        MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);

        setDetails(request, mobileAuthenticationToken);

        return this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
    }


    private Map obtainMap(HttpServletRequest request) {
        String  data ="";
        try {
            HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
            data = HttpHelper.getBodyString(requestWrapper);
        } catch (IOException e) {
            logger.error("Request接受值错误!");
        }
        Map ma = (Map) JSON.parse(data);



	/*	 "avatarUrl\\\": \\\"trtrtrtttettttttttttttttt\\\",\\\"" +
         "city\\\": \\\"朝阳区\\\",\\\" +" +
         "code\\\": \\\"最新获取的code\\\",\\\"" +
         "country\\\": \\\"china\\\",\\\"" +
         "gender\\\": 0,\\\"" +
         "language\\\": \\\"zh_CN\\\"\\\"" +*/
/*        ma.put(MOBILE_NICK_NAME, request.getParameter(MOBILE_NICK_NAME));
        ma.put(MOBILE_AVATAR_URL,request.getParameter(MOBILE_AVATAR_URL));
        ma.put(MOBILE_CITY,request.getParameter(MOBILE_CITY));
        ma.put(MOBILE_CODE,request.getParameter(MOBILE_CODE));
        ma.put(MOBILE_COUNTRY,request.getParameter(MOBILE_COUNTRY));
        ma.put(MOBILE_GENDER,request.getParameter(MOBILE_GENDER));
        ma.put(MOBILE_LANGUAGE,request.getParameter(MOBILE_LANGUAGE));
        ma.put(SPRING_SECURITY_FORM_MOBILE_KEY,request.getParameter(SPRING_SECURITY_FORM_MOBILE_KEY));
        ma.put(MOBILE_PROVINCE,request.getParameter(MOBILE_PROVINCE));
*/
        return ma;
    }


    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              MobileAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getMobileParameter() {
        return mobileParameter;
    }

    public void setMobileParameter(String mobileParameter) {
        this.mobileParameter = mobileParameter;
    }

    public boolean isPostOnly() {
        return postOnly;
    }
}

