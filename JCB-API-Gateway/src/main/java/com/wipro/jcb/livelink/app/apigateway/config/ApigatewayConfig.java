package com.wipro.jcb.livelink.app.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApigatewayConfig {
	
	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

}
