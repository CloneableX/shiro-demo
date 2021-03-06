package com.clo.chapter.test.realm;

import com.clo.chapter.test.BaseTest;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Assert;
import org.junit.Test;

import java.security.spec.ECField;

public class UserRealmTest extends BaseTest {
    @Test
    public void testLoginSuccess() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().isAuthenticated());
    }

    @Test(expected = UnknownAccountException.class)
    public void testLoginFailWithUnknownUsername() {
        login("classpath:shiro.ini", u1.getUsername() + 1, password);
    }

    @Test(expected = LockedAccountException.class)
    public void testLoginFailWithLockedUsername() {
        login("classpath:shiro.ini", u4.getUsername(), password);
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void testLoginFailWithErrorPassword() {
        login("classpath:shiro.ini", u1.getUsername(), password + 1);
    }

    @Test(expected = ExcessiveAttemptsException.class)
    public void testLoginFailWithLimitRetryCount() {
        for(int i = 0; i < 5; i++) {
            try {
                login("classpath:shiro.ini", u3.getUsername(), password + 1);
            } catch (Exception e) {}
        }

        login("classpath:shiro.ini", u3.getUsername(), password + 1);
    }

    @Test
    public void testHasRole() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().hasRole("admin"));
    }

    @Test
    public void testNoRole() {
        login("classpath:shiro.ini", u2.getUsername(), password);
        Assert.assertFalse(subject().hasRole("admin"));
    }

    @Test
    public void testHasPermission() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().isPermittedAll("user:create", "user:update"));
    }

    @Test
    public void testNoPermission() {
        login("classpath:shiro.ini", u2.getUsername(), password);
        Assert.assertFalse(subject().isPermitted("menu:create"));
    }
}
