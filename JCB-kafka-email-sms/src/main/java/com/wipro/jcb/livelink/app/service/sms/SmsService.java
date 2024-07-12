package com.wipro.jcb.livelink.app.service.sms;

import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import com.wipro.jcb.livelink.app.config.TwilioConfig;
import com.wipro.jcb.livelink.app.model.Message;
import com.wipro.jcb.livelink.app.model.OTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-05-2024
 * project: msKafka
 */

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);
    private final TwilioConfig twilioConfig;

    @Autowired
    public SmsService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    public void sendSms(Message message) {
        try {
            List<String> mobileNumber = message.getMobileNumber();
            for (String mobNumber : mobileNumber) {
                PhoneNumber to = new PhoneNumber(mobNumber);
                PhoneNumber from = new PhoneNumber(twilioConfig.getTwilioPhoneNumber());
                MessageCreator creator = com.twilio.rest.api.v2010.account.Message.creator(to, from, message.getContent());
                creator.create();
            }
            log.info("SMS sent to {} with content: '{}'", message.getMobileNumber(), message.getContent());
        } catch (Exception e) {
            log.error("Failed to send SMS : {}", e.getMessage());
        }

    }

    public void sendOTP(OTP otp) {

        PhoneNumber to = new PhoneNumber(otp.getMobileNumber());
        PhoneNumber from = new PhoneNumber(twilioConfig.getTwilioPhoneNumber());
        MessageCreator creator = com.twilio.rest.api.v2010.account.Message.creator(to, from, otp.getOtp());
        creator.create();
        log.info("OTP sent to mobile Number: {} with OTP as: {}", otp.getMobileNumber(), otp.getOtp());
    }

    public void sendPassword(String plainPassword, String mobileNumber) {
        if (mobileNumber == null) {
            log.error("Cannot send password: mobileNumber is null");
            throw new IllegalArgumentException("Mobile number cannot be null.");
        } else if (plainPassword == null) {
            log.error("Cannot send password: plainPassword is null");
            throw new NullPointerException("Plain password cannot be null.");
        } else {
            PhoneNumber to = new PhoneNumber(mobileNumber);
            PhoneNumber from = new PhoneNumber(twilioConfig.getTwilioPhoneNumber());
            MessageCreator creator = com.twilio.rest.api.v2010.account.Message.creator(to, from, plainPassword);
            creator.create();
            log.info("Password sent to the User :{} with password as :{}", mobileNumber, plainPassword);

        }
    }
}
