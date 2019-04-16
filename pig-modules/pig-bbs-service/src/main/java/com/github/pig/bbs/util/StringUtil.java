package com.github.pig.bbs.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public List<String> sLits(String string){
        List<String> list = new ArrayList<>();
        for (int i= 0;i<string.length();i++ ){
            list.add(String.valueOf(string.charAt(i)));
        }
        return list;
    }
}
