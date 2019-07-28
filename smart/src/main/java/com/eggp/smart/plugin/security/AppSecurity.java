package com.eggp.smart.plugin.security;

import com.eggp.smart.helper.DatabaseHelper;

import java.util.Set;

public class AppSecurity implements SmartSecurity {
    @Override
    public String getPassword(String username) {
        String sql="select password from user where username = ?";

        return DatabaseHelper.q
    }

    @Override
    public Set<String> getRoleNameSet(String username) {
        return null;
    }

    @Override
    public Set<String> getPermissionNameSet(String roleName) {
        return null;
    }
}
