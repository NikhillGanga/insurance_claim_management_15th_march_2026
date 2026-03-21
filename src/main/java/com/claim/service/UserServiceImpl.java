package com.claim.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claim.dao.UserDAO;
import com.claim.model.User;

@Service
public class UserServiceImpl implements UserService {

	 @Autowired
	    private UserDAO userDAO;

	    
	   

	    @Override
	    public User login(String username, String password) {

	        User user = userDAO.login(username);

	        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
	            return user;
	        }

	        return null;
	    }


    @Override
    public void save(User user) {

        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hash);
        user.setRole("CSR");

        userDAO.save(user);
    }
}