package com.wipro.jcb.livelink.app.user.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wipro.jcb.livelink.app.user.businessObject.TenancyBO;
import com.wipro.jcb.livelink.app.user.businessObject.UserDetailsBO;
import com.wipro.jcb.livelink.app.user.commonUtils.UserCommonUtils;
import com.wipro.jcb.livelink.app.user.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.dto.UserDetails;
import com.wipro.jcb.livelink.app.user.entity.AccountContactMapping;
import com.wipro.jcb.livelink.app.user.repo.AccountContactMappingRepo;
import com.wipro.jcb.livelink.app.user.repo.AccountRepo;
import com.wipro.jcb.livelink.app.user.repo.ContactRepo;
import com.wipro.jcb.livelink.app.user.service.TenancyService;
import com.wipro.jcb.livelink.app.user.service.UserDetailsBOService;
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
    UserDetailsBOService userDetailsBOService;

    @Autowired
    TenancyService tenancyService;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountContactMappingRepo accountContactMappingRepo;

    @Autowired
    UserRoleCheckService userRoleCheckService;

    @Autowired
    ContactRepo contactRepo;

    /*
		This End Point is used for the testing Purpose
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
        This End Point is used to fetch Tenancy Details
    */
    @GetMapping("/tenancyDetails/{userName}")
    public UserAuthenticationRespContract getTenancyDetails(@PathVariable String userName) {

        //Fetch userDetails like Contact and Role based on userName
        UserDetailsBO userDetails = userDetailsBOService.getUserDetails(userName);

        //Fetch Tenancy Details based on userName and accountId
        TenancyBO tenancy = tenancyService.getUserTenancy(userName, userDetails.getAccount_id());
        UserAuthenticationRespContract userAuthenticationResponse = new UserAuthenticationRespContract();

        if (userDetails.getContact().getLast_name() == null) {
            userAuthenticationResponse.setUser_name(userDetails.getContact().getFirst_name());
        } else {
            userAuthenticationResponse.setUser_name(userDetails.getContact().getFirst_name() + " " + userDetails.getContact().getLast_name());
        }

        //Fetch Account and Contact ID's based on userName
        AccountContactMapping reporesult = accountContactMappingRepo.findAccountAndContactID(userName);

        //Fetch countryName based on countryCode from country_codes table
        String countryName = accountRepo.findCountryNameByCode(reporesult.getContact().getCountrycode());

        //Starting Setting up Details into userAuthenticationResponse
        userAuthenticationResponse.setLoginId(userName);
        userAuthenticationResponse.setUser_name(userAuthenticationResponse.getUser_name() + "|" + countryName);
        String llAccountCode = tenancyService.getLLAccountCode(reporesult.getAccount().getAccountCode());

        if (llAccountCode == null && userDetails.getLast_login_date() != null) {
            userAuthenticationResponse.setLast_login_date(userDetails.getLast_login_date() + "|NA");
        } else if (llAccountCode != null && userDetails.getLast_login_date() == null) {
            userAuthenticationResponse.setLast_login_date("|" + llAccountCode);
        } else if (llAccountCode != null && userDetails.getLast_login_date() != null) {
            userAuthenticationResponse.setLast_login_date(userDetails.getLast_login_date() + "|" + llAccountCode);
        } else {
            userAuthenticationResponse.setLast_login_date("|NA");
        }

        userAuthenticationResponse.setRole_name(userDetails.getContact().getRole().getRole_name());
        userAuthenticationResponse.setRoleId(userDetails.getContact().getRole().getRole_id());
        userAuthenticationResponse.setIsTenancyAdmin(userDetails.getContact().getIs_tenancy_admin());
        userAuthenticationResponse.setSysGeneratedPassword(userDetails.getContact().getSysGeneratedPassword());
        //Ending of Setting up Details into userAuthenticationResponse

        //Fetch TenancyList based on userAuthenticationResponse
        userAuthenticationResponse = tenancyService.getTenancyObj(tenancy, userAuthenticationResponse);
        return userAuthenticationResponse;
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