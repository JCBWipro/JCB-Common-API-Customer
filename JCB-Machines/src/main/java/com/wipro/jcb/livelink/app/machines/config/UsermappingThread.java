package com.wipro.jcb.livelink.app.machines.config;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@PropertySource("application.properties")
public class UsermappingThread implements Runnable {
    @Autowired
    Utilities utilities;

    private String restTemplateUrl;

    private String env;

    private String userName;

    public UsermappingThread(String restTemplateUrl, String env, String userName) {
        super();
        this.restTemplateUrl = restTemplateUrl;
        this.env = env;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            final RestTemplate restTemplate = new RestTemplate();
            String url = restTemplateUrl + "/user/machines/userMapping";
            // Create headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    UriComponentsBuilder
                            .fromHttpUrl(url)
                            .queryParam("userName", userName)
                            .toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("get user profile successful.");
            } else {
                log.error("get user profile : Error calling  API. Status code: {}, Response body: {}",
                        response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception in Thread {}", e.getMessage());

        }
    }

}
