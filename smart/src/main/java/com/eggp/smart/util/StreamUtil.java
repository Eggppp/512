package com.eggp.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

    public static void copyStream(InputStream inputStream, OutputStream outputStream){
        try{
            int length;
            byte[] buffer=new byte[4*1024];
            while ((length=inputStream.read(buffer,0,buffer.length))!=-1){
                outputStream.write(buffer,0,buffer.length);
            }
            outputStream.flush();
        }catch (Exception e){
            LOGGER.error("copy stream failure",e);
            throw new RuntimeException(e);
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            }catch (Exception e){
                LOGGER.error("close stream failure",e);
            }
        }
    }
}
