package com.github.pig.bbs.util;


import com.github.pig.common.entity.Page;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BbsUtil {

    /**
     *
     * @param obj 为数据model
     * @param t1  为返回的泛型
     * @param <T>
     * @return
     */
    public static  <T>T getDomOrDto(Object obj,Class<T> t1){
        try {
            T t3 = t1.newInstance();
            BeanUtils.copyProperties(obj, t3);
            return t3;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int DoubleChangeInt(double number){
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    public static String  getDateString(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String format = sdf.format(date);
        return format;
    }


    /*
    * 根据model类获取page类  model类中需要有分页所需要的参数
    * */
    public static Page  getPage(Object  obj ) {
        Page page = BbsUtil.getDomOrDto(obj, Page.class);
        page.setData(obj);
        return page;
    }



}
