package com.github.pig.common.util;

import io.swagger.models.auth.In;

import java.util.List;

public class ListResponse<T> {
  
  private List<T> list;
  
  private Long count;

  private Integer allPage;

  public static <T> ListResponse<T> build(List<T> list,Long count){
    ListResponse<T> response = new ListResponse<>();
    response.setCount(count);
    response.setList(list);
    return response;
  }
  public static <T> ListResponse<T> build(List<T> list,Long count,Integer allPage){
    ListResponse<T> response = new ListResponse<>();
    response.setCount(count);
    response.setList(list);
    response.setAllPage(allPage);
    return response;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Integer getAllPage() {
    return allPage;
  }

  public void setAllPage(Integer allPage) {
    this.allPage = allPage;
  }
}
