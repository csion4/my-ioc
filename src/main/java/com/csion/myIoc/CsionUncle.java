package com.csion.myIoc;

/**
 * Created by csion on 2022/3/3 11:13.
 * 富二代，啥都从父类继承
 */
public class CsionUncle extends AbstractAnnotationUncle {

    /**
     * 创建一个uncle实例
     * @param bluePrint 蓝图，指定扫描的注解类根路径
     */
    public CsionUncle(Class<?>... bluePrint){
        super(bluePrint);
    }
}
