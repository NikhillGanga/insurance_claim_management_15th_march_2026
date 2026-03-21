package com.claim.service;

import com.claim.model.User;

public interface UserService {

	User login(String username, String password);
    void save(User user);

}