package com.claim.action;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.claim.model.User;
import com.claim.service.UserService;
import com.opensymphony.xwork2.ActionSupport;

@Component
public class LoginAction extends ActionSupport {

    private String username;
    private String password;

    @Autowired
    private UserService userService;
    
    
    private User user;

    private static final Logger logger = Logger.getLogger(LoginAction.class);

    private boolean success;
    private String message;

    private static final ConcurrentHashMap<String, LoginAttempt> attemptMap = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final long TIMEOUT_MS = 120_000;
    
    


    public String login() {

        long now = System.currentTimeMillis();
        LoginAttempt attempt = attemptMap.get(username);

        if (attempt != null) {
            if (now - attempt.firstAttemptTime >= TIMEOUT_MS) {
                attemptMap.remove(username);
                attempt = null;
            } else if (attempt.count >= MAX_ATTEMPTS) {
                success = false;
                message = "Too many attempts. Try later.";
                return SUCCESS;
            }
        }

         user = userService.login(username, password);
        
        logger.debug(user);

        if (user != null) {
            attemptMap.remove(username);
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("user", user);

            success = true;
            message = "Login successful";

        } else {
            if (attempt == null) {
                attempt = new LoginAttempt(1, now);
            } else {
                attempt.count++;
            }
            attemptMap.put(username, attempt);

            success = false;
            message = "Invalid credentials";
        }

        return SUCCESS;
    }

    private static class LoginAttempt {
        int count;
        long firstAttemptTime;

        LoginAttempt(int count, long time) {
            this.count = count;
            this.firstAttemptTime = time;
        }
    }
    
    public String logout()
    {
    	HttpSession session = ServletActionContext.getRequest().getSession();
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static ConcurrentHashMap<String, LoginAttempt> getAttemptmap() {
		return attemptMap;
	}

	public static int getMaxAttempts() {
		return MAX_ATTEMPTS;
	}

	public static long getTimeoutMs() {
		return TIMEOUT_MS;
	}

    
}