package com.github.pig.common.util;

import java.util.List;

/**
 * Created by yyn on 2018/12/13 0013.
 */
public class ListResponseUtil {

    public static <T> ListResponse<T> build(ListResponse<T> response, List<T> list, Long count){
        response.setCount(count);
        response.setList(list);
        return response;
    }
}
