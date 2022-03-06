package com.csion.myIoc.painting;

import com.csion.myIoc.annotation.Assemble;
import com.csion.myIoc.drawing.ToyDrawing;
import com.csion.myIoc.factory.ToyFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by csion on 2022/3/3 17:21.
 * 上色 - 属性填充
 */
public abstract class Painting {

    /**
     * 属性填充，优先通过类型，相同类型的再根据名称
     * @param folder  文件夹
     * @param toyFactory 玩具工厂
     */
    protected void paint(Map<String, ToyDrawing> folder, ToyFactory toyFactory){
        for (String s : folder.keySet()) {
            ToyDrawing toyDrawing = folder.get(s);
            Field[] fields = toyDrawing.getClazz().getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotationsByType(Assemble.class) != null && field.getAnnotationsByType(Assemble.class).length > 0){
                    field.setAccessible(true);

                    Object toy = null;
                    List<String> toysByType = toyFactory.getToysByType(field.getType());
                    if (toysByType.size() == 1) {
                        toy = toyFactory.getToy(toysByType.get(0));
                    } else if (toysByType.size() > 1) {
                        if (toysByType.contains(field.getName())){
                            toy = toyFactory.getToy(field.getName());
                        }
                    }
                    if (toy != null) {
                        try {
                            field.set(toyFactory.getToy(s), toy);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            // 需要日志
                        }
                    }
                }
            }
        }
    }

}
