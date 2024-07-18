package com.wipro.jcb.livelink.app.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.dto.AuthRequest;
import com.wipro.jcb.livelink.app.auth.dto.JwtResponse;
import com.wipro.jcb.livelink.app.auth.dto.RefreshTokenRequest;
import com.wipro.jcb.livelink.app.auth.entity.ClientEntity;
import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.IndustryEntity;
import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.auth.entity.RoleEntity;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.AuthService;
import com.wipro.jcb.livelink.app.auth.service.JwtService;
import com.wipro.jcb.livelink.app.auth.service.RefreshTokenService;
import com.wipro.jcb.livelink.app.auth.service.ResetPasswordService;


@RestController
@RequestMapping("/auth/web")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResetPasswordService resetPasswordService;
    
    @Autowired
    private ContactRepo contactRepo;

    @PostMapping("/register")
    public String saveContact(@RequestBody ContactEntity contactEntity) {

        String username = AuthCommonutils.generateUsername(contactEntity.getFirst_name());
        contactEntity.setContactId(username);
        System.out.println("Username is :" + contactEntity.getContactId());
        String autoGenPassword = resetPasswordService.generatePassword();
        contactEntity.setPassword(autoGenPassword);
        System.out.println("Password is :" + contactEntity.getPassword());
        contactEntity.setPassword(new BCryptPasswordEncoder().encode(contactEntity.getPassword()));
        RoleEntity roleEntity = new RoleEntity();
        
        
        IndustryEntity industryEntity = new IndustryEntity();
        industryEntity.setIndustry_id(1);
        industryEntity.setIndustry_name("DIndustry");

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClient_id(1);
        clientEntity.setClient_name("DClientname");
        clientEntity.setIndustry_id(industryEntity);

        contactEntity.setClient_id(clientEntity);
        contactRepo.save(contactEntity);
        return "Contact Saved !!!";
    }


    @PostMapping("/login")
    public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
    	JwtResponse jwtResponse = new JwtResponse();
    	try {
    		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            String role = authenticate.getAuthorities().iterator().next().toString();
            String roleName = AuthCommonutils.getRolesByID(role);
            if (authenticate.isAuthenticated() && roleName.equals(authRequest.getRole())) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
                jwtResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername(), roleName));
                jwtResponse.setToken(refreshToken.getToken());
                return jwtResponse;
            } else {
            	jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
            }
        } catch (Exception e) {
        	jwtResponse.setError("Authentication failed. Invalid username, password, or role.");
            System.out.println(e.getMessage());
        }
    	return jwtResponse;
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getContactEntity)
                .map(contactEntity -> {
                    String roleName = AuthCommonutils.getRolesByID(String.valueOf(contactEntity.getRole().getRole_id()));
                    String accessToken = jwtService.generateToken(contactEntity.getContactId(), roleName);
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setAccessToken(accessToken);
                    jwtResponse.setToken(refreshTokenRequest.getToken());
                    return jwtResponse;
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }

}
