package com.wipro.jcb.livelink.app.mob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JcbMobileAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcbMobileAuthenticationApplication.class, args);
	}

}
