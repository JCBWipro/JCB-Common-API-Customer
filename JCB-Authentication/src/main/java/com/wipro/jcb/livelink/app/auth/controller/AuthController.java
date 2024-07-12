package com.wipro.jcb.livelink.app.auth.controller;

import com.wipro.jcb.livelink.app.auth.dto.AuthRequest;
import com.wipro.jcb.livelink.app.auth.dto.JwtResponse;
import com.wipro.jcb.livelink.app.auth.dto.RefreshTokenRequest;
import com.wipro.jcb.livelink.app.auth.entity.ClientEntity;
import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.IndustryEntity;
import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.service.AuthService;
import com.wipro.jcb.livelink.app.auth.service.JwtService;
import com.wipro.jcb.livelink.app.auth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
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
    private ContactRepo contactRepo;

    @PostMapping("/register")
    public String saveContact(@RequestBody ContactEntity contactEntity) {

        contactEntity.setPassword(new BCryptPasswordEncoder().encode(contactEntity.getPassword()));
        System.out.println(contactEntity.getPassword());
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

    @PostMapping("/token")
    public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        //String roles = authenticate.getAuthorities().iterator().next().toString();
        //if (authenticate.isAuthenticated() && roles.equals(authRequest.getRole())) {
        if (authenticate.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            JwtResponse jwtResponse = new JwtResponse();
            //jwtResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername(), authRequest.getRole()));
            jwtResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername()));
            jwtResponse.setToken(refreshToken.getToken());
            return jwtResponse;
        } else {
            throw new UsernameNotFoundException("invalid User Request !!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getContactEntity)
                .map(contactEntity -> {
                    String accessToken = jwtService.generateToken(contactEntity.getContact_id());
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
