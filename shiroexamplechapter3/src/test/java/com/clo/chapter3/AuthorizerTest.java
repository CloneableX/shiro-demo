package com.clo.chapter3;

import com.clo.chapter3.util.TestUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

public class AuthorizerTest {
    @Test
    public void testIsPermitted() {
        TestUtil.login("classpath:shiro-authorizer.ini", "wang", "123");

        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.isPermitted("user1:update"));
        Assert.assertTrue(subject.isPermitted("user2:update"));

        Assert.assertTrue(subject.isPermitted("+user1+2"));
        Assert.assertTrue(subject.isPermitted("+user1+8"));
        Assert.assertTrue(subject.isPermitted("+user2+10"));

        Assert.assertFalse(subject.isPermitted("+user1+4"));

        Assert.assertTrue(subject.isPermitted("menu:view"));
    }

    @Test
    public void testJdbcIsPermitted() {
        TestUtil.login("classpath:shiro-jdbc-authorizer.ini", "zhang", "123");

        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.isPermitted("user1:update"));
        Assert.assertTrue(subject.isPermitted("user2:update"));

        Assert.assertTrue(subject.isPermitted("+user1+2"));
        Assert.assertTrue(subject.isPermitted("+user1+8"));
        Assert.assertTrue(subject.isPermitted("+user2+10"));

        Assert.assertFalse(subject.isPermitted("+user1+4"));

        Assert.assertTrue(subject.isPermitted("menu:view"));
    }
}
