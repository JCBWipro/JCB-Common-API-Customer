package com.wipro.jcb.livelink.app.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:26-07-2024
 * project: JCB-Common-API-Customer
 */
@Data
@Configuration
public class AWSConfig {

    @Value("${spring.mail.fromEmail}")
    private String fromEmail;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean smtpStarttlsEnable;

}
