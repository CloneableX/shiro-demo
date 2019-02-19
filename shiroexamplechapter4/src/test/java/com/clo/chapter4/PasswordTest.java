package com.clo.chapter4;

import com.clo.chapter4.util.TestUtil;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

public class PasswordTest {
    @Test
    public void testPasswordServiceWithMyRealm() {
        TestUtil.login("classpath:shiro-passwordservice.ini", "wu", "123");
    }

    @Test
    public void testPasswordServiceWithJdbcRealm() {
        TestUtil.login("classpath:shiro-jdbc-passwordservice.ini", "wu", "123");
    }

    @Test
    public void testHashedCredentialsMatcherWithMyRealm1() {
        TestUtil.login("classpath:shiro-hashcredentialsmatcher.ini", "liu", "123");
    }

    @Test
    public void testHashedCredentialsMatcherWithJdbcRealm() {
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        TestUtil.login("classpath:shiro-jdbc-hashcredentialsmatcher.ini", "liu", "123");
    }

    private class EnumConverter extends AbstractConverter {

        @Override
        protected String convertToString(Object value) throws Throwable {
            return ((Enum) value).name();
        }

        protected Object convertToType(Class aClass, Object o) throws Throwable {
            return Enum.valueOf(aClass, o.toString());
        }

        protected Class getDefaultType() {
            return null;
        }
    }

    @Test(expected = ExcessiveAttemptsException.class)
    public void testRetryLimitHashedCredentialsMatcher() {
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        for(int i = 0; i < 6; i++) {
            try {
                TestUtil.login("classpath:shiro-jdbc-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
            } catch (Exception e) {}
        }

        TestUtil.login("classpath:shiro-jdbc-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
    }
}
