package com.clo.chapter4.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class TestUtil {
    public static void login(String configFile, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }
}
