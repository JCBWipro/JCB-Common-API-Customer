package com.wipro.jcb.livelink.app.auth.service.impl;

import com.wipro.jcb.livelink.app.auth.exception.PasswordUpdateException;
import com.wipro.jcb.livelink.app.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.auth.model.SmsResponse;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.ResetPasswordService;
import com.wipro.jcb.livelink.app.auth.service.UnicelSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-07-2024
 * project: JCB-New-Common-API
 */
@Service
public class ResetPasswordServiceImpl extends ResetPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ResetPasswordServiceImpl.class);

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UnicelSmsService unicelSmsService;

    public SmsResponse resetPassword(String userName) {
        try {
            if (userName.isEmpty()) {
                return new SmsResponse("Invalid username provided.", false);
            }

            String mobileNumber = contactRepo.findMobileNumberByUserID(userName);
            if (mobileNumber == null) {
                return new SmsResponse("User not found.", false);
            }

            String autoGeneratedPassword = generatePassword();
            log.info("Password is before sending to DB: {}", autoGeneratedPassword);

            // Update password in the database first
            resetPassword(autoGeneratedPassword, mobileNumber);

            // Prepare SMS template
            SMSTemplate smsTemplate = new SMSTemplate();
            smsTemplate.setTo(Collections.singletonList(mobileNumber));
            String body = "Your new password registered with JCB LiveLink is " + autoGeneratedPassword +
                    " . JCB LiveLink Team.; Dt:";
            smsTemplate.setMsgBody(Collections.singletonList(body));

            //SMS Send
            unicelSmsService.sendSms(smsTemplate);

            return new SmsResponse("Password Generated and Sent to mobile Number", true);

        } catch (PasswordUpdateException e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return new SmsResponse("Failed to update password. Please check the mobile number and try again.", false);

        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return new SmsResponse("An unexpected error occurred. Please try again later.", false);
        }
    }
}