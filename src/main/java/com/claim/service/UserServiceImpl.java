package com.claim.service;

import com.claim.dao.UserDAO;
import com.claim.dao.UserDAOImpl;
import com.claim.model.User;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String username, String password) {

        return userDAO.login(username, password);

    }

}