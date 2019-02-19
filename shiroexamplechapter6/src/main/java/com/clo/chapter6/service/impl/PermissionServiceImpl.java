package com.clo.chapter6.service.impl;

import com.clo.chapter6.dao.PermissionDao;
import com.clo.chapter6.dao.impl.PermissionDaoImpl;
import com.clo.chapter6.entity.Permission;
import com.clo.chapter6.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {
    private PermissionDao permissionDao = new PermissionDaoImpl();

    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
