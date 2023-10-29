package com.bezkoder.spring.login.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String username;
	private String signature;
	private String address;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
