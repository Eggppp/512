package com.eggp.smart.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * 封装请求信息
 */
public class Request {
    private String requestMethod;
    private String requestPath;

    public Request(String requestMethod, String requestPath){
        this.requestMethod=requestMethod;
        this.requestPath=requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this,object);
    }


}
