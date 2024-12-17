package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.config.AWSConfig;
import com.wipro.jcb.livelink.app.machines.dto.EmailTemplate;
import com.wipro.jcb.livelink.app.machines.dto.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the EmailService interface for sending emails.
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${email.feedback.enable}")
    private boolean feedbackEnable;
    @Value("${user.feedback.mail.receiver}")
    private String receiverId;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AWSConfig awsConfig;

    /**
     *  Sends a feedback email if feedback is enabled.
     * @param feedbackMsg
     * @param userName
     */
   @Override
   public void sendFeedbackEmail(final String feedbackMsg, final String userName) throws ProcessCustomError, IOException {
       if (feedbackEnable) {
           EmailTemplate emailTemplate = new EmailTemplate();
           emailTemplate.setTo(receiverId);
           emailTemplate.setSubject("Feedback from user: " + userName);
           emailTemplate.setBody(createFeedbackEmailBody(userName, feedbackMsg)); // Use helper method

           sendEmail(emailTemplate);
       }
   }

    /**
     * Creates the body of the feedback email by replacing placeholders in the HTML template.
     */
    private String createFeedbackEmailBody(String userName, String feedbackMsg) {
        try {
            Resource resource = new ClassPathResource("templates/feedback-email-template.html");
            InputStream inputStream = resource.getInputStream();
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String emailBody = htmlTemplate.replace("{{userName}}", userName)
                    .replace("{{feedbackMsg}}", feedbackMsg);
            return emailBody;

        } catch (IOException e) {
            log.error("Error reading feedback email template: {}", e.getMessage(), e);
            return "Error loading feedback email template.";
        }
    }

    /**
     * Sends an email using the provided EmailTemplate.
     * @param emailTemplate
     */
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

            javaMailSender.send(message);
            log.info("Email sent successfully to {}", recipient);
            responseTemplates.add(new MsgResponseTemplate("Email sent successfully to " + recipient, true));

        } catch (Exception e) {
            log.error("Error sending email to {}: {}", recipient, e.getMessage());
            responseTemplates.add(new MsgResponseTemplate("Error sending email to " + recipient + ": " + e.getMessage(), false));
        }
    }
}
