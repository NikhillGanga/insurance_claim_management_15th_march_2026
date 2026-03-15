package com.claim.dao;

import java.util.List;

import com.claim.model.Claim;

public interface ClaimDAO {

	void saveClaim(Claim claim);

	List<Claim> getAllClaims();

	Claim getClaimById(int id);

	void updateClaim(Claim claim);

	void deleteClaim(int id);

}
