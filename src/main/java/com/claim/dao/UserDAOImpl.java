package com.claim.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.claim.model.User;
import com.claim.util.HibernateUtil;
@Repository
public class UserDAOImpl implements UserDAO {

    @Override
    public User login(String username) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        User user = session.createQuery(
                "from User where username = :username",
                User.class)
                .setParameter("username", username)
                .uniqueResult();

        session.close();

        return user;
    }

	@Override
	public void save(User user) {
		 Session session = HibernateUtil.getSessionFactory().openSession();
	        Transaction tx = session.beginTransaction();

	        session.save(user);

	        tx.commit();
	        session.close();
		
	}
    
    
}