package com.eggp.smart.util;

/**
 * 转型操作工具类
 */
public class CastUtil {

    /**
     * 转为String
     * @param object
     * @return
     */
    public static String castString(Object object){
        return CastUtil.castString(object,"");
    }

    /**
     * 转为String（提供默认值）
     * @param object
     * @param defaultValue
     * @return
     */
    public static String castString(Object object,String defaultValue){
        return object !=null ?String.valueOf(object):defaultValue;
    }

    /**
     * 转为double
     * @param object
     * @return
     */
    public static double castDouble(Object object){
        return CastUtil.castDouble(object,0);
    }

    /**
     * 转为double（提供默认值）
     * @param object
     * @param defaultValue
     * @return
     */
    public static double castDouble(Object object,double defaultValue){
        double doubleValue = defaultValue;
        if(object!=null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try {
                    doubleValue = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为long
     * @param object
     * @return
     */
    public static long castLong(Object object){
        return castLong(object,0);
    }

    /**
     * 转为long（提供默认值）
     * @param object
     * @param defaultValue
     * @return
     */
    public static long castLong(Object object,long defaultValue){
        long longValue = defaultValue;
        if(object!=null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    longValue = Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为int
     * @param object
     * @return
     */
    public static int castInt(Object object){
        return castInt(object,0);
    }

    /**
     * 转为int（提供默认值）
     * @param object
     * @param defaultValue
     * @return
     */
    public static int castInt(Object object,int defaultValue){
        int intValue = defaultValue;
        if(object!=null){
            String strValue = castString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转为boolean
     * @param object
     * @return
     */
    public static boolean castBoolean(Object object){
        return CastUtil.castBoolean(object,false);
    }

    /**
     * 转为boolean（提供默认值）
     * @param object
     * @param defaultValue
     * @return
     */
    public static boolean castBoolean(Object object,boolean defaultValue){
        boolean booleanValue=defaultValue;
        if(object!=null){
            booleanValue=Boolean.parseBoolean(castString(object));
        }
        return booleanValue;
    }

}
