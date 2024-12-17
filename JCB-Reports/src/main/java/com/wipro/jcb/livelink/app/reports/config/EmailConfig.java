package com.wipro.jcb.livelink.app.reports.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Class for Setting Up Email Properties.
 */
@Data
@Configuration
public class EmailConfig {

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
    
    @Value("${user.feedback.mail.receiver}")
    private String receiverId;

}