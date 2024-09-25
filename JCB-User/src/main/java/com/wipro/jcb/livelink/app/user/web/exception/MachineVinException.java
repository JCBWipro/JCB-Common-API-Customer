package com.wipro.jcb.livelink.app.user.web.exception;

public class MachineVinException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String message;

	public MachineVinException(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
