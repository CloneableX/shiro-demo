package com.clo.chapter3.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

public class BitAndWildPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionStr) {
        if(permissionStr.startsWith("+")) {
            return new BitPermission(permissionStr);
        }
        return new WildcardPermission(permissionStr);
    }
}
