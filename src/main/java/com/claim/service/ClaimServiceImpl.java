package com.claim.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claim.dao.ClaimDAO;
import com.claim.model.Claim;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

	  @Autowired
	private ClaimDAO claimDAO;

	@Override
	public void saveClaim(Claim claim) {

		claim.setStatus("NEW");

		claimDAO.saveClaim(claim);

	}

	@Override
	public List<Claim> getAllClaims() {

		return claimDAO.getAllClaims();
	}

	@Override
	public Claim getClaimById(int id) {

		return claimDAO.getClaimById(id);
	}

	@Override
	public void updateClaim(Claim claim) {

		claimDAO.updateClaim(claim);
	}
	
	   @Override
	    public void deleteClaim(int id) {

	        claimDAO.deleteClaim(id);

	    }

}
