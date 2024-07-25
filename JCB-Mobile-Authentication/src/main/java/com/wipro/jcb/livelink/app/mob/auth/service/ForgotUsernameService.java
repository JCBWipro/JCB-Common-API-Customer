package com.wipro.jcb.livelink.app.mob.auth.service;

import com.wipro.jcb.livelink.app.mob.auth.model.SmsResponse;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ForgotUsernameService {

    private static final Logger log = LoggerFactory.getLogger(ForgotUsernameService.class);
    @Autowired
    private UserRepository userRepository;

    public SmsResponse forgotUsername(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
            return new SmsResponse("Mobile number is required", false);
        }
        try {
            String username = userRepository.findByMobileNumber(mobileNumber);
            if (username != null) {
                return new SmsResponse(username, true);
            } else {
                return new SmsResponse("No user found with this mobile number", false);
            }
        } catch (DataAccessException e) {
            log.error("Error retrieving username for mobile number: {}", mobileNumber, e);
            return new SmsResponse("Error retrieving username", false);
        }
    }
}
