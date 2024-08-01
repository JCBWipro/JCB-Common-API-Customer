package com.wipro.jcb.livelink.app.apigateway.exception;

public class TokenException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String message;

	public TokenException(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
