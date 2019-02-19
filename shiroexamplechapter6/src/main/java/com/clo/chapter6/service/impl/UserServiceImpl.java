package com.clo.chapter6.service.impl;

import com.clo.chapter6.dao.UserDao;
import com.clo.chapter6.dao.impl.UserDaoImpl;
import com.clo.chapter6.entity.User;
import com.clo.chapter6.service.UserService;
import com.clo.chapter6.util.PasswordHelper;

import java.util.Set;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    private PasswordHelper passwordHelper = new PasswordHelper();

    public User createUser(User user) {
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userDao.findOne(userId);
        if(user != null) {
            user.setPassword(newPassword);
            passwordHelper.encryptPassword(user);
            userDao.updateUser(user);
        }
    }

    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    public void unCorrelationRoles(Long userId, Long... roleIds) {
        userDao.unCorrelationRoles(userId, roleIds);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
}
