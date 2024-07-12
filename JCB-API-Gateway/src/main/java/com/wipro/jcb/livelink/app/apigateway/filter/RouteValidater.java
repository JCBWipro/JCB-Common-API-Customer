package com.wipro.jcb.livelink.app.apigateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidater {
	
	public static final List<String> openEndPoints = List.of(
			"/auth/register",
			"/auth/token",
			"/eureka",
			"/api/message/users/register"
			);
	
	public Predicate<ServerHttpRequest> isSecured = request -> openEndPoints.stream().noneMatch(uri->request.getURI().getPath().contains(uri));	

}
