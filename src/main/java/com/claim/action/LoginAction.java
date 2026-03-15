package com.claim.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.claim.model.User;
import com.claim.service.UserService;
import com.claim.service.UserServiceImpl;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

    private String username;
    private String password;

    private UserService userService = new UserServiceImpl();

    public String login() {

        User user = userService.login(username, password);

        if(user != null){

            HttpSession session = ServletActionContext.getRequest().getSession();

            session.setAttribute("user", user);

            return SUCCESS;
        }

        return ERROR;
    }

    public String logout() {

        HttpSession session =
                ServletActionContext.getRequest().getSession();

        session.invalidate();

        return SUCCESS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}