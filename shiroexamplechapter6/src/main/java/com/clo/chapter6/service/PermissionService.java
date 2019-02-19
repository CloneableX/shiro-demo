package com.clo.chapter6.service;

import com.clo.chapter6.entity.Permission;

public interface PermissionService {
    public Permission createPermission(Permission permission);
    public void deletePermission(Long permissionId);
}
