package com.wipro.jcb.livelink.app.mob.auth.service;

import com.wipro.jcb.livelink.app.mob.auth.dto.LoginRequest;
import com.wipro.jcb.livelink.app.mob.auth.dto.LoginResponse;
import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:29-08-2024
 * project: JCB-Common-API-Customer
 */

@Service
public class LoginServiceMobile {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceMobile.class);

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    public LoginResponse authenticateAndGenerateToken(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            String username = loginRequest.getUserName();
            int attempts = userRepository.userLoginGetAttempts(username);
            if (attempts >= 5) {
                User user = userRepository.findByUserUserId(username);
                if (user != null) {
                    Calendar cal1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf1.setTimeZone(TimeZone.getTimeZone("IST"));
                    String timeStamp = sdf1.format(cal1.getTime());
                    try {
                        user.setLockedOutTime(Timestamp.valueOf(new Timestamp(sdf1.parse(timeStamp).getTime()).toLocalDateTime()));
                    } catch (ParseException e) {
                        log.error("Error parsing date: {}", timeStamp, e);
                        loginResponse.setError("Failed to lock account due to a timestamp format error.");
                        return loginResponse;
                    }
                    userRepository.save(user);
                    log.info("Account locked time is :{}", timeStamp);
                    loginResponse.setError("Maximum login attempts reached. Account locked.");
                    return loginResponse;
                }
            } else {
                Authentication authenticate = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
                );
                String userType = authenticate.getAuthorities().iterator().next().toString();
                if (authenticate.isAuthenticated() && userType.equals(loginRequest.getUserType())) {
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
                    userRepository.userLoginIncrementAttempts(username);
                    loginResponse.setError("Authentication failed. Invalid username, password, or role.");
                }
            }
        } catch (Exception e) {
            userRepository.userLoginIncrementAttempts(loginRequest.getUserName());
            loginResponse.setError("Authentication failed. Invalid username, password, or role.");
            log.error("Authentication failed for user: {}. Error: {}", loginRequest.getUserName(), e.getMessage());
        }
        return loginResponse;
    }
}
