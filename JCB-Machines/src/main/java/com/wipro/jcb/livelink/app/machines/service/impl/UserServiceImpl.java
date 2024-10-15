package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.config.UsermappingThread;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.constants.UserType;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.service.UserService;
import com.wipro.jcb.livelink.app.machines.service.response.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Value("${livelinkserver.resttemplateurl}")
    private String restTemplateUrl;
    @Value("${server.evn.baseurl}")
    private String env;
    @Autowired
    UserMappingRequest userMappingRequest;


    @Override
    public UserProfile getUserProfile(String userName, String version) throws ProcessCustomError {

        User user;
        if (userName != null) {
            try {
                user = userRepository.findByUserName(userName);
                log.info("the  value of user s :{}", userName);
                try {
                    user = userRepository.findByUserName(userName);
                    log.info("value of user:{}", user);
                } catch (Exception e) {
                    log.info("value of user in catch:{}", user);
                    e.getMessage();
                    e.printStackTrace();

                }
                if (user != null) {
                    UsermappingThread thread = new UsermappingThread(restTemplateUrl, env, userName);
                    Thread runner = new Thread(thread);
                    runner.start();
                    log.info("After Thread Calling ");
                } else {
                    log.info("Calling RestTemplate Methods For UserMapping :{}", userName);
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

                }

                //return result;

            } catch (RestClientException exception) {
                log.error("Error while calling machine mapping API: {}", exception.getMessage());
                log.error("Exception occurred while calling user mapping API: ", exception);
                return new ResponseEntity<UserProfile>(HttpStatus.INTERNAL_SERVER_ERROR).getBody();
            }

            try {
                if (!"optional".equals(version)) {
                    log.info("Version Updation {}-{}", userName, version);
                    userRepository.versionUpdate(version, userName);
                }
            } catch (Exception e) {
                log.error("Exception occured at userprofile : {}-{}-{}", userName, version, e.getMessage());
                e.printStackTrace();
            }
            user = userRepository.findByUserName(userName);
            return new UserProfile(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(),
                    user.getImage(), user.getThumbnail(), user.getCountry(), user.getSmsLanguage(), user.getTimeZone(),
                    user.getRoleName());
        } else {
            log.error("getUserProfile: User session expired ");
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public Boolean getuserMapping(String userName) {
        boolean status = false;
        try {
            log.info("Calling UserMapping Method");
            User user = userRepository.findByUserName(userName);
            log.info("UserMapping API Details - {} - {}", user.getUserType(), user.getUserName());
            if (Objects.equals(user.getRoleName(), "Customer Care") || user.getUserType() == UserType.JCB) {
                log.info("Started machine mapping for Admin :{}", userName);
                userMappingRequest.adduserMapping(userName);
                log.info("Completed machine mapping fro Admin :{}", userName);
            } else {
                try {
                    userMappingRequest.userMapping(userName);
                } catch (final ProcessCustomError exception) {
                    log.error("Error while processing mapping request: {}", exception.getMessage());
                    throw new ProcessCustomError(exception.getMessage(), exception.getMessage(),
                            HttpStatus.UNAUTHORIZED);
                }
            }

            status = true;
        } catch (Exception e) {
            log.error("Exception in usermapping :{}-{}", userName, e.getMessage());
        }
        return status;
    }

}
