package com.eggp.smart.plugin.security;

import java.util.Set;

/**
 * Smart Security 接口
 * 可在应用中实现该接口，或者在smart.properties文件中提供以下基于sql的配置项
 */
public interface SmartSecurity {
    String getPassword(String username);
    Set<String> getRoleNameSet(String username);
    Set<String> getPermissionNameSet(String roleName);
}
