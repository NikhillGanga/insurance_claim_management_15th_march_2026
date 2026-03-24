package com.claim.dto;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.claim.model.Claim;

@Component
public class ApiResponse {

	private boolean success;
	private Map<String, List<String>>  fieldErrors;
	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	

	

	@Override
	public String toString() {
		return "ApiResponse [success=" + success + ", errors=" + fieldErrors + "]";
	}

	public Map<String, List<String>> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
	
}
