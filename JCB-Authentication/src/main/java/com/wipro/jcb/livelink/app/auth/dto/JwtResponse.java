package com.wipro.jcb.livelink.app.auth.dto;

public class JwtResponse {

	private String jwtToken;
	private String tokenId;
	private String error;

	public JwtResponse(String error) {
		this.error = "Authentication failed. Invalid username, password, or role.";
	}

	public JwtResponse() {
	}

	public JwtResponse(String jwtToken, String tokenId, String error) {
		super();
		this.jwtToken = jwtToken;
		this.tokenId = tokenId;
		this.error = "Authentication failed. Invalid username, password, or role.";
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
