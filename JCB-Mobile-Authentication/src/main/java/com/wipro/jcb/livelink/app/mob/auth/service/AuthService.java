package com.wipro.jcb.livelink.app.mob.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    public String generateToken(String username, String roleName) {
        return jwtService.generateToken(username, roleName);
    }
    
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
