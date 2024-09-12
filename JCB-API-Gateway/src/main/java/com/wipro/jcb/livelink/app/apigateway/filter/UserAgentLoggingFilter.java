package com.wipro.jcb.livelink.app.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-08-2024
 * project: JCB-Common-API-Customer
 */
@Component
public class UserAgentLoggingFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(UserAgentLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //Custom logic based on User-Agent
        String userAgent = request.getHeaders().getFirst("User-Agent");

        try {
            if (userAgent != null) {
                log.info("Request from User-Agent: {}", userAgent);
                // Adding logic specific to web browsers
                if (userAgent.contains("Mozilla") || userAgent.contains("Chrome") || userAgent.contains("Edge") || userAgent.contains("Safari")) {
                    log.info("This is a request from a web browser.");
                    // Adding logic specific to mobile
                } else if (userAgent.contains("okhttp") || userAgent.contains("Android") || userAgent.contains("iPhone") || userAgent.contains("iPad")) {
                    log.info("This is a request from a mobile application.");
                } else {
                    log.warn("Unknown User-Agent: {}", userAgent);
                }
            } else {
                log.warn("User-Agent header is missing.");
            }
        } catch (Exception e) {
            log.error("Error processing User-Agent: {}", e.getMessage(), e);
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User-Agent"));
        }

        return chain.filter(exchange);
    }
}
