package com.wipro.jcb.livelink.app.mob.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.jcb.livelink.app.mob.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.mob.auth.dto.JwtResponse;
import com.wipro.jcb.livelink.app.mob.auth.dto.LoginRequest;
import com.wipro.jcb.livelink.app.mob.auth.dto.LoginResponse;
import com.wipro.jcb.livelink.app.mob.auth.dto.RefreshTokenRequest;
import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import com.wipro.jcb.livelink.app.mob.auth.service.JwtService;
import com.wipro.jcb.livelink.app.mob.auth.service.RefreshTokenService;
import com.wipro.jcb.livelink.app.mob.auth.service.ResetPasswordService;

@RestController
@RequestMapping("/auth/mob")
public class UserController {
	
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordService resetPasswordService;
    
    @GetMapping("/registerUser")
    public String saveUser() {

        User user = new User();
        user.setMachineUpdateNotificationEnabled(true);
        user.setLanguage("English");
        user.setUserAppVersion("4.1.1");
        user.setUserType(UserType.Dealer);
        user.setFirstName("Gokul");
        user.setLastName("Aher");
        user.setEmail("gokul.ahir@gmail.com");
        user.setPhoneNumber("9890091680");
        user.setImage("abc.png");
        user.setThumbnail("abcd.png");
        user.setCountry("India");
        user.setSmsLanguage("English");
        user.setTimeZone("GMT+5:30");
        user.setSysGenPass(true);
        user.setRoleName("Customer-Fleet-Manager");
        user.setLanguage("English");
        user.setAddress("HYD");

        String username = AuthCommonutils.generateUsername(user.getFirstName());
        System.out.println("username:"+username);
        user.setUserName(username);
        String autoGenPassword = resetPasswordService.generatePassword();
        System.out.println("autoGenPassword:"+autoGenPassword);
        user.setPassword(autoGenPassword);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "User Saved in DB";

    }
	
	@PostMapping("/login")
    public LoginResponse getToken(@RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = new LoginResponse();
		try {
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
	        String userType = authenticate.getAuthorities().iterator().next().toString();
	        if (authenticate.isAuthenticated() && userType.equals(loginRequest.getUserType())){
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
	            loginResponse.setError("Authentication failed. Invalid username, password, or role.");
	        }
		}catch(Exception e) {
			loginResponse.setError("Authentication failed. Invalid username, password, or role.");
			System.out.println(e.getMessage());
		}
		return loginResponse;
        
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
