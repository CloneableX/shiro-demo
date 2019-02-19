package com.clo.chapter8.web.env;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.*;

import javax.servlet.Filter;

public class MyIniWebEnvironment extends IniWebEnvironment {
    @Override
    public FilterChainResolver getFilterChainResolver() {
        PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();

        DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
        for(DefaultFilter filter : DefaultFilter.values()) {
            filterChainManager.addFilter(filter.name(), (Filter) ClassUtils.newInstance(filter.getFilterClass()));
        }

        filterChainManager.addToChain("/login.jsp", "authc");
        filterChainManager.addToChain("/unauthorized.jsp", "anon");
        filterChainManager.addToChain("/**", "authc");
        filterChainManager.addToChain("/**", "roles", "admin");

        FormAuthenticationFilter authenticationFilter = (FormAuthenticationFilter) filterChainManager.getFilter("authc");
        authenticationFilter.setLoginUrl("/login.jsp");
        RolesAuthorizationFilter authorizationFilter = (RolesAuthorizationFilter) filterChainManager.getFilter("roles");
        authorizationFilter.setUnauthorizedUrl("/unauthorized.jsp");

        filterChainResolver.setFilterChainManager(filterChainManager);
        return filterChainResolver;
    }
}
