package com.csion.myIoc.box;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by csion on 2022/3/2 19:29.
 * 箱子，存放所有生产出来的玩具，提供已经生产好的玩具
 */
public abstract class ToyBox {
    /**
     * 玩具实例缓存
     */
    private final Map<String, Object> toys = new ConcurrentHashMap<>();

    /**
     * 玩具类型与玩具缓存，用于根据类型获取玩具
     */
    private final Map<Class<?>, List<String>> toyTypes = new ConcurrentHashMap<>();   // toyType 和 name的关联


    protected Object getToy(String name) {
        return toys.get(name);
    }

    protected void addToy(String name, Object toy){
        toys.put(name, toy);
    }

    protected void addToyType(Class<?> type, String name) {
        synchronized (toyTypes) {
            List<String> list = toyTypes.get(type);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(name);
            toyTypes.put(type, list);
        }
    }

    protected Map<Class<?>, List<String>> getToyTypes() {
        return toyTypes;
    }


    protected List<String> getToyTypes(Class<?> c) {
        return toyTypes.get(c);
    }
}
