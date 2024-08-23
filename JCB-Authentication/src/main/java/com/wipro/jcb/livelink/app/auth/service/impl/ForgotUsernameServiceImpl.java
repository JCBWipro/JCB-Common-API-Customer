package com.wipro.jcb.livelink.app.auth.service.impl;

import com.wipro.jcb.livelink.app.auth.model.EmailTemplate;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.AWSEmailService;
import com.wipro.jcb.livelink.app.auth.service.ForgotUsernameService;
import com.wipro.jcb.livelink.app.auth.service.UnicelSmsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ForgotUsernameServiceImpl extends ForgotUsernameService {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ForgotUsernameServiceImpl.class);
    @Autowired
    ContactRepo contactRepo;

    @Autowired
    UnicelSmsService unicelSmsService;

    @Autowired
    AWSEmailService awsEmailService;


    public MsgResponseTemplate forgotUsername(String mobileNumber, String emailId) {
        try {
            String username;
            String firstName = contactRepo.findFirstNameFromID(emailId);
            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                username = contactRepo.findByMobileNumber(mobileNumber);
                if (username == null) {
                    return new MsgResponseTemplate("No user found with this mobile number.", false);
                }
                // Send SMS with username
                SMSTemplate smsTemplate = new SMSTemplate();
                smsTemplate.setTo(Collections.singletonList(mobileNumber));
                String body = "Your login id registered with JCB LiveLink is " + username +
                        " . JCB LiveLink Team.; Dt: -JCB LiveLink";
                smsTemplate.setMsgBody(Collections.singletonList(body));
                unicelSmsService.sendSms(smsTemplate);

                return new MsgResponseTemplate("Username sent to mobile number: " + mobileNumber, true);

            } else if (emailId != null && !emailId.trim().isEmpty()) {
                username = contactRepo.findByEmailId(emailId);
                if (username == null) {
                    return new MsgResponseTemplate("No user found with this email ID.", false);
                }
                // Send email with username
                EmailTemplate emailTemplate = new EmailTemplate();
                emailTemplate.setTo(emailId);
                emailTemplate.setSubject("Your login details registered with JCB LiveLink");

                try {
                    Resource resource = new ClassPathResource("forgot_username_email.html");
                    InputStream inputStream = resource.getInputStream();
                    String emailBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    emailBody = emailBody.replace("{{firstName}}", firstName);
                    emailBody = emailBody.replace("{{username}}", username);
                    emailTemplate.setBody(emailBody);
                    awsEmailService.sendEmail(emailTemplate);
                } catch (IOException e) {
                    log.error("Error reading email template: {}", e.getMessage(), e);
                }
                return new MsgResponseTemplate("Username sent to email ID: " + emailId, true);
            } else {
                return new MsgResponseTemplate("Either mobile number or email ID is required.", false);
            }

        } catch (Exception e) {
            log.error("Error retrieving username: {}", e.getMessage(), e);
            return new MsgResponseTemplate("An unexpected error occurred. Please try again later.", false);
        }
    }
}
