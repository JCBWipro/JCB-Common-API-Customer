package com.wipro.jcb.livelink.app.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.apigateway.config.BlackList;
import com.wipro.jcb.livelink.app.apigateway.constants.ApiGatewayConstants;
import com.wipro.jcb.livelink.app.apigateway.exception.TokenException;
import com.wipro.jcb.livelink.app.util.JwtUtils;
import com.google.common.net.HttpHeaders;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
	
	//We are Calling this AuthenticationFilter before redirecting request to corresponding MicroServices by defining filters in properties file

	@Autowired
	private RouteValidater routeValidater;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private BlackList blackList;

	AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest role = null;
			if (routeValidater.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new TokenException(ApiGatewayConstants.MISSING_AUTHORIZATION_HEADER);
				}

				// If Header contains the token, then get that header
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if(authHeader!=null && authHeader.startsWith(ApiGatewayConstants.BEARER)) {
					//Removing Extra 7 Spaces contains in Bearer Token
					authHeader=authHeader.substring(7);
				}
				
//				if(blackList.isBlackListed(authHeader)) {
//					throw new RuntimeException("Session or Token is Expired.");
//				}
				try {
					jwtUtils.validateToken(authHeader);
					role = exchange.getRequest().mutate().header(ApiGatewayConstants.LOGGED_IN_USER_ROLE, jwtUtils.extractRole(authHeader)).build();
				}catch(Exception e) {
					throw new TokenException(ApiGatewayConstants.INVALID_TOKEN);
				}
			}
            assert role != null;
            return chain.filter(exchange.mutate().request(role).build());
		});
	}

	public static class Config {

	}

}
