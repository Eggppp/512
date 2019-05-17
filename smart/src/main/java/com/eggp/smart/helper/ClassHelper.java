package com.eggp.smart.helper;

import com.eggp.smart.annotation.Controller;
import com.eggp.smart.annotation.Service;
import com.eggp.smart.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手
 */
public final class ClassHelper {
    /**
     * 定义类集合，用来存放加载的类
     */
    private static final Set<Class<?>> CLASS_SET;
    static {
        String basePackage=ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包下所有Service类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet= new HashSet<>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下所有Controller类
     * @return
     */
    public static Set<Class<?>> getControllerSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下所有Bean类（包括Controller、Service等）
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet=new HashSet<>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerSet());
        return beanClassSet;
    }

}
