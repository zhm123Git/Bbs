package com.github.pig.common.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * ClassName:PageResult
 * Authot:ZhangHongMeng
 * Date:2018/12/13 21:45
 * project: (pig)
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 9056411043515781783L;

    private List<T> data;
    private Integer pageNum; //当前页
    private Integer pageSize;//每页多少
    private Integer allNum;//总条数
    private Integer allPage;//总页数


}
