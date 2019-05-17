package com.eggp.smart;

import com.eggp.smart.annotation.Controller;
import com.eggp.smart.helper.BeanHelper;
import com.eggp.smart.helper.ClassHelper;
import com.eggp.smart.helper.IocHelper;
import com.eggp.smart.util.ClassUtil;


/**
 * 加载相应的Helper
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList={ClassHelper.class, BeanHelper.class, IocHelper.class, Controller.class};
        for(Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
