package com.eggp.smart.bean;



import com.eggp.smart.util.CastUtil;
import com.eggp.smart.util.CollectionUtil;
import com.eggp.smart.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求参数对象
 */
public class Param {

    private Map<String,Object> paramMap;

    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取请求参数映射
     * @return
     */
    public Map<String,Object> getFiledMap(){
        Map<String,Object> fieldMap=new HashMap<>();
        if(CollectionUtil.isNotEmpty(formParamList)){
            for(FormParam formParam:formParamList){
                String fieldName=formParam.getFieldName();
                Object fieldValue=formParam.getFieldValue();
                if(fieldMap.containsKey(fieldName)){
                    fieldValue=fieldMap.get(fieldName)+ StringUtil.SEPARATOR+fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     * @return
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap= new HashMap<>();
        if(CollectionUtil.isNotEmpty(fileParamList)){
            for(FileParam fileParam:fileParamList){
                String fieldName=fileParam.getFieldName();
                List<FileParam> fileParamList;
                if(fileMap.containsKey(fieldName)){
                    fileParamList=fileMap.get(fieldName);
                }else {
                    fileParamList=new ArrayList<>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     * @param fieldName
     * @return
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     * @param fieldName
     * @return
     */
    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList=getFileList(fieldName);
        if(CollectionUtil.isNotEmpty(fileParamList)&&fileParamList.size()==1){
            return fileParamList.get(0);
        }
        return null;
    }

    /**
     * 验证参数是否为空
     * @return
     */
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(formParamList) &&CollectionUtil.isEmpty(fileParamList);
    }

    /**
     * 获取所有字段信息
     * @return
     */
    public Map<String,Object> getMap(){
        return paramMap;
    }

    /**
     * 根据参数名获取String型参数值
     * @param name
     * @return
     */
    public String getString(String name){
        return CastUtil.castString(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取double型参数值
     * @param name
     * @return
     */
    public double getDouble(String name){
        return CastUtil.castDouble(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取long型参数值
     * @param name
     * @return
     */
    public long getLong(String name){
        return CastUtil.castLong(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取int型参数值
     * @param name
     * @return
     */
    public int getInt(String name){
        return CastUtil.castInt(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取boolean型参数值
     * @param name
     * @return
     */
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFiledMap().get(name));
    }
}
