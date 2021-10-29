package com.mantou.utils;

import java.lang.reflect.Field;

/**
 * 利用反射获取数据
 */
public class ClazzUtil {

    public static String getData(Object obj){

        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String nameVlues="";
        for (Field field : fields) {
            //打开私有访问
            field.setAccessible(true);
            //获取属性
            String name = field.getName();
            if (name != "signature") {
                //获取属性值
                Object value;
                try {
                    value = field.get(obj);
                    //一个个赋值
                    nameVlues += field.getName()+":"+value+",";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //获取最后一个逗号的位置
        int lastIndex = nameVlues.lastIndexOf(",");
        //不要最后一个逗号","
        String  data= nameVlues.substring(0,lastIndex) ;

        return data;
    }

}
