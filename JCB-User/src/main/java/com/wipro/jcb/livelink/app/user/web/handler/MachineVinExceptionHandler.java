package com.wipro.jcb.livelink.app.user.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wipro.jcb.livelink.app.user.web.exception.MachineVinException;

@RestControllerAdvice
public class MachineVinExceptionHandler {
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleTokenExpiredException(MachineVinException exception) {
		return exception.getMessage();
	}

}
