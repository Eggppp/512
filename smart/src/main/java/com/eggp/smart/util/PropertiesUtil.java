package com.eggp.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 加载属性文件
     * @param fileName
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream==null){
                throw new FileNotFoundException(fileName + "file is not found");
            }
            properties = new Properties();
            properties.load(inputStream);

        } catch (IOException e){
            LOGGER.error("load properties file failure",e);
        } finally {
            if(inputStream!=null){
                try{
                    inputStream.close();
                } catch (IOException e){
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取字符型属性，默认空字符串
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }

    /**
     * 获取字符型属性，可以指定默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties,String key,String defaultValue){
        String value = defaultValue;
        if(properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties,String key){
        return getInt(properties,key,0);
    }

    /**
     * 获取数值型属性（可指定默认值）
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties,String key,int defaultValue){
        int value = defaultValue;
        if(properties.containsKey(key)){
            value=CastUtil.castInt(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取boolean属性，默认false
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }

    /**
     * 获取boolean属性（可指定默认值）
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties properties,String key,boolean defaultValue){
        boolean value = defaultValue;
        if(properties.containsKey(key)){
            value=CastUtil.castBoolean(properties.get(key));
        }
        return value;
    }



}
