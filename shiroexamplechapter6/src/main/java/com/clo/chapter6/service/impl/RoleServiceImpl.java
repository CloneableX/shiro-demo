package com.clo.chapter6.service.impl;

import com.clo.chapter6.dao.RoleDao;
import com.clo.chapter6.dao.impl.RoleDaoImpl;
import com.clo.chapter6.entity.Role;
import com.clo.chapter6.service.RoleService;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao = new RoleDaoImpl();

    public Role createRole(Role role) {
        return roleDao.createRole(role);
    }

    public void deleteRole(Long roleId) {
        roleDao.deleteRole(roleId);
    }

    public void correlationPermissions(Long roleId, Long... permissionIds) {
        roleDao.correlationPermissions(roleId, permissionIds);
    }

    public void unCorrelationPermissions(Long roleId, Long... permissionIds) {
        roleDao.unCorrelationPermissions(roleId, permissionIds);
    }
}
