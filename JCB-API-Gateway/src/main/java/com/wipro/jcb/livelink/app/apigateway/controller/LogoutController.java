package com.wipro.jcb.livelink.app.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.wipro.jcb.livelink.app.apigateway.config.BlackList;

@RestController
public class LogoutController {

	@Autowired
	BlackList blackList;

	@GetMapping("/logout")
	public ResponseEntity<String> logoutUser(ServerWebExchange exchange) {
		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}

		if (token != null) {
			blackList.blacKListToken(token);
			return ResponseEntity.ok("User Logged Out Successfully!!!");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token.");
		}
	}
}
