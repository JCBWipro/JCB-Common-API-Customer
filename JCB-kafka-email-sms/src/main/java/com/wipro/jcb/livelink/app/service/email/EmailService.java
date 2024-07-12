package com.wipro.jcb.livelink.app.service.email;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-06-2024
 * project: msKafka_SMS_Email
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Setter
    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.fromEmail}")
    private String fromEmail;

    public void sendEmail(List<String> toEmails, String subject, String text, Date sentDate) {
        String[] emails = toEmails.toArray(String[]::new);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emails);
            message.setSubject(subject);
            message.setText(text);
            message.setSentDate(sentDate);
            emailSender.send(message);
            log.info("Email sent successfully to: {}", toEmails);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

}
