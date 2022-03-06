package com.csion.myIoc.drawing;

import java.util.List;

/**
 * Created by csion on 2022/3/3 11:24.
 * 图纸收集者
 */
public interface DrawingGatherer {

    /**
     * 解析蓝图
     * @return  返回解析的玩具定制单
     */
    List<Class<?>> gather();

    /**
     * 获取解析的结果
     * @return  解析的玩具定制单
     */
    List<Class<?>> getToyTypes();

}
