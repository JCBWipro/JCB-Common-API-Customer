package com.wipro.jcb.livelink.app.auth.controller;

import com.wipro.jcb.livelink.app.auth.dto.ForgotUsernameRequest;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.auth.service.PasswordDecryptionService;
import com.wipro.jcb.livelink.app.auth.service.PasswordUpdateEncrypted;
import com.wipro.jcb.livelink.app.auth.service.impl.ForgotUsernameServiceImpl;
import com.wipro.jcb.livelink.app.auth.service.impl.ResetPasswordServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/auth/web")
public class ResetUsernameAndPasswordController {

    private static final Logger log = LoggerFactory.getLogger(ResetUsernameAndPasswordController.class);

    @Autowired
    ResetPasswordServiceImpl resetPasswordService;

    @Autowired
    ForgotUsernameServiceImpl forgotUsername;

    @Autowired
    PasswordUpdateEncrypted passwordUpdateEncrypted;

    @Autowired
    PasswordDecryptionService passwordDecryptionService;

    //API to reset the password and send to the user.
    @PostMapping("/resetPassword")
    public ResponseEntity<MsgResponseTemplate> processResetPassword(@RequestBody String userName) {
        MsgResponseTemplate msgResponseTemplate = resetPasswordService.resetPassword(userName);
        if (msgResponseTemplate.isSuccess()) {
            log.info("Status is :- {}", msgResponseTemplate);
            return ResponseEntity.ok(msgResponseTemplate);
        } else {
            // Determine appropriate HTTP status based on the error message in the response
            HttpStatus status = msgResponseTemplate.getMessage().contains("Invalid username") ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Status is :- {} ", status);
            return ResponseEntity.status(status).body(msgResponseTemplate);
        }
    }

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

    @PostMapping("/updatePasswordsFromExcel")
    public ResponseEntity<MsgResponseTemplate> updatePasswordsFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("usernameColumn") int usernameColumn,
            @RequestParam("passwordColumn") int passwordColumn
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new MsgResponseTemplate("Please select an Excel file to upload.", false));
            }

            // Processing  the Excel file
            passwordUpdateEncrypted.updatePasswordsFromExcel(file.getInputStream().toString(), usernameColumn, passwordColumn);

            return ResponseEntity.ok(new MsgResponseTemplate("Passwords updated successfully from Excel.", true));
        } catch (IOException e) {
            log.error("Error updating passwords from Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponseTemplate("An error occurred while processing the Excel file.", false));
        }
    }

    @GetMapping("/decryptAndExportPasswords")
    public ResponseEntity<ByteArrayResource> decryptAndExportPasswords(HttpServletResponse response) {
        try {
            ByteArrayInputStream excelData = passwordDecryptionService.decryptAndExportPasswords();
            ByteArrayResource resource = new ByteArrayResource(excelData.readAllBytes());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=UsernameDecryptedPassword.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(resource);

        } catch (IOException e) {
            log.error("Error decrypting and exporting passwords: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
