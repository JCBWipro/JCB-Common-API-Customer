package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.dto.AuthRequest;
import com.wipro.jcb.livelink.app.auth.dto.JwtResponse;
import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
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
 * Date:14-08-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    ContactRepo contactRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    public JwtResponse authenticateAndGenerateToken(AuthRequest authRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            String username = authRequest.getUsername();
            int attempts = contactRepo.userLoginGetAttempts(username);
            if (attempts > 5) {
                ContactEntity contactEntity = contactRepo.findByUserContactId(username);
                if (contactEntity != null) {
                    Calendar cal1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf1.setTimeZone(TimeZone.getTimeZone("IST"));
                    String timeStamp = sdf1.format(cal1.getTime());
                    try {
                        contactEntity.setLockedOutTime(Timestamp.valueOf(new Timestamp(sdf1.parse(timeStamp).getTime()).toLocalDateTime()));
                    } catch (ParseException e) {
                        log.error("Error parsing date: {}", timeStamp, e);
                        jwtResponse.setError("Failed to lock account due to an timeStamp Format error.");
                        return jwtResponse;
                    }
                    contactRepo.save(contactEntity);
                    log.info("Account locked time is :{}", timeStamp);
                    jwtResponse.setError("Maximum login attempts reached. Account locked.");
                    return jwtResponse;
                }
            } else {
                Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
                String role = authenticate.getAuthorities().iterator().next().toString();
                String roleName = AuthCommonutils.getRolesByID(role);
                if (authenticate.isAuthenticated() && roleName.equals(authRequest.getRole())) {
                    contactRepo.userLoginResetAttempts(username);
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
                    jwtResponse.setJwtToken(jwtService.generateToken(authRequest.getUsername(), roleName));
                    jwtResponse.setTokenId(refreshToken.getToken());
                    return jwtResponse;
                } else {
                    contactRepo.userLoginIncrementAttempts(username);
                    jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
                }
            }
        } catch (Exception e) {
            contactRepo.userLoginIncrementAttempts(authRequest.getUsername());
            jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
            System.out.println(e.getMessage());
        }
        return jwtResponse;
    }
}
