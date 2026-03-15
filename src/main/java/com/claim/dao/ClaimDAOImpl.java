package com.claim.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.claim.model.Claim;
import com.claim.util.HibernateUtil;

@Repository
public class ClaimDAOImpl implements ClaimDAO {

    @Override
    public void saveClaim(Claim claim) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.save(claim);

        tx.commit();
        session.close();
    }

    @Override
    public List<Claim> getAllClaims() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Claim> list = session.createQuery("from Claim order by id desc", Claim.class).list();

        session.close();

        return list;
    }

    @Override
    public Claim getClaimById(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Claim claim = session.get(Claim.class, id);

        session.close();

        return claim;
    }

    @Override
    public void updateClaim(Claim claim) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        session.update(claim);

        tx.commit();

        session.close();
    }
    
    @Override
    public void deleteClaim(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Claim claim = session.get(Claim.class, id);

        if(claim != null){
            session.delete(claim);
        }

        tx.commit();

        session.close();
    }

}