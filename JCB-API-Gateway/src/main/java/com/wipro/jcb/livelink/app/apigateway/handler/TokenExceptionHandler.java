package com.wipro.jcb.livelink.app.apigateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wipro.jcb.livelink.app.apigateway.exception.TokenException;

@RestControllerAdvice
public class TokenExceptionHandler {
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String handleTokenExpiredException(TokenException exception) {
		return exception.getMessage();
	}

}
