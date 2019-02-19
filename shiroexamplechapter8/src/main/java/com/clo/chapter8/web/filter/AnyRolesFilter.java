package com.clo.chapter8.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AnyRolesFilter extends AccessControlFilter {
    private String loginUrl = "/login.jsp";
//    private String unauthorizedUrl = "/unauthorized.jsp";
    private String unauthorizedUrl = "";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String[] roles = (String[]) mappedValue;
        if(roles == null || roles.length < 1) {
            return true;
        }

        for(String role : roles) {
            if(getSubject(request, response).hasRole(role)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if(getSubject(request, response).getPrincipal() == null) {
            saveRequest(request);
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            if(StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }
}
