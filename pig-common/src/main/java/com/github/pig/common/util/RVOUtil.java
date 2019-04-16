package com.github.pig.common.util;

/**
返回结果
 */
public class RVOUtil {

    public static R success(Object object) {
        R R = new R();
        R.setData(object);
        R.setCode(0);
        R.setMsg("成功");
        return R;
    }

    public static R success() {
        return success(null);
    }

    public static R error(Integer code, String msg) {
        R R = new R();
        R.setCode(code);
        R.setMsg(msg);
        return R;
    }
}
