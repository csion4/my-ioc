package com.csion.myIoc.drawing;

import java.lang.reflect.Constructor;

/**
 * Created by csion on 2022/3/2 19:20.
 * 图纸，定义了图纸中应该包括哪些部分
 */
public class ToyDrawing {
    private String toyName;

    private Class<?> clazz;

    private Constructor<?>[] constructors;

    public Constructor<?>[] getConstructors() {
        return constructors;
    }

    public void setConstructors(Constructor<?>[] constructors) {
        this.constructors = constructors;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getToyName() {
        return toyName;
    }

    public void setToyName(String toyName) {
        this.toyName = toyName;
    }
}
