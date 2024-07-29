package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.config.AWSConfig;
import com.wipro.jcb.livelink.app.auth.model.EmailTemplate;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:26-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class AWSEmailService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AWSEmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AWSConfig awsConfig;

    public void sendEmail(EmailTemplate emailTemplate) {
        List<MsgResponseTemplate> responseTemplates = new ArrayList<>();

        String recipient = emailTemplate.getTo();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipient);
            helper.setFrom(awsConfig.getFromEmail());
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(emailTemplate.getBody(), true);

            // Send email
            javaMailSender.send(message);
            log.info("Email sent successfully to {}", recipient);
            responseTemplates.add(new MsgResponseTemplate("Email sent successfully to " + recipient, true));

        } catch (Exception e) {
            log.error("Error sending email to {}: {}", recipient, e.getMessage());
            responseTemplates.add(new MsgResponseTemplate("Error sending email to " + recipient + ": " + e.getMessage(), false));
        }
    }
}
