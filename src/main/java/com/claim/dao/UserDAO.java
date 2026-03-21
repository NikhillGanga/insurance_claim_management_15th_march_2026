package com.claim.dao;

import com.claim.model.User;

public interface UserDAO {

User login(String username);

void save(User user);

}
