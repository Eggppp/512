package com.eggp.smart.helper;

import com.eggp.smart.annotation.Action;
import com.eggp.smart.bean.Handler;
import com.eggp.smart.bean.Request;
import com.eggp.smart.util.ArrayUtil;
import com.eggp.smart.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 */
public final class ControllerHelper {
    /**
     * 用于存放所有请求与处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP=new HashMap<>();

    static {
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)){
            for(Class<?> controllerClass:controllerClassSet){
                Method[] methods=controllerClass.getMethods();
                if(ArrayUtil.isNotEmpty(methods)){
                    for(Method method:methods){
                        // 判断当前方法是否带有Action注解
                        if(method.isAnnotationPresent(Action.class)){
                            // 从Action注解中获取URL映射规则
                            Action action=method.getAnnotation(Action.class);
                            String mapping =action.value();
                            // 验证URL映射规则
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array=mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array)&&array.length==2){
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
