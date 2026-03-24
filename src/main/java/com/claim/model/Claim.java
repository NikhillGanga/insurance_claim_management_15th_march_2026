package com.claim.model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

//✅ CORRECT
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "claims")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) // Enable 2nd level cache

public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String claimNumber;

    @PostPersist
    public void generateClaimNumber() {
        this.claimNumber = "CLM" + (1000 + id);
    }

	private Date accidentDate;

	private String accidentAddress;

	private String claimantName;

	private Date claimantDob;

	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Date getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getAccidentAddress() {
		return accidentAddress;
	}

	public void setAccidentAddress(String accidentAddress) {
		this.accidentAddress = accidentAddress;
	}

	public String getClaimantName() {
		return claimantName;
	}

	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}

	public Date getClaimantDob() {
		return claimantDob;
	}

	public void setClaimantDob(Date claimantDob) {
		this.claimantDob = claimantDob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Claim [id=" + id + ", claimNumber=" + claimNumber + ", accidentDate=" + accidentDate
				+ ", accidentAddress=" + accidentAddress + ", claimantName=" + claimantName + ", claimantDob="
				+ claimantDob + ", status=" + status + "]";
	}
	
	
	

}
