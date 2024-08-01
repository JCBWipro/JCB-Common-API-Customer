package com.wipro.jcb.livelink.app.mob.auth.controller;

import com.wipro.jcb.livelink.app.mob.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.mob.auth.dto.*;
import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import com.wipro.jcb.livelink.app.mob.auth.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.mob.auth.model.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import com.wipro.jcb.livelink.app.mob.auth.service.JwtService;
import com.wipro.jcb.livelink.app.mob.auth.service.RefreshTokenService;
import com.wipro.jcb.livelink.app.mob.auth.service.ResetPasswordService;
import com.wipro.jcb.livelink.app.mob.auth.service.UserPasswordUpdateService;
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
@RequestMapping("/auth/mob")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResetPasswordService resetPasswordService;

    @Autowired
    UserPasswordUpdateService userPasswordUpdateService;

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
    public LoginResponse getToken(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            String username = loginRequest.getUserName();

            // Check login attempts
            int attempts = userRepository.userLoginGetAttempts(username);
            if (attempts >= 5) {
                loginResponse.setError("Maximum login attempts reached. Account locked.");
                return loginResponse;
            }

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
            String userType = authenticate.getAuthorities().iterator().next().toString();
            if (authenticate.isAuthenticated() && userType.equals(loginRequest.getUserType())) {

                // Reset login attempts on successful login
                userRepository.userLoginResetAttempts(username);

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUserName());
                loginResponse.setAccessToken(jwtService.generateToken(loginRequest.getUserName(), userType));
                loginResponse.setToken(refreshToken.getToken());
                loginResponse.setRoleName(refreshToken.getUser().getRoleName());
                loginResponse.setFirstName(refreshToken.getUser().getFirstName());
                loginResponse.setLastName(refreshToken.getUser().getLastName());
                loginResponse.setEmail(refreshToken.getUser().getEmail());
                loginResponse.setNumber(refreshToken.getUser().getPhoneNumber());
                loginResponse.setImage(refreshToken.getUser().getImage());
                loginResponse.setThumbnail(refreshToken.getUser().getThumbnail());
                loginResponse.setCountry(refreshToken.getUser().getCountry());
                loginResponse.setSmsLanguage(refreshToken.getUser().getSmsLanguage());
                loginResponse.setTimeZone(refreshToken.getUser().getTimeZone());
                loginResponse.setLanguage(refreshToken.getUser().getLanguage());
                return loginResponse;
            } else {
                // Increment login attempts on failed login
                userRepository.userLoginIncrementAttempts(username);
                loginResponse.setError("Authentication failed. Invalid username, password, or role.");
            }
        } catch (Exception e) {
            userRepository.userLoginIncrementAttempts(loginRequest.getUserName());
            loginResponse.setError("Authentication failed. Invalid username, password, or role.");
            System.out.println(e.getMessage());
        }
        return loginResponse;

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
