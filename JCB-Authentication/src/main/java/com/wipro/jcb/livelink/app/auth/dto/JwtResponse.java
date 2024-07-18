package com.wipro.jcb.livelink.app.auth.dto;

public class JwtResponse {
	
	private String accessToken;
    private String token;
    private String error;
    
    public JwtResponse(String error) {
    	this.error = "Authentication failed. Invalid username, password, or role.";
    }
    
    public JwtResponse() {}

	public JwtResponse(String accessToken, String token, String error) {
		super();
		this.accessToken = accessToken;
		this.token = token;
		this.error = "Authentication failed. Invalid username, password, or role.";
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
    
}
