package com.claim.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.claim.dto.ApiResponse;
import com.claim.model.Claim;
import com.claim.model.User;
import com.claim.service.ClaimService;
import com.opensymphony.xwork2.ActionSupport;

@Component
public class ClaimAction extends ActionSupport {

	private Claim claim;
	private List<Claim> claims;
	private int id;

	private static final Logger logger = Logger.getLogger(ClaimAction.class);
	@Autowired
	private ClaimService claimService;
	@Autowired
	private ApiResponse apiResponse;

	public String saveClaim() {

		HttpSession session = ServletActionContext.getRequest().getSession();
		User user = (User) session.getAttribute("user");

		if (user != null && "CSR".equalsIgnoreCase(user.getRole())) {
			claim.setStatus("NEW");
		}

		claimService.saveClaim(claim);

		logger.info("Save claim started Claimant Number:[" + claim.getClaimNumber() + "]");

		System.out.println("Save Claim");

		apiResponse.setSuccess(true);
		apiResponse.setData(claim);

		return SUCCESS;
	}

	public void validateSaveClaim() {

	    if (claim == null) {
	        return;
	    }

	    // Claim Number
	    if (claim.getClaimNumber() == null || claim.getClaimNumber().trim().isEmpty()) {
	        addFieldError("claim.claimNumber", "Claim Number is required");
	    }

	    // Claimant Name
	    if (claim.getClaimantName() == null || claim.getClaimantName().trim().isEmpty()) {
	        addFieldError("claim.claimantName", "Claimant Name is required");
	    }

	    // Accident Address
	    if (claim.getAccidentAddress() == null || claim.getAccidentAddress().trim().isEmpty()) {
	        addFieldError("claim.accidentAddress", "Accident Address is required");
	    }

	    // Accident Date
	    if (claim.getAccidentDate() == null) {
	        addFieldError("claim.accidentDate", "Accident Date is required");
	    } else {
	        Date today = new Date();

	        if (claim.getAccidentDate().after(today)) {
	            addFieldError("claim.accidentDate", "Future accident date is not allowed");
	        }
	    }

	    // Claimant DOB
	    if (claim.getClaimantDob() == null) {
	        addFieldError("claim.claimantDob", "Claimant DOB is required");
	    } else {

	        Calendar today = Calendar.getInstance();
	        Calendar dob = Calendar.getInstance();
	        dob.setTime(claim.getClaimantDob());

	        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

	        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
	            age--;
	        }

	        if (age < 18) {
	            addFieldError("claim.claimantDob", "Claimant must be at least 18 years old");
	        }
	    }

	    // return validation errors to UI
	    if (hasFieldErrors()) {
	        apiResponse.setSuccess(false);
	        apiResponse.setFieldErrors(getFieldErrors());
	    }
	}

	public String listClaims() {
		claims = claimService.getAllClaims();
		return SUCCESS;
	}

	public String editClaim() {
		claim = claimService.getClaimById(id);
		logger.info("Edited claim by MANAGER Claimant Number:[" + claim.getClaimNumber() + "]");
		return SUCCESS;
	}

	public String updateClaim() {

	    HttpSession session = ServletActionContext.getRequest().getSession();
	    User user = (User) session.getAttribute("user");

	    // fetch existing claim from DB
	    Claim existingClaim = claimService.getClaimById(claim.getId());

	    if (existingClaim == null) {
	        return ERROR;
	    }

	    // preserve claimNumber (cannot be edited)
	    claim.setClaimNumber(existingClaim.getClaimNumber());

	    if (user != null) {

	        if ("CSR".equalsIgnoreCase(user.getRole())) {

	            claim.setStatus("NEW");
	            logger.info("Updated claim by CSR Claim Number:[" + existingClaim.getClaimNumber() + "]");

	        } 
	        else if ("MANAGER".equalsIgnoreCase(user.getRole())) {

	            claim.setStatus("OPEN");
	            logger.info("Updated claim by MANAGER Claim Number:[" + existingClaim.getClaimNumber() + "]");

	        } 
	        else {
	            return ERROR;
	        }
	    }

	    claimService.updateClaim(claim);

	    apiResponse.setSuccess(true);
	    apiResponse.setData(claim);

	    return SUCCESS;
	}
	public String deleteClaim() {
		claimService.deleteClaim(id);
		logger.info("Deleted claim by MANAGER Claimant Number:[" + claim.getClaimNumber() + "]");
		return SUCCESS;
	}

	public String approveClaim() {
		Claim existingClaim = claimService.getClaimById(id);
		existingClaim.setStatus("APPROVED");
		claimService.updateClaim(existingClaim);
		logger.info("Approved claim by MANAGER Claimant Number:[" + claim.getClaimNumber() + "]");

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

				logger.info("Submitted claim by MANAGER Claimant Number:[" + claim.getClaimNumber() + "]");
				logger.info("Status changed to OPEN by CSR Claimant Number:[" + claim.getClaimNumber() + "]");

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

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

}