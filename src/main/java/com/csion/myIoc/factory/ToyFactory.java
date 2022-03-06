package com.csion.myIoc.factory;

import com.csion.myIoc.box.ToyBox;
import com.csion.myIoc.drawing.ToyDrawing;
import com.csion.myIoc.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by csion on 2022/3/2 19:12.
 * 工厂，接受文件夹-遍历图纸-生产玩具-提供生产玩具方法
 */
public class ToyFactory extends ToyBox{ // Factory实现了box因为工厂应该拥有箱子存放toy，这里将box抽离出来是为了更好的思路
    /**
     * 工厂启动标识
     */
    private Boolean doing;
    private Map<String, ToyDrawing> folder;

    /**
     * 玩具工厂
     * @param folder 传入图纸
     */
    public ToyFactory(Map<String, ToyDrawing> folder) {
        this.folder = folder;
        doing = true;
    }

    /**
     * 关闭工厂
     */
    public void stop(){
        doing = false;
    }


    /**
     * 根据名称获取玩具
     * @param name  玩具名称
     * @return  玩具
     */
    public Object getToy(String name) {
        Object toy = super.getToy(name);
        if (toy == null && doing) {
            ToyDrawing toyDrawing = folder.get(name);   // 这里保证工厂中创建的都是在folder中的
            addToy(toyDrawing.getToyName(), createToy(toyDrawing));
            addToyType(toyDrawing.getClazz(), toyDrawing.getToyName());
            return super.getToy(name);
        }
        return toy;
    }


    public <T> T getToy(Class<T> type){
        return getToy(type, null);
    }

    /**
     * 根据类型获取
     * @param type
     * @param <T>
     * @return
     */
    public  <T> T getToy(Class<T> type, String name){
        if (StringUtils.isEmpty(name)) {
            List<String> toysByType = getToysByType(type);
            if (toysByType.isEmpty()) {
                return null;
            }
            if (toysByType.size() > 1) {
                throw new IllegalArgumentException("类型：[" + type + "]有多个对应的Toy");
            }
            Object toy = getToy(toysByType.get(0));
            return toy == null ? null : (T) toy;
        }
        else {
            Object toy = getToy(name);
            if (type.isInstance(toy)) {     // class.isInstance 支持泛型校验，就很棒
                return (T) toy;
            }
            return null;
        }
    }

    public List<String> getToysByType(Class<?> type){
        Map<Class<?>, List<String>> toyTypes = getToyTypes();
        List<String> list = new ArrayList<>();
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {   // 如果是接口或者抽象类
            for (Class<?> aClass : toyTypes.keySet()) {
                if (type.isAssignableFrom(aClass)) {
                    list.addAll(toyTypes.get(aClass));
                }
            }
        } else {
            list.addAll(getToyTypes(type));
        }
        return list;
    }

    /**
     * 真正的创建toy
     * @param drawing
     * @return
     */
    private Object createToy(ToyDrawing drawing) {
        Class<?> clazz = drawing.getClazz();
        try {
            return clazz.newInstance(); // 优先使用无参构造器实例化对象
        } catch (Exception e) {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                // todo: 对无无参构造器时选择一个合适的构造器
            }
            return null;
        }
    }
}

