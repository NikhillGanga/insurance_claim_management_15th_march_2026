package com.claim.intercepter;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.claim.model.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class RoleInterceptor extends AbstractInterceptor {

    private String allowedRole;

    public void setAllowedRole(String allowedRole) {
        this.allowedRole = allowedRole;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        HttpSession session = ServletActionContext.getRequest().getSession(false);

        if (session == null) {
            return "login";
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "login";
        }

        if (allowedRole != null && !allowedRole.equalsIgnoreCase(user.getRole())) {
            return "unauthorized";
        }

        return invocation.invoke();
    }
    
}