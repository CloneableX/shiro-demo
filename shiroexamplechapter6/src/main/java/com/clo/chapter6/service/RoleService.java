package com.clo.chapter6.service;

import com.clo.chapter6.entity.Role;

public interface RoleService {
    public Role createRole(Role role);
    public void deleteRole(Long roleId);
    public void correlationPermissions(Long roleId, Long... permissionIds);
    public void unCorrelationPermissions(Long roleId, Long... permissionIds);
}
