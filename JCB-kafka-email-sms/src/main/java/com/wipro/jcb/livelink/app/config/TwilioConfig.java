package com.wipro.jcb.livelink.app.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-05-2024
 * project: msKafka
 */
@Data
@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init(){
        Twilio.init(accountSid,authToken);
    }

}
