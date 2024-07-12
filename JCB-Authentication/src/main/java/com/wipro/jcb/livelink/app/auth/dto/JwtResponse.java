package com.wipro.jcb.livelink.app.auth.dto;

public class JwtResponse {
	
	private String accessToken;
    private String token;
    
    public JwtResponse() {}

	public JwtResponse(String accessToken, String token) {
		super();
		this.accessToken = accessToken;
		this.token = token;
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
    
}
