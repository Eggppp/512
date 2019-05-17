package com.eggp.smart.helper;

import com.eggp.smart.annotation.Inject;
import com.eggp.smart.util.ArrayUtil;
import com.eggp.smart.util.CollectionUtil;
import com.eggp.smart.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public final class IocHelper {
    static {
        // 获取所有Bean与Bean实例之间的映射关系
        Map<Class<?> ,Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)){
            // 遍历
            for(Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet()){
                // 从BeanMap中获取Bean类和Bean实例
                Class<?> beanClass=beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                // 获取Bean定义的成员变量（Bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    for(Field beanField:beanFields){
                        // 判断当前Bean Field 是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldClass=beanField.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            if(beanFieldInstance!=null){
                                // 通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
