package com.csion.myIoc.utils;

/**
 * Created by csion on 2022/3/3 14:17.
 * 断言，抽象类不可以实例化
 */
public abstract class Assert {
    public static void notEmpty(Object[] o, String msg){
        if (o == null || o.length == 0){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(String s, String msg) {
        if (StringUtils.isEmpty(s)){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object s, String msg) {
        if (s == null){
            throw new IllegalArgumentException(msg);
        }
    }
}
