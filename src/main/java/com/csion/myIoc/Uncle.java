package com.csion.myIoc;

import com.csion.myIoc.drawing.ToyDrawing;

/**
 * Created by csion on 2022/3/3 10:40.
 * 大叔，获取玩具，只向外部暴露这些接口
 */
public interface Uncle {

    Object getToy(String name);

    <T> T getToy(Class<T> type);

    <T> T getToy(Class<T> type, String name);


}
