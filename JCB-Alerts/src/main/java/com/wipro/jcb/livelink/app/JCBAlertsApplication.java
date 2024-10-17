package com.wipro.jcb.livelink.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:05-09-2024
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class JCBAlertsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JCBAlertsApplication.class, args);
    }
}