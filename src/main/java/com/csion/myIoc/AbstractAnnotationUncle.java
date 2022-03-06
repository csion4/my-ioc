package com.csion.myIoc;

import com.csion.myIoc.drawing.AnnotationDrawingGatherer;
import com.csion.myIoc.drawing.DrawingGatherer;
import com.csion.myIoc.drawing.ToyDrawing;
import com.csion.myIoc.factory.ToyFactory;
import com.csion.myIoc.folder.DrawingFolder;
import com.csion.myIoc.painting.Painting;
import com.csion.myIoc.utils.Assert;
import com.csion.myIoc.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by csion on 2022/3/3 10:43.
 * 抽象出来一个基于注解的大叔，用于串联整个生产玩具的过程
 */
public abstract class AbstractAnnotationUncle extends Painting implements Uncle {  // Painting算是Uncle的一个技能吧，下面那些不用继承实现是因为他们不是uncle的所属技能，而是持有的一个实体
    protected DrawingGatherer drawingGatherer;
    private ToyFactory toyFactory;
    private DrawingFolder drawingFolder;


    /**
     * 实例化图纸收集者，图纸文件夹
     * @param bluePrint 这里的每个蓝图都给DrawingGatherer指一个收集方向
     */
    protected AbstractAnnotationUncle(Class<?>... bluePrint){
        drawingGatherer = new AnnotationDrawingGatherer(bluePrint);
        drawingFolder = new DrawingFolder();
        justDoIt();     // 说干就干
    }

    /**
     * 开启生产流水线
     */
    private void justDoIt(){
        // 收集小朋友的玩具定制单
        List<Class<?>> toyClassList = drawingGatherer.gather();

        // 将定制单汇成图纸并保存到一个文件夹中
        for (Class<?> toyClass : toyClassList) {
            ToyDrawing toyDrawing = designToy(toyClass);
            drawingFolder.addDrawing(toyDrawing.getToyName(), toyDrawing);
        }

        // 创建一个玩具工厂并提供文件夹
        toyFactory = new ToyFactory(drawingFolder.getFolder());

        // 工厂生产玩具
        for (String s : drawingFolder.getFolder().keySet()) {
            toyFactory.getToy(s);   // 这里不用直接传入drawing而是让工厂自己去folder中取是保证工厂中创建的都是在folder中
        }

        // 对玩具进行着色
        paint(drawingFolder.getFolder(), toyFactory);

        // 表示生产完成，完结撒花
        toyFactory.stop();
    }

    /**
     * 实现uncle接口的根据玩具名称获取玩具功能
     * @param name
     * @return
     */
    public Object getToy(String name) {
        Assert.notNull(name, "无法以空的玩具名称获取玩具");
        return toyFactory.getToy(name);
    }

    /**
     * 实现uncle接口的根据玩具类型获取玩具功能
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getToy(Class<T> type){     // 根据类型获取需要考虑泛型(接口获取和抽象类)
        Assert.notNull(type, "无法以空的玩具类型获取玩具");
        return toyFactory.getToy(type);
    }

    /**
     * 实现uncle接口的根据玩具名称+类型获取玩具功能
     * @param type
     * @param name
     * @param <T>
     * @return
     */
    public <T> T getToy(Class<T> type, String name){
        Assert.notNull(type, "无法以空的玩具类型获取玩具");
        Assert.notNull(name, "无法以空的玩具名称获取玩具");
        return toyFactory.getToy(type, name);
    }

    /**
     * 根据定制单绘制图纸
     * @param toyClass  玩具定制单
     * @return  返回图纸
     */
    protected ToyDrawing designToy(Class<?> toyClass) {
        Constructor<?>[] constructors = toyClass.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException("toy：<" + toyClass.getName() + ">没有开放的创建入口!");
        }
        ToyDrawing toyDrawing = new ToyDrawing();
        toyDrawing.setToyName(StringUtils.firstLowerCase(toyClass.getSimpleName()));
        toyDrawing.setClazz(toyClass);
        return toyDrawing;
    }
}
