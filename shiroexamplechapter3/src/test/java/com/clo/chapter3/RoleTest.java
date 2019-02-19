package com.clo.chapter3;

import com.clo.chapter3.util.TestUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class RoleTest {
    @Test
    public void testHasRole() {
        TestUtil.login("classpath:shiro-role.ini", "zhang", "123");
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.hasRole("role1"));
        Assert.assertTrue(subject.hasAllRoles(Arrays.asList("role1", "role2")));
        boolean[] results = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertTrue(results[0]);
        Assert.assertTrue(results[1]);
        Assert.assertFalse(results[2]);
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        TestUtil.login("classpath:shiro-role.ini", "zhang", "123");
        Subject subject = SecurityUtils.getSubject();
        subject.checkRole("role1");
        subject.checkRoles("role1", "role2");
    }
}
