package com.clo.chapter6.dao;

import com.clo.chapter6.entity.Role;

public interface RoleDao {
    public Role createRole(Role role);
    public void deleteRole(Long roleId);
    public void correlationPermissions(Long roleId, Long... permissionIds);
    public void unCorrelationPermissions(Long roleId, Long... permissionIds);
}
