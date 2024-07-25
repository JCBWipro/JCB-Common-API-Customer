package com.wipro.jcb.livelink.app.auth.controller;

import com.wipro.jcb.livelink.app.auth.model.SmsResponse;
import com.wipro.jcb.livelink.app.auth.service.impl.ForgotUsernameServiceImpl;
import com.wipro.jcb.livelink.app.auth.service.impl.ResetPasswordServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/web")
public class ResetPasswordController {

    private static final Logger log = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private ResetPasswordServiceImpl resetPasswordService;

    @Autowired
    private ForgotUsernameServiceImpl forgotUsername;

    //API to reset the password and send to the user.
    @PostMapping("/resetPassword")
    public ResponseEntity<SmsResponse> processResetPassword(@RequestBody String userName) {
        SmsResponse response = resetPasswordService.resetPassword(userName);
        if (response.isSuccess()) {
            log.info("Status is : {}", response);
            return ResponseEntity.ok(response);
        } else {
            // Determine appropriate HTTP status based on the error message in the response
            HttpStatus status = response.getMessage().contains("Invalid username") ?
                    HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Status is : {} ", status);
            return ResponseEntity.status(status).body(response);
        }
    }

    @PostMapping("/forgotUsername")
    public ResponseEntity<SmsResponse> processForgotUsername(@RequestBody String mobileNumber) {
        SmsResponse response = forgotUsername.forgotUsername(mobileNumber);
        if (response.isSuccess()) {
            log.info("Status is : {}", response);
            return ResponseEntity.ok(response);
        } else {
            // Determine appropriate HTTP status based on the error message in the response
            HttpStatus status = response.getMessage().contains("Invalid mobile number") ?
                    HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Status is :{} ", status);
            return ResponseEntity.status(status).body(response);
        }
    }

}
