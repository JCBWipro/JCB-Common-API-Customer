package com.wipro.jcb.livelink.app;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JcbKafkaEmailSmsApplication {

	private static final Logger log = LoggerFactory.getLogger(JcbKafkaEmailSmsApplication.class);

	public static void main(String[] args) {

		log.info("Starting Kafka Application");
		SpringApplication.run(JcbKafkaEmailSmsApplication.class, args);
		log.info("Jcb Kafka Email&Sms Application Started");
	}

}
