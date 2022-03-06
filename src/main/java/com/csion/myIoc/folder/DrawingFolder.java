package com.csion.myIoc.folder;

import com.csion.myIoc.drawing.ToyDrawing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by csion on 2022/3/2 19:23.
 * 文件夹，存储所有的图纸
 */
public class DrawingFolder {

    /**
     * 文件夹缓存
     */
    private final Map<String, ToyDrawing> folder = new ConcurrentHashMap<String, ToyDrawing>();

    /**
     * 向文件夹中添加玩具图纸
     * @param toyName   玩具名称
     * @param toyDrawing    图纸
     */
    public void addDrawing(String toyName, ToyDrawing toyDrawing) {
        folder.put(toyName, toyDrawing);
    }

    /**
     * 根据玩具名称获取图纸
     * @param name  玩具名称
     * @return  图纸
     */
    public ToyDrawing getToyDrawing(String name) {
        return folder.get(name);
    }

    /**
     * 获取文件夹
     * @return  文件夹
     */
    public Map<String, ToyDrawing> getFolder() {
        return folder;
    }

}
