/*
package com.wipro.jcb.livelink.app.service.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

*/
/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:02-07-2024
 * project: JCB_NewRepo
 *//*

@Service
public class UnicelSmsService {
    private static final Logger log = LoggerFactory.getLogger(UnicelSmsService.class);
    @Autowired
    private UnicelConfig unicelConfig;

    public String sendSms(String phoneNumber, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String encodedMessage = encode(message);
        String beforeUrl = unicelConfig.getAccountUrl() + "&dest=" + phoneNumber + "&msg=" + encodedMessage + "&prty=1&vp=30";
        beforeUrl = beforeUrl.replaceAll("%", "%25");
        beforeUrl = beforeUrl.replaceAll("\\s", "%20");
        beforeUrl = beforeUrl.replaceAll("#", "%23");
        beforeUrl = beforeUrl.replaceAll("@", "%40");
        beforeUrl = beforeUrl.replaceAll("!", "%21");
        beforeUrl = beforeUrl.replaceAll(">", "%3E");
        String finalUrl = beforeUrl;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);
            if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                log.info("SMS Sent Successfully");
                return "SMS sent successfully"; // Success response
            } else {
                log.error("Failed to Send SMS, Status Code: {}", response.getStatusCode());
                return "Failed to send SMS. Status code: " + response.getStatusCode(); // Error response
            }
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
            return "Error sending SMS: " + e.getMessage(); // Error response with details
        }
    }
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Handle encoding exceptions appropriately (e.g., log and return original value)
            return value;
        }
    }
}
*/
