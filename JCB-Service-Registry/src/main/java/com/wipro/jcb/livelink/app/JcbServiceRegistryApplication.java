package com.wipro.jcb.livelink.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JcbServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcbServiceRegistryApplication.class, args);
	}

}
