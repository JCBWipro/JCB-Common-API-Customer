package com.wipro.jcb.livelink.app.mob.auth.controller;

import com.wipro.jcb.livelink.app.mob.auth.dto.ForgotUsernameRequest;
import com.wipro.jcb.livelink.app.mob.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.mob.auth.service.impl.ForgotUsernameServiceImpl;
import com.wipro.jcb.livelink.app.mob.auth.service.impl.ResetPasswordServiceImpl;
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
@RequestMapping("/auth")
public class ResetUsernameAndPasswordController {

    private static final Logger log = LoggerFactory.getLogger(ResetUsernameAndPasswordController.class);
    @Autowired
    ResetPasswordServiceImpl resetPasswordService;

    @Autowired
    ForgotUsernameServiceImpl forgotUsername;

    //API to reset the password and send to the user.
    @PostMapping("/resetPassword")
    public ResponseEntity<MsgResponseTemplate> processResetPassword(@RequestBody String userName) {
        MsgResponseTemplate response = resetPasswordService.resetPassword(userName);
        if (response.isSuccess()) {
            log.info("Status is :- {}", response);
            return ResponseEntity.ok(response);
        } else {
            // Determine appropriate HTTP status based on the error message in the response
            HttpStatus status = response.getMessage().contains("Invalid username") ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Status is :- {}", status);
            return ResponseEntity.status(status).body(response);
        }
    }

    //API to send username to the User mobile Number
    @PostMapping("/forgotUsername")
    public ResponseEntity<MsgResponseTemplate> processForgotUsername(@RequestBody ForgotUsernameRequest request) {
        MsgResponseTemplate msgResponseTemplate;
        if (request.getMobileNumber() != null) {
            msgResponseTemplate = forgotUsername.forgotUsername(request.getMobileNumber(), null);
        } else if (request.getEmailId() != null) {
            msgResponseTemplate = forgotUsername.forgotUsername(null, request.getEmailId());
        } else {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("Either mobile number or email ID is required.", false));
        }

        if (msgResponseTemplate.isSuccess()) {
            log.info("Status is : {}", msgResponseTemplate);
            return ResponseEntity.ok(msgResponseTemplate);
        } else {
            HttpStatus status = msgResponseTemplate.getMessage().contains("Invalid") ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Status is : {}", status);
            return ResponseEntity.status(status).body(msgResponseTemplate);
        }
    }

}
