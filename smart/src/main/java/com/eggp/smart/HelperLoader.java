package com.eggp.smart;


import com.eggp.smart.helper.*;
import com.eggp.smart.util.ClassUtil;


/**
 * 加载相应的Helper
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList={ClassHelper.class, BeanHelper.class, AopHelper.class,
                IocHelper.class, ControllerHelper.class};
        for(Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
