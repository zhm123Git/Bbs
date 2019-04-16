package com.github.pig.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by domainclient on 2018/7/31.
 */
public class BeanCopierUtils {

    public static Map<String,BeanCopier> beanCopierMap = new HashMap<String,BeanCopier>();

    public static void copy(Object source, Object target){

        String beanKey =  generateKey(source.getClass(), target.getClass());

        BeanCopier copier =  null;

        if(!beanCopierMap.containsKey(beanKey)){

            copier = BeanCopier.create(source.getClass(), target.getClass(), false);

            beanCopierMap.put(beanKey, copier);

        }else{

            copier = beanCopierMap.get(beanKey);

        }

        copier.copy(source, target, null);

    }

    private static String generateKey(Class<?> class1,Class<?>class2){

        return class1.toString() + class2.toString();

    }

    public static<T> List<T> copyList(List<?> sourceList,Class<T> targetClazz) {
        if(CollectionUtils.isEmpty(sourceList)){
            return null;
        }
        List<T> targetList = new ArrayList<T>();
        try {
            for(Object source : sourceList){
                T obj = targetClazz.newInstance();
                copy(source,obj);
                targetList.add(obj);
            }
            return targetList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
