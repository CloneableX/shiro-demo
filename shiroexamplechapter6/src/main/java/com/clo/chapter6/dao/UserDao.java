package com.clo.chapter6.dao;

import com.clo.chapter6.entity.User;

import java.util.Set;

public interface UserDao {
    public User createUser(User user);
    public User findOne(Long userId);
    public void updateUser(User user);
    public void correlationRoles(Long userId, Long... roleIds);
    public void unCorrelationRoles(Long userId, Long... roleIds);
    public User findByUsername(String username);
    public Set<String> findRoles(String username);
    public Set<String> findPermissions(String username);
}
