package com.wipro.jcb.livelink.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;


import com.wipro.jcb.livelink.app.user.entity.Contact;
import com.wipro.jcb.livelink.app.user.entity.User;
import com.wipro.jcb.livelink.app.user.model.ContactRequest;
import com.wipro.jcb.livelink.app.user.model.ContactResponse;
import com.wipro.jcb.livelink.app.user.model.UserContactResponse;
import com.wipro.jcb.livelink.app.user.model.UserRequest;
import com.wipro.jcb.livelink.app.user.model.UserResponse;
import com.wipro.jcb.livelink.app.user.repo.ContactRepository;
import com.wipro.jcb.livelink.app.user.repo.UserRepository;

@Service
@CacheConfig(cacheNames="{User}")
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactRepository contactRepository;
	// you can use the @CacheEvict annotation, because it doesn't add anything to the cache unlike the @Cacheable annotation.
	//Use CacheEvict when you have to remove stale or unused data from the cache
	
	@CacheEvict(value="users", allEntries=true)
	public void saveUser(UserRequest userRequest) {
		User user = new User();
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setAge(userRequest.getAge());
		User newUser = userRepo.save(user);
		List<Contact> listOfContact = new ArrayList<>();
		for(ContactRequest contactRequest : userRequest.getContactRequest()){
			Contact contact = new Contact();
			contact.setUser(newUser);
			contact.setAddressLine1(contactRequest.getAddressLine1());
			contact.setAddressLine2(contactRequest.getAddressLine2());
			contact.setCity(contactRequest.getCity());
			contact.setMobileNumber(contactRequest.getMobileNumber());
			contact.setEmail(contactRequest.getEmail());
			contact.setPincode(contactRequest.getPincode());
			contact.setZip(contactRequest.getZip());
			listOfContact.add(contact);
		}
		contactRepository.saveAll(listOfContact);
	}
	
	public UserResponse findUserById(int id) throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("findUserById() called from DB");
		User user = userRepo.findUserById(id).get();
		UserResponse userResponse = new UserResponse();
		userResponse.setId(user.getId());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setAge(user.getAge());
		return userResponse;
	}
	
	public List<UserResponse> findAllUsers() throws InterruptedException {
		System.out.println("findAllUserDetails() called from DB");
		List<User> listOfUser = userRepo.findAllUsers();
		List<UserResponse> listOfUsers = new ArrayList<>();
		for(User user : listOfUser) {
			UserResponse userResponse = new UserResponse();
			userResponse.setId(user.getId());
			userResponse.setFirstName(user.getFirstName());
			userResponse.setLastName(user.getLastName());
			userResponse.setAge(user.getAge());
			listOfUsers.add(userResponse);
		}
		return listOfUsers;
	}
	
	public UserContactResponse getUserDetailsById(int id) {
		List<Object[]> listOfUsers = userRepo.findUserDetailsByUserId(id);
		List<ContactResponse> listOfContactResponse = new ArrayList<>();
		UserContactResponse userContactResponse = null;
		for(Object[] data : listOfUsers) {
			userContactResponse = new UserContactResponse();
			userContactResponse.setId((int) data[0]);
			userContactResponse.setAge((Long) data[1]);
			userContactResponse.setFirstName(data[2].toString());
			userContactResponse.setLastName(data[3].toString());
			ContactResponse contactResponse = new ContactResponse();
			contactResponse.setId((int)data[4]);
			contactResponse.setAddressLine1(data[5].toString());
			contactResponse.setAddressLine2(data[6].toString());
			contactResponse.setCity(data[7].toString());
			contactResponse.setEmail(data[8].toString());
			contactResponse.setMobileNumber(data[9].toString());
			contactResponse.setPincode(data[10].toString());
			contactResponse.setZip(data[11].toString());
			listOfContactResponse.add(contactResponse);
			userContactResponse.setContact(listOfContactResponse);
		}
		
		return userContactResponse;
	}
	
}
