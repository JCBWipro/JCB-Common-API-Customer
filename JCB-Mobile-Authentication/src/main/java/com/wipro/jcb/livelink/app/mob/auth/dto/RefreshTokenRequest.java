package com.wipro.jcb.livelink.app.mob.auth.dto;

public class RefreshTokenRequest {
	
	private String token;

	public RefreshTokenRequest() {
	}

	public RefreshTokenRequest(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}