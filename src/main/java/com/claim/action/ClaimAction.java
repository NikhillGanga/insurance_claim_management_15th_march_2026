package com.claim.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.claim.model.Claim;
import com.claim.model.User;
import com.claim.service.ClaimService;
import com.opensymphony.xwork2.ActionSupport;

@Component
public class ClaimAction extends ActionSupport {

	private Claim claim;
	private List<Claim> claims;
	private int id;

	@Autowired
	private ClaimService claimService;

	public Map<String, List<String>> getFieldErrors() {
		return super.getFieldErrors();
	}

	public String saveClaim() {

		if (hasFieldErrors()) {
			return INPUT;
		}

		HttpSession session = ServletActionContext.getRequest().getSession();
		User user = (User) session.getAttribute("user");

		if (user != null && "CSR".equalsIgnoreCase(user.getRole())) {
			claim.setStatus("NEW");
		}

		claimService.saveClaim(claim);
		return SUCCESS;
	}

	public String listClaims() {
		claims = claimService.getAllClaims();
		return SUCCESS;
	}

	public String editClaim() {
		claim = claimService.getClaimById(id);
		return SUCCESS;
	}

	public String updateClaim() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if ("CSR".equalsIgnoreCase(user.getRole()) && "NEW".equalsIgnoreCase(claim.getStatus())) {

			} else if ("MANAGER".equalsIgnoreCase(user.getRole())) {

				claim.setStatus("OPEN");
			} else {
				return ERROR;
			}
		}

		claimService.updateClaim(claim);
		return SUCCESS;
	}

	public String deleteClaim() {
		claimService.deleteClaim(id);
		return SUCCESS;
	}

	public String approveClaim() {
		Claim existingClaim = claimService.getClaimById(id);
		existingClaim.setStatus("APPROVED");
		claimService.updateClaim(existingClaim);

		return SUCCESS;
	}

	public String submitClaim() {

		HttpSession session = ServletActionContext.getRequest().getSession();
		User user = (User) session.getAttribute("user");

		if (user != null && "CSR".equalsIgnoreCase(user.getRole())) {

			Claim existingClaim = claimService.getClaimById(id);

			if (existingClaim != null && "NEW".equalsIgnoreCase(existingClaim.getStatus())) {

				existingClaim.setStatus("OPEN");
				claimService.updateClaim(existingClaim);

				System.out.println("Claim submitted: " + existingClaim.getId() + " by user: " + user.getUsername());
			}
		}

		return SUCCESS;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Claim getClaim() {
		return claim;
	}

	public List<Claim> getClaims() {
		return claims;
	}

	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}