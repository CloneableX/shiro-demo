package com.clo.chapter2.authenticator.strategy;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

public class OnlyOneAuthenticatorStrategy extends AbstractAuthenticationStrategy {
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }

    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if(singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if(aggregateInfo == null) {
                info = singleRealmInfo;
            } else {
                info = merge(singleRealmInfo, aggregateInfo);
                if(info.getPrincipals().getRealmNames().size() > 1) {
                    System.out.println(info.getPrincipals().getRealmNames());
                    throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                            "could not be authenticated by any configured realms.  Please ensure that only one realm can " +
                            "authenticate these tokens.");
                }
            }
        }

        return info;
    }

    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}
