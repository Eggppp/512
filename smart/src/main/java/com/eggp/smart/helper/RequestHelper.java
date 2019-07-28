package com.eggp.smart.helper;

import com.eggp.smart.bean.FormParam;
import com.eggp.smart.bean.Param;
import com.eggp.smart.util.ArrayUtil;
import com.eggp.smart.util.CodeUtil;
import com.eggp.smart.util.StreamUtil;
import com.eggp.smart.util.StringUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 请求助手类
 */
public final class RequestHelper {
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList=new ArrayList<>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request){
        List<FormParam> formParamList=new ArrayList<>();
        Enumeration<String> paramNames=request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String fieldName=paramNames.nextElement();
            String[] fieldValues=request.getParameterValues(fieldName);
            if(ArrayUtil.isNotEmpty(fieldValues)){
                Object fieldValue;
                if(fieldValues.length==1){
                    fieldValue=fieldValues[0];
                }else {
                    StringBuilder stringBuilder=new StringBuilder("");
                    for(int i=0;i<fieldValues.length;i++){
                        stringBuilder.append(fieldValues[i]);
                        if(i!=fieldValues.length-1){
                            stringBuilder.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue=stringBuilder.toString();
                }
                formParamList.add(new FormParam(fieldName,fieldValue));
            }
        }
        return formParamList;
    }

    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList= new ArrayList<>();
        String body= CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if(StringUtil.isNotEmpty(body)){
            String[] kvs=body.split("&");
            if(ArrayUtil.isNotEmpty(kvs)){
                for(String kv:kvs){
                    String[] array=kv.split("=");
                    if(ArrayUtil.isNotEmpty(array)&&array.length==2){
                        String fieldName=array[0];
                        String fieldValue=array[1];
                        formParamList.add(new FormParam(fieldName,fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
