package com.claim.intercepter;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        Map<String, Object> session = ActionContext.getContext().getSession();

        Object user = session.get("user");

        if (user == null) {

            return "login";

        }

        return invocation.invoke();
    }

}