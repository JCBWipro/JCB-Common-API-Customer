package com.wipro.jcb.livelink.app.mob.auth.service;

import com.wipro.jcb.livelink.app.mob.auth.config.UnicelConfig;
import com.wipro.jcb.livelink.app.mob.auth.model.SMSTemplate;
import com.wipro.jcb.livelink.app.mob.auth.model.MsgResponseTemplate;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:02-07-2024
 * project: JCB_NewRepo
 */

@Service
public class UnicelSmsService {

    private static final Logger log = LoggerFactory.getLogger(UnicelSmsService.class);

    @Autowired
    UnicelConfig unicelConfig;

    public List<MsgResponseTemplate> sendSms(SMSTemplate smsTemplate) throws IOException {
        List<MsgResponseTemplate> responses = new ArrayList<>();

        for (int i = 0; i < smsTemplate.getTo().size(); i++) {
            String phoneNumber = smsTemplate.getTo().get(i);
            String message = smsTemplate.getMsgBody().get(i);

            RestTemplate restTemplate = new RestTemplate();
            String finalUrl = getSMSUrl(phoneNumber, message);
            HttpHost targetHost = new HttpHost("instaalerts.zone");
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(finalUrl);
            httpClient.execute(targetHost, httpget);

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("SMS Sent Successfully to {}", phoneNumber);
                    responses.add(new MsgResponseTemplate("SMS sent successfully to " + phoneNumber, true));
                } else {
                    log.error("Failed to Send SMS to {}, Status Code: {}", phoneNumber, response.getStatusCode());

                    responses.add(new MsgResponseTemplate("Failed to send SMS to " + phoneNumber + ". Status code: " + response.getStatusCode(), false));
                }
            } catch (Exception e) {
                log.error("Error sending SMS to {}: {}", phoneNumber, e.getMessage());
                responses.add(new MsgResponseTemplate("Error sending SMS to " + phoneNumber + ": " + e.getMessage(), false));
            }
        }

        return responses;
    }

    private String getSMSUrl(String phoneNumber, String encodedMessage) {
        String beforeUrl = unicelConfig.getAccountUrl() + "&dest=" + phoneNumber + "&msg=" + encodedMessage + "&prty=1";
        beforeUrl = beforeUrl.replaceAll("%", "%25");
        beforeUrl = beforeUrl.replaceAll("\\s", "%20");
        beforeUrl = beforeUrl.replaceAll("#", "%23");
       // beforeUrl = beforeUrl.replaceAll("@", "%40");
        beforeUrl = beforeUrl.replaceAll("!", "%21");
        beforeUrl = beforeUrl.replaceAll(">", "%3E");
        log.info("Unicel URL is : {}", beforeUrl);
        return beforeUrl;
    }
}
