package com.claim.dao;

import org.hibernate.Session;

import com.claim.model.User;
import com.claim.util.HibernateUtil;

public class UserDAOImpl implements UserDAO {

	@Override
	public User login(String username, String password) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		User user = session.createQuery("from User where username = :username and password = :password", User.class)
				.setParameter("username", username).setParameter("password", password).uniqueResult();

		session.close();

		return user;

	}

}
