package com.wipro.jcb.livelink.app.mob.auth.controller;

import com.wipro.jcb.livelink.app.mob.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.mob.auth.dto.*;
import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import com.wipro.jcb.livelink.app.mob.auth.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.mob.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import com.wipro.jcb.livelink.app.mob.auth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResetPasswordService resetPasswordService;

    @Autowired
    UserPasswordUpdateService userPasswordUpdateService;

    @Autowired
    LoginServiceMobile loginServiceMobile;

    @GetMapping("/registerUser")
    public String saveUser() {

        User user = new User();
        user.setMachineUpdateNotificationEnabled(true);
        user.setLanguage("English");
        user.setUserAppVersion("4.1.1");
        user.setUserType(UserType.JCB);
        user.setFirstName("Rituraj");
        user.setLastName("Azad");
        user.setEmail("rituraj.azad@wipro.com");
        user.setPhoneNumber("+917903164690");
        user.setImage("abc.png");
        user.setThumbnail("abcd.png");
        user.setCountry("India");
        user.setSmsLanguage("English");
        user.setTimeZone("GMT+5:30");
        user.setSysGenPass(1);
        user.setRoleName("Customer-Fleet-Manager");
        user.setLanguage("English");
        user.setAddress("HYD");

        String username = AuthCommonutils.generateUsername(user.getFirstName());
        System.out.println("username:" + username);
        user.setUserName(username);
        String autoGenPassword = resetPasswordService.generatePassword();
        System.out.println("autoGenPassword:" + autoGenPassword);
        user.setPassword(autoGenPassword);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "User Saved in DB";

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> getToken(@RequestBody LoginRequest loginRequest) {
        log.info("Received login request for user: {}", loginRequest.getUserName());
        try {
            LoginResponse loginResponse = loginServiceMobile.authenticateAndGenerateToken(loginRequest);
            if (loginResponse.getError() == null) {
                log.info("Login successful for user: {}", loginRequest.getUserName());
                return ResponseEntity.ok(loginResponse);
            } else {
                log.error("Login unsuccessful for user: {}. Error: {}", loginRequest.getUserName(), loginResponse.getError());
                return ResponseEntity.badRequest().body(loginResponse);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred during login for user: {}", loginRequest.getUserName(), e);
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setError("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //method to update password after 1st login
    @PostMapping("/updatePassword")
    public ResponseEntity<MsgResponseTemplate> updatePassword(@RequestBody PasswordUpdateRequest request) {
        if (!userPasswordUpdateService.isValidPassword(request.getNewPassword())) {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("Password does not meet the requirements.", false));
        }
        try {
            if (userPasswordUpdateService.isFirstLogin(request.getUsername()) == 1) {
                userPasswordUpdateService.updatePassword(request.getUsername(), request.getNewPassword());
                return ResponseEntity.ok(new MsgResponseTemplate("Password updated successfully for Username : -" + request.getUsername(), true));
            } else {
                return ResponseEntity.badRequest().body(new MsgResponseTemplate("Not allowed to update password, user already changed the password", false));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new MsgResponseTemplate("User not found.", false));
        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponseTemplate("An unexpected error occurred. Please try again later.", false));
        }
    }

    /*API to Refresh Token By Using the Access Token. Token: Random Generated Value
    Token is used to refresh the Access token. Access_Token is generated Using UserName, password and UserType.
    Whole Access_Token will be absolute and new Access_Token will be Generated with Extended Time.
    */
    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userEntity -> {
                    String accessToken = jwtService.generateToken(userEntity.getUserName(), userEntity.getUserType().toString());
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setAccessToken(accessToken);
                    jwtResponse.setToken(refreshTokenRequest.getToken());
                    return jwtResponse;
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

}
