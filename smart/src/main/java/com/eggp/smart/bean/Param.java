package com.eggp.smart.bean;

import com.eggp.smart.util.CastUtil;

import java.util.Map;

/**
 * 请求参数对象
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    public Map<String,Object> getMap(){
        return paramMap;
    }
}
