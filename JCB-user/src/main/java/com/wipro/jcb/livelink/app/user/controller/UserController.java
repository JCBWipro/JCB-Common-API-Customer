package com.wipro.jcb.livelink.app.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.wipro.jcb.livelink.app.user.model.ContactResponse;
import com.wipro.jcb.livelink.app.user.model.UserContactResponse;
import com.wipro.jcb.livelink.app.user.model.UserRequest;
import com.wipro.jcb.livelink.app.user.model.UserResponse;
import com.wipro.jcb.livelink.app.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Value("${contactUri}")
	public String contactUri;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private UserService userService;

	@GetMapping
	public String getString(@RequestHeader("LoggedInUserRole") String role) {
		if(role.equals("ROLE_USER")) {
			return role+" Not Authorized to Access this API";
		} else {
			return "User Service";
		}
		
	}

	@GetMapping("/getresponseFromContactService")
	public String getresponseFromContactService() {
		return webClientBuilder.build().get().uri(contactUri+"/api/contact").retrieve().bodyToMono(String.class).block();
	}

	@PostMapping("/saveUserWithContact")
	public void saveUserWithContact(@RequestBody UserRequest userRequest) {
		userService.saveUser(userRequest);
	}

	@Cacheable(key = "#id", value = "usersById")
	@GetMapping("/getUserById/{id}")
	public UserResponse getUserById(@PathVariable int id) throws InterruptedException {
		System.out.println("From Controller: getUserById() Executed");
		return userService.findUserById(id);
	}

	@Cacheable(value = "users")
	@GetMapping("/getAllUsers")
	public List<UserResponse> getAllUsers() throws InterruptedException {
		return userService.findAllUsers();
	}

	@GetMapping("/getUserDetailsById/{id}")
	public UserContactResponse getUserDetailsById(@PathVariable int id) {
		return userService.getUserDetailsById(id);
	}
	
	@GetMapping("/getContactDetailsByUserID/{id}")
	public List<ContactResponse> getContactDetailsByUserID(@PathVariable int id) {
		ParameterizedTypeReference<List<ContactResponse>> responseType = new ParameterizedTypeReference<List<ContactResponse>>(){};
		System.out.println("In UserService: Call Made to Contact Service to fetch ContactDetailsByUserID "+id);
		ResponseEntity<List<ContactResponse>> responseEntity = webClientBuilder.build().get().uri(contactUri+"/api/contact/getContactDetailsByUserID/"+id).retrieve().toEntity(responseType).block();
		List<ContactResponse> contactResponse = responseEntity.getBody();
		return contactResponse;
	}

}
