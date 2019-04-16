package com.github.pig.bbs.util;

/**
 * 常量
 */
public interface Constant {


    public static  final Integer USER_ID=1;

    // 1帖子已经发布  0帖子未发布
    public static  final  Integer HAVE_PUBLIC = 1;
    public static  final  Integer NOT_PUBLIC = 0;

    // 1赞 2 踩  3 取消赞 4 取消踩
    public static  final Integer PRAISE      = 1;
    public static  final Integer TRAMPLE     = 2;
    public static  final Integer NOT_PRAISE  = 3;
    public static  final Integer NOT_TRAMPLE =4;

    public static final Integer addPostsPoint = 10;
    public static final String  addPostsMsg = "发帖加10积分";

    public static final Integer PAGESIZE_TEN = 10;
    public static final Integer PAGESIZE_FIFTEEN = 15;
    public static final Integer PAGESIZE_ZERO = 20;
    public static final Integer PAGENUM_ONE = 1;



}
