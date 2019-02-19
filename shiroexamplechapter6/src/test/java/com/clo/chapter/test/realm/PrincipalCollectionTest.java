package com.clo.chapter.test.realm;

import com.clo.chapter.test.BaseTest;
import com.clo.chapter6.entity.User;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

public class PrincipalCollectionTest extends BaseTest {
    @Test
    public void test() {
        login("classpath:shiro-multirealm.ini", "zhang", "123");
        Subject subject = subject();
        Object primaryPrincipal1 = subject.getPrincipal();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Object primaryPrincipal2 = principalCollection.getPrimaryPrincipal();
        Assert.assertEquals(primaryPrincipal1, primaryPrincipal2);

        Set<String> realmNames = principalCollection.getRealmNames();
        System.out.println(realmNames);

        Set<Object> principalSet = principalCollection.asSet();
        System.out.println(principalSet);

        Collection<User> user = principalCollection.fromRealm("c");
        System.out.println(user);
    }
}
