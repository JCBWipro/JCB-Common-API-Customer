package com.wipro.jcb.livelink.app.auth.service.impl;

import com.wipro.jcb.livelink.app.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.auth.model.SmsResponse;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.ForgotUsernameService;
import com.wipro.jcb.livelink.app.auth.service.UnicelSmsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ForgotUsernameServiceImpl extends ForgotUsernameService {


    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ForgotUsernameServiceImpl.class);
    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UnicelSmsService unicelSmsService;

    public SmsResponse forgotUsername(String mobileNumber) {
        try {
            if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
                return new SmsResponse("Mobile number is required.", false);
            }

            String userName = contactRepo.findByMobileNumber(mobileNumber);
            if (userName == null) {
                return new SmsResponse("User not found.", false);
            }

            SMSTemplate smsTemplate = new SMSTemplate();
            smsTemplate.setTo(Collections.singletonList(mobileNumber));
            String body = "Your login id registered with JCB LiveLink is " + userName +
                    " . JCB LiveLink Team.; Dt: -JCB LiveLink";
            smsTemplate.setMsgBody(Collections.singletonList(body));
            //Send SMS
            unicelSmsService.sendSms(smsTemplate);

            return new SmsResponse("Username Sent to mobile Number : "+mobileNumber, true);

        } catch (Exception e) {
            log.error("Error retrieving username: {}", e.getMessage(), e);
            return new SmsResponse("An unexpected error occurred. Please try again later.", false);
        }
    }
}
