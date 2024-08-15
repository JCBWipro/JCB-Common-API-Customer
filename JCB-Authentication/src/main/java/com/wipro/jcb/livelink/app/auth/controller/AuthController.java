package com.wipro.jcb.livelink.app.auth.controller;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.dto.AuthRequest;
import com.wipro.jcb.livelink.app.auth.dto.JwtResponse;
import com.wipro.jcb.livelink.app.auth.dto.PasswordUpdateRequest;
import com.wipro.jcb.livelink.app.auth.dto.RefreshTokenRequest;
import com.wipro.jcb.livelink.app.auth.entity.*;
import com.wipro.jcb.livelink.app.auth.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth/web")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    LoginService loginService;

    @Autowired
    ResetPasswordService resetPasswordService;

    @Autowired
    ContactRepo contactRepo;

    @Autowired
    ContactPasswordUpdateService contactPasswordUpdateService;

    @Autowired
    AccountUnlockService accountUnlockService;

    @GetMapping("/register")
    public String saveContact() {
    	
    	ContactEntity contactEntity = new ContactEntity();
    	contactEntity.setFirst_name("Gokul");
        contactEntity.setLast_name("Aher");
    	contactEntity.setPrimary_email_id("gokul.aher@wipro.com");
        contactEntity.setPrimary_mobile_number("+919890091680");
        
        
        String username = AuthCommonutils.generateUsername(contactEntity.getFirst_name());
        contactEntity.setContactId(username);
        log.info("Username is : {} ", contactEntity.getContactId());
        String autoGenPassword = resetPasswordService.generatePassword();
        contactEntity.setPassword(autoGenPassword);
        log.info("Password is : {} ", contactEntity.getPassword());
        contactEntity.setPassword(new BCryptPasswordEncoder().encode(contactEntity.getPassword()));
        
        RoleEntity roleEntity = new RoleEntity();
        if(username.startsWith("g") || username.startsWith("G")) {
        	roleEntity.setRole_id(1);
        	roleEntity.setRole_name("JCB Account");
        } else {
        	roleEntity.setRole_id(8);
        	roleEntity.setRole_name("Customer");
        }
        log.info("Role Name is : {} ", roleEntity.getRole_name());
        
        IndustryEntity industryEntity = new IndustryEntity();
        industryEntity.setIndustry_id(1);
        industryEntity.setIndustry_name("DIndustry");

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClient_id(1);
        clientEntity.setClient_name("DClientname");
        clientEntity.setIndustry_id(industryEntity);

        contactEntity.setClient_id(clientEntity);
        contactEntity.setRole(roleEntity);
        contactRepo.save(contactEntity);
        
        String response = "Contact Saved. ContactID:-"+contactEntity.getContactId()+", Password:-"+autoGenPassword+
        		", Role:-"+roleEntity.getRole_name();
        return response;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> getToken(@RequestBody AuthRequest authRequest) {
        log.info("Received login request for user: {}", authRequest.getUsername());
        try {
            JwtResponse jwtResponse = loginService.authenticateAndGenerateToken(authRequest);
            if (jwtResponse.getError() == null) {
                log.info("Login successful for user: {}", authRequest.getUsername());
            } else {
                log.error("Login unsuccessful for user: {}. Error: {}", authRequest.getUsername(), jwtResponse.getError());
            }
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            log.error("An unexpected error occurred during login for user: {}", authRequest.getUsername(), e);
            JwtResponse errorResponse = new JwtResponse();
            errorResponse.setError("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //method to update password after 1st login
    @PostMapping("/updatePassword")
    public ResponseEntity<MsgResponseTemplate> updatePassword(@RequestBody PasswordUpdateRequest request) {
        if (!contactPasswordUpdateService.isValidPassword(request.getNewPassword())) {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("Password does not meet the requirements.", false));
        }
        try {
            if (contactPasswordUpdateService.isFirstLogin(request.getUsername()) == 1) {
                contactPasswordUpdateService.updatePassword(request.getUsername(), request.getNewPassword());
                return ResponseEntity.ok(new MsgResponseTemplate("Password updated successfully.", true));
            } else {
                return ResponseEntity.badRequest().body(new MsgResponseTemplate("Not allowed to update password.", false));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("User not found.", false));
        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponseTemplate("An unexpected error occurred. Please try again later.", false));
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getContactEntity)
                .map(contactEntity -> {
                    String roleName = AuthCommonutils.getRolesByID(String.valueOf(contactEntity.getRole().getRole_id()));
                    String jwtToken = jwtService.generateToken(contactEntity.getContactId(), roleName);
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setJwtToken(jwtToken);
                    jwtResponse.setTokenId(refreshTokenRequest.getToken());
                    return jwtResponse;
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/unlockAccountsManually")
    public ResponseEntity<String> unlockAccountsManually() {
        log.info("Received request to manually unlock accounts.");
        try {
            List<ContactEntity> lockedUsers = contactRepo.findLockedUsers(); // Get locked users

            if (lockedUsers.isEmpty()) {
                log.info("No locked accounts found.");
                return ResponseEntity.ok("No locked accounts found.");
            }

            accountUnlockService.unlockAccounts();
            log.info("Successfully unlocked accounts manually.");
            return ResponseEntity.ok("Accounts unlocked manually.");

        } catch (Exception e) {
            log.error("An error occurred while manually unlocking accounts.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to unlock accounts manually.");
        }
    }

}
