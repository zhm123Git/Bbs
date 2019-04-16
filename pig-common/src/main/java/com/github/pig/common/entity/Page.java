package com.github.pig.common.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;


/*
* 分页实体类
* */
@Data
public class Page<T> {

    /*
    * 数据
    * */
    private T data;

    /*
    * 当前页
    * */
    private Integer pageNum;

   /*
   * 每页多少条
   * */
    private Integer pageSize;

    /*
    * 总条数
    * */
    private Integer allNum;

    /*
    * 总页数
    * */
    private Integer allPage;



}
