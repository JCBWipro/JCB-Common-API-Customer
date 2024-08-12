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
    AuthenticationManager authenticationManager;

    @Autowired
    ResetPasswordService resetPasswordService;

    @Autowired
    ContactRepo contactRepo;

    @Autowired
    ContactPasswordUpdateService contactPasswordUpdateService;

    @GetMapping("/register")
    public String saveContact() {
    	
    	ContactEntity contactEntity = new ContactEntity();
    	contactEntity.setFirst_name("Gokul");
        contactEntity.setLast_name("Aher");
    	contactEntity.setPrimary_email_id("gokul.aher@wipro.com");
        contactEntity.setPrimary_mobile_number("+919890091680");
        
        
        String username = AuthCommonutils.generateUsername(contactEntity.getFirst_name());
        contactEntity.setContactId(username);
        System.out.println("Username is :" + contactEntity.getContactId());
        String autoGenPassword = resetPasswordService.generatePassword();
        contactEntity.setPassword(autoGenPassword);
        System.out.println("Password is :" + contactEntity.getPassword());
        contactEntity.setPassword(new BCryptPasswordEncoder().encode(contactEntity.getPassword()));
        
        RoleEntity roleEntity = new RoleEntity();
        if(username.startsWith("g") || username.startsWith("G")) {
        	roleEntity.setRole_id(1);
        	roleEntity.setRole_name("JCB Account");
        } else {
        	roleEntity.setRole_id(8);
        	roleEntity.setRole_name("Customer");
        }
        
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
    public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        try {

            String username = authRequest.getUsername();

            // Check login attempts
            int attempts = contactRepo.userLoginGetAttempts(username);
            if (attempts >= 5) {
                jwtResponse.setError("Maximum login attempts reached. Account locked.");
                return jwtResponse;
            }
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            String role = authenticate.getAuthorities().iterator().next().toString();
            String roleName = AuthCommonutils.getRolesByID(role);
            if (authenticate.isAuthenticated() && roleName.equals(authRequest.getRole())) {

                // Reset login attempts on successful login
                contactRepo.userLoginResetAttempts(username);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
                jwtResponse.setJwtToken(jwtService.generateToken(authRequest.getUsername(), roleName));
                jwtResponse.setTokenId(refreshToken.getToken());
                return jwtResponse;
            } else {

                // Increment login attempts on failed login
                contactRepo.userLoginIncrementAttempts(username);
                jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
            }
        } catch (Exception e) {

            contactRepo.userLoginIncrementAttempts(authRequest.getUsername());
            jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
            System.out.println(e.getMessage());
        }
        return jwtResponse;
    }

    //method to update password after 1st login
    @PostMapping("/updatePassword")
    public ResponseEntity<MsgResponseTemplate> updatePassword(@RequestBody PasswordUpdateRequest request) {
        if (!contactPasswordUpdateService.isValidPassword(request.getNewPassword())) {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("Password does not meet the requirements.", false));
        }
        try {
            if (contactPasswordUpdateService.isFirstLogin(request.getUsername())==1) {
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

}
