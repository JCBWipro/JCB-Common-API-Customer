package com.wipro.jcb.livelink.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:06-06-2024
 * project: JCB_Microservices
 */
@Configuration
public class KafkaWebClient {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
