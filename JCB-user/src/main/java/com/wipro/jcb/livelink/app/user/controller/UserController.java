package com.wipro.jcb.livelink.app.user.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wipro.jcb.livelink.app.user.commonUtils.UserCommonUtils;
import com.wipro.jcb.livelink.app.user.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.dto.UserDetails;
import com.wipro.jcb.livelink.app.user.repo.ContactRepo;
import com.wipro.jcb.livelink.app.user.service.UserAuthenticationResponseService;
import com.wipro.jcb.livelink.app.user.service.UserRoleCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserAuthenticationResponseService userAuthenticationResponseService;

	@Autowired
	ContactRepo contactRepo;

	@Autowired
	UserRoleCheckService userRoleCheckService;

    /*
     * This End Point is used for the testing Purpose
     */
    @GetMapping
    public String getString(@RequestHeader("LoggedInUserRole") String userDetails) {
        Gson gson = new Gson();
        UserDetails userResponse = gson.fromJson(userDetails, UserDetails.class);
        int roleId = Integer.parseInt(userResponse.getRoleName());
        String roleName = UserCommonUtils.getRolesByID(roleId);
        return "LoggedIn Role is:-" + roleName + " and UserName is:-" + userResponse.getUserName();
    }

    /*
     * This End Point is used to fetch Tenancy Details
     */
    @GetMapping("/tenancyDetails/{userName}")
    public UserAuthenticationRespContract getTenancyDetails(@PathVariable String userName) {
        return userAuthenticationResponseService.getTenancyDetails(userName);

    }

    @GetMapping("/unlockAccountsManually")
    public ResponseEntity<Map<String, Object>> unlockAccountsManually(@RequestHeader("LoggedInUserRole") String userDetails) {
        log.info("Received request to manually unlock accounts.");
        Map<String, Object> response = new HashMap<>();

        Gson gson = new Gson();
        UserDetails userResponse = gson.fromJson(userDetails, UserDetails.class);
        try {
            if (userResponse.getRoleName().equals("Super Admin")) {
                ResponseEntity<String> unlockResult = userRoleCheckService.unlockAccounts();

                // Check the status code from the unlockAccounts method
                if (unlockResult.getStatusCode().is2xxSuccessful()) {
                    response.put("message", unlockResult.getBody());
                    response.put("success", true);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("message", "Failed to unlock accounts manually: " + unlockResult.getBody());
                    response.put("success", false);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            } else {
                log.warn("User is not authorized to unlock accounts..");
                response.put("message", "LoggedIn User is not authorized to unlock accounts.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            log.error("An error occurred while manually unlocking accounts.", e);
            response.put("message", "Failed to unlock accounts manually.");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/unlockAccountByUserID/{contactID}")
    public ResponseEntity<Map<String, Object>> unlockAccountManuallyByUserID(
            @RequestHeader("LoggedInUserRole") String userDetails,
            @PathVariable String contactID) {

        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        UserDetails userResponse = gson.fromJson(userDetails, UserDetails.class);

        try {
            if (userResponse.getRoleName().equals("Super Admin")) {
                String lockedUser = contactRepo.findLockedUserByID(contactID);

                if (lockedUser != null) {
                    userRoleCheckService.unlockAccountByUserID(contactID);
                    log.info("Account unlocked successfully for user: {}", contactID);

                    response.put("message", "Account unlocked successfully for user: " + contactID);
                    response.put("success", true);
                    return ResponseEntity.ok(response);

                } else {
                    log.info("No locked user found with ID: {}", contactID);

                    response.put("message", "No locked user found with ID: " + contactID);
                    response.put("success", false);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

            } else {
                log.warn("User is not authorized to unlock accounts.");

                response.put("message", "LoggedIn User is not authorized to unlock accounts.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (JsonSyntaxException e) {
            log.error("Invalid JSON format in LoggedInUserRole header: {}", userDetails, e);

            response.put("message", "Invalid user detail in header.");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}