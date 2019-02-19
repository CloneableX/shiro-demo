package com.clo.chapter6.dao.impl;

import com.clo.chapter6.dao.UserDao;
import com.clo.chapter6.entity.User;
import com.clo.chapter6.util.JdbcTemplateUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    public User createUser(final User user) {
        final String sql = "INSERT INTO sys_users(username, password, salt, locked) VALUES(?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"});
                psst.setString(1, user.getUsername());
                psst.setString(2, user.getPassword());
                psst.setString(3, user.getSalt());
                psst.setBoolean(4, user.getLocked());
                return psst;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public User findOne(Long userId) {
        String sql = "SELECT id, username, password, salt, locked from sys_users where id=?";
        return jdbcTemplate.queryForObject(sql, User.class, userId);
    }

    public void updateUser(User user) {
        String sql = "UPDATE sys_users SET username=?, password=?, salt=?, locked=? WHERE id=?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getSalt(), user.getLocked(), user.getId());
    }

    public void correlationRoles(Long userId, Long... roleIds) {
        String sql = "INSERT INTO sys_users_roles(user_id, role_id) VALUES(?,?)";
        for(Long roleId : roleIds) {
            if(!exists(userId, roleId)) {
                jdbcTemplate.update(sql, userId, roleId);
            }
        }
    }

    public void unCorrelationRoles(Long userId, Long... roleIds) {
        String sql = "DELETE FROM sys_users_roles where user_id=? and role_id=?";
        for(Long roleId : roleIds) {
            if(exists(userId, roleId)) {
                jdbcTemplate.update(sql, userId, roleId);
            }
        }
    }

    public boolean exists(Long userId, Long roleId) {
        String sql = "SELECT COUNT(1) from sys_users_roles where user_id=? and role_id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, roleId) != 0;
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, salt, locked from sys_users where username=?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), username);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    public Set<String> findRoles(String username) {
        String sql = "SELECT sr.role FROM sys_users su, sys_roles sr, sys_users_roles sur where su.username=? and su.id=sur.user_id and sr.id=sur.role_id";
        return new HashSet(jdbcTemplate.queryForList(sql, String.class, username));
    }

    public Set<String> findPermissions(String username) {
        String sql = "SELECT sp.permission FROM sys_users su, sys_users_roles sur, sys_roles_permissions srp, sys_permissions sp " +
                "where su.username=? and su.id=sur.user_id and sur.role_id=srp.role_id and srp.permission_id=sp.id";
        return new HashSet(jdbcTemplate.queryForList(sql, String.class, username));
    }
}
