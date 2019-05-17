package com.eggp.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StreamUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream inputStream){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            LOGGER.error("get String failure",e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
