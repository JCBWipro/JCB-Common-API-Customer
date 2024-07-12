package com.wipro.jcb.livelink.app.controller;

import com.wipro.jcb.livelink.app.entity.RegistrationResponse;
import com.wipro.jcb.livelink.app.exception.PasswordUpdateException;
import com.wipro.jcb.livelink.app.model.ContactResponse;
import com.wipro.jcb.livelink.app.model.Message;
import com.wipro.jcb.livelink.app.model.UserRegistrationRequest;
import com.wipro.jcb.livelink.app.repository.UserRepository;
import com.wipro.jcb.livelink.app.service.UserService;
import com.wipro.jcb.livelink.app.service.email.EmailService;
import com.wipro.jcb.livelink.app.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:09-07-2024
 * project: msKafka
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private UserRepository userRepository;

    //API to send SMS and Email to the user
   /* @GetMapping("/send/{id}")
    public ResponseEntity<String> sendMessage(@PathVariable int id) {
        ParameterizedTypeReference<List<ContactResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ContactResponse>> responseEntity = webClientBuilder.build().get().uri(contactUrl + id).retrieve().toEntity(responseType).block();
        assert responseEntity != null;
        List<ContactResponse> contactResponses = responseEntity.getBody();
        Message msg = new Message();
        //SMS Call
        List<String> mobList = new ArrayList<>();
        assert contactResponses != null;
        for (ContactResponse contactResp : contactResponses) {
            mobList.add(contactResp.getMobileNumber());
        }
        msg.setMobileNumber(mobList);
        msg.setContent("Hi,This is System generated SMS from JCB LiveLink Application ");
        msg.setTimeStamp(LocalDateTime.now());
        //smsService.sendSms(msg);
        //Email Call
        List<String> emailList = new ArrayList<>();
        for (ContactResponse contactResp : contactResponses) {
            emailList.add(contactResp.getEmail());
        }
        log.info("Message sent Successfully to : '{}'", msg);
        //emailService.sendEmail(emailList, "Testing Email from APIGateway", "This is System generated Email from JCB LiveLink Application", new Date());
        log.info("Email sent Successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Message sent Successfully to " + mobList);
    }*/
    
    public String getResponse() {
    	return "Message Controller Service";
    }
    
    //API to register user details after mobile verification
    @PostMapping("/users/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody UserRegistrationRequest request) {

        RegistrationResponse response = userService.registerUserDetails(request);
        assert response != null;
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    //API to register user with mobile Number
    @GetMapping("/registrationWithMobileNumber/{mobileNumber}")
    public ResponseEntity<RegistrationResponse> registerUserWithMobileNumber(@PathVariable String mobileNumber) {
        RegistrationResponse response = userService.registerUserByMobileNumber(mobileNumber);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //API to verify the OTP.
    @PostMapping("/otp/verify")
    public boolean otpVerification(@RequestBody String otpRequest) {
        return userService.isOtpValid(otpRequest);
    }

    //API to regenerate the OTP.
    @PostMapping("/otp/regenerate")
    public void setNewOtp(@RequestBody String email) {
        String mobile = userRepository.findMobileNumber(email);
        userService.reGenerateOTP(mobile);
    }

    //API to reset the password and send to the user.
    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestBody String mobileNumber) {
        try {
            String autoGeneratedPassword = userService.generatePassword();
            log.info("password is before sending to DB:{}", autoGeneratedPassword);
            userService.updateResetPassword(autoGeneratedPassword, mobileNumber);
            //smsService.sendPassword(autoGeneratedPassword, mobileNumber);
            return "Password Generated and Sent to mobile Number";
        } catch (PasswordUpdateException e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return "Failed to update password. Please check the mobile number and try again.";
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return "An unexpected error occurred. Please try again later.";
        }
    }
}
