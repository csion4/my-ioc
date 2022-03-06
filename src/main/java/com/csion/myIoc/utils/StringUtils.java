package com.csion.myIoc.utils;

/**
 * Created by csion on 2022/3/3 20:07.
 * String工具类
 */
public abstract class StringUtils {

    public static Boolean isEmpty(String s){
        return s == null || "".equals(s.trim());
    }

    // 首字母小写
    public static String firstLowerCase(String s) {
        char[] cs= s.toCharArray();
        if (65 <= cs[0] && cs[0] <= 90) {
            cs[0]+=32;
        }
        return String.valueOf(cs);
    }
}
