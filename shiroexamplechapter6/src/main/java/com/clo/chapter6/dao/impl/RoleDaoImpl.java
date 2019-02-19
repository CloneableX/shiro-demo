package com.clo.chapter6.dao.impl;

import com.clo.chapter6.dao.RoleDao;
import com.clo.chapter6.entity.Role;
import com.clo.chapter6.util.JdbcTemplateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoleDaoImpl implements RoleDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    public Role createRole(final Role role) {
        final String sql = "INSERT INTO sys_roles(role, description, available) VALUES(?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"});
                psst.setString(1, role.getRole());
                psst.setString(2, role.getDescription());
                psst.setBoolean(3, role.getAvailable());
                return psst;
            }
        }, keyHolder);
        role.setId(keyHolder.getKey().longValue());

        return role;
    }

    public void deleteRole(Long roleId) {
        String sql = "DELETE FROM sys_users_roles WHERE role_id=?";
        jdbcTemplate.update(sql, roleId);

        sql = "DELETE FROM sys_roles WHERE role_id=?";
        jdbcTemplate.update(sql, roleId);
    }

    public void correlationPermissions(Long roleId, Long... permissionIds) {
        if(permissionIds == null || permissionIds.length == 0) {
            return;
        }

        String sql = "INSERT INTO sys_roles_permissions(role_id, permission_id) VALUES(?,?)";
        for(Long permissionId : permissionIds) {
            if(!exists(roleId, permissionId)) {
                jdbcTemplate.update(sql, roleId, permissionId);
            }
        }
    }

    public void unCorrelationPermissions(Long roleId, Long... permissionIds) {
        if(permissionIds == null || permissionIds.length == 0) {
            return;
        }

        String sql = "DELETE FROM sys_roles_permissions WHERE role_id=? and permission_id=?";
        for(Long permissionId : permissionIds) {
            if(exists(roleId, permissionId)) {
                jdbcTemplate.update(sql, roleId, permissionId);
            }
        }
    }

    private boolean exists(Long roleId, Long permissionId) {
        String sql = "SELECT COUNT(1) FROM sys_roles_permissions WHERE role_id=? and permission_id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roleId, permissionId) != 0;
    }
}