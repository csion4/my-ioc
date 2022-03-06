package com.csion.myIoc.drawing;

import com.csion.myIoc.annotation.AToy;
import com.csion.myIoc.utils.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by csion on 2022/3/3 11:26.
 * 基于注解方式的图纸收集者
 */
public class AnnotationDrawingGatherer implements DrawingGatherer {
    private Class<?>[] bluePrint;
    private Set<String> classes = new HashSet<>();
    private List<Class<?>> toyClass = new ArrayList<>();

    /**
     * 实例化一个基于注解的图纸收集者
     * @param bluePrint 蓝图
     */
    public AnnotationDrawingGatherer(Class<?>... bluePrint){
        Assert.notEmpty(bluePrint, "空的蓝图是无意义的!");
        this.bluePrint = bluePrint;
    }

    /**
     * 基于注解的收集者，用于将传入的蓝图中所有类都解析出来
     * @return 返回解析出来的类
     */
    public List<Class<?>> gather() {
        for (Class<?> root : bluePrint) {
            try {
                String name = root.getName();
                Class<?> rootClass = Class.forName(name);
                String rootPackage = rootClass.getPackage().getName();
                Enumeration<URL> resources = rootClass.getClassLoader().getResources(rootPackage.replace(".", "/"));
                while (resources.hasMoreElements()){
                    URL url = resources.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)){   // 对于目录的处理
                        findClass(rootPackage, URLDecoder.decode(url.getFile(), "UTF-8"), null);   // decode处理非英文目录
                    } else if ("jar".equals(protocol)){
                        // todo: 待处理
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                throw new IllegalArgumentException("无法加载的蓝图：" + root.getName(), e);
            }
        }
        if (!classes.isEmpty()){
            searchToy();
        }
        return toyClass;
    }

    /**
     * 遍历获取包下所有的类全路径
     * @param pkg
     * @param path
     * @param file
     */
    private void findClass(String pkg, String path, File file) {
        if (file == null) {
            file = new File(path);
        }
        if (!file.exists()) {
            return;
        }
        if (file.isFile()){
            classes.add(pkg.substring(0, pkg.length() - 6));
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles(f -> f.isDirectory() || f.getName().endsWith(".class"));
            for (File f : files) {
                findClass(pkg + "." + f.getName(), null, f);
            }
        }

    }

    /**
     * 检索所有@AToy注解的toy
     */
    private void searchToy() {
        for (String className : classes) {
            try {
                Class<?> clazz = Class.forName(className);
                AToy aToy = clazz.getDeclaredAnnotation(AToy.class);
                if (aToy != null){
                    toyClass.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                // todo: 这里需要日志系统的帮忙
            }

        }
    }

    /**
     * 返回检索出来的所有的toy
     * @return
     */
    public List<Class<?>> getToyTypes() {
        return toyClass;
    }
}
