package com.wipro.jcb.livelink.app.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;
import com.wipro.jcb.livelink.app.apigateway.config.BlackList;

@RestController
public class LogoutController {
	
	@Autowired
	private BlackList blackList;
	
	@GetMapping("/logout")
	public String logoutUser(ServerWebExchange exchange) {
		String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
		String token = null;
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			token=authHeader.substring(7);
		}
		blackList.blacKListToken(token);
		return "User LoggedOut Successfully!!!";
	}

}
