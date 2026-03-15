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
    
  
	/*
	 * public void validateSaveClaim() {
	 * 
	 * Date today = new Date();
	 * 
	 * if (claim.getClaimNumber() == null ||
	 * claim.getClaimNumber().trim().isEmpty()) { addFieldError("claimNumber",
	 * "Claim Number is required"); }
	 * 
	 * if (claim.getAccidentAddress() == null ||
	 * claim.getAccidentAddress().trim().isEmpty()) {
	 * addFieldError("accidentAddress", "Accident Address is required"); }
	 * 
	 * if (claim.getClaimantName() == null ||
	 * claim.getClaimantName().trim().isEmpty()) { addFieldError("claimantName",
	 * "Claimant Name is required"); }
	 * 
	 * // Accident Date Validation if (claim.getAccidentDate() == null) {
	 * addFieldError("accidentDate", "Accident Date is required"); } else if
	 * (claim.getAccidentDate().after(today)) { addFieldError("accidentDate",
	 * "Accident date cannot be in the future"); }
	 * 
	 * // Claimant DOB Validation if (claim.getClaimantDob() == null) {
	 * addFieldError("claimantDob", "Date of Birth is required"); } else { if
	 * (claim.getClaimantDob().after(today)) { addFieldError("claimantDob",
	 * "Date of Birth cannot be in the future"); } else {
	 * 
	 * // Age Calculation long ageInMillis = today.getTime() -
	 * claim.getClaimantDob().getTime(); long years = ageInMillis / (1000L * 60 * 60
	 * * 24 * 365);
	 * 
	 * if (years < 18) { addFieldError("claimantDob",
	 * "Claimant must be at least 18 years old"); } } } }
	 */
   

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

                System.out.println("Claim submitted: " + existingClaim.getId() +
                        " by user: " + user.getUsername());
            }
        }

        return SUCCESS;
    }

    public void setClaim(Claim claim) { this.claim = claim; }
    public Claim getClaim() { return claim; }

    public List<Claim> getClaims() { return claims; }
    public void setClaims(List<Claim> claims) { this.claims = claims; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}