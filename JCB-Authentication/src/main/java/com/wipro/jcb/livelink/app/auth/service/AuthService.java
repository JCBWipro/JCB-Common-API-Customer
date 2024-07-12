package com.wipro.jcb.livelink.app.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String generateToken(String username, String role) {
        return jwtService.generateToken(username, role);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
