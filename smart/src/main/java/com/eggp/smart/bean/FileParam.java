package com.eggp.smart.bean;

import java.io.InputStream;

/**
 * 封装文件上传参数
 */
public class FileParam {
    // 表单的字段名
    private String fieldName;
    // 文件名
    private String fileName;
    private long fileSize;
    // 文件类型
    private String contentType;
    // 字节输入流
    private InputStream inputStream;

    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
