package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.model.EmailTemplate;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ContactPasswordUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ContactPasswordUpdateService.class);
    @Autowired
    ContactRepo contactRepo;

    @Autowired
    UnicelSmsService unicelSmsService;

    @Autowired
    AWSEmailService awsEmailService;


    public int isFirstLogin(String userName) {
        return contactRepo.checkSysGenPassByContactID(userName);
    }

    public MsgResponseTemplate updatePassword(String username, String newPassword) {
        try {
            ContactEntity contactEntity = contactRepo.findByUserContactId(username);
            if (contactEntity == null) {
                return new MsgResponseTemplate("User not found", false);
            }

            String mobileNumber = contactEntity.getPrimary_mobile_number();
            String emailId = contactEntity.getPrimary_email_id();

            // Prepare and send SMS
            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                SMSTemplate smsTemplate = new SMSTemplate();
                smsTemplate.setTo(Collections.singletonList(mobileNumber));
                String body = "Your new password registered with JCB LiveLink is " + newPassword +
                        " . JCB LiveLink Team.; Dt:";
                smsTemplate.setMsgBody(Collections.singletonList(body));
                unicelSmsService.sendSms(smsTemplate);
            }

            // Prepare and send email
            if (emailId != null && !emailId.trim().isEmpty()) {
                try {
                    // Load the HTML template
                    Resource resource = new ClassPathResource("templates/password_update_email.html");
                    InputStream inputStream = resource.getInputStream();
                    String emailBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    // Replace placeholders with actual values
                    emailBody = emailBody.replace("{{newPassword}}", newPassword);
                    EmailTemplate emailTemplate = new EmailTemplate();
                    emailTemplate.setTo(emailId);
                    emailTemplate.setSubject("Your new password for JCB LiveLink");
                    emailTemplate.setBody(emailBody);
                    awsEmailService.sendEmail(emailTemplate);
                } catch (IOException e) {
                    log.error("Error reading email template: {}", e.getMessage(), e);
                }
            }
            // Reset the first login flag after updating the password
            contactEntity.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            contactEntity.setSysGeneratedPassword(0);
            contactRepo.save(contactEntity);

            return new MsgResponseTemplate("Password updated successfully", true);

        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage(), e);
            return new MsgResponseTemplate("An unexpected error occurred. Please try again later.", false);
        }
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 12 || password.length() > 45) {
            return false;
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecialChar = true;
            }
        }

        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar && !password.toUpperCase().contains("JCB");
    }
        /*List<String> last5Passwords = passwordHistoryService.getLast5Passwords(username);
        return !last5Passwords.contains(password);*/
}
