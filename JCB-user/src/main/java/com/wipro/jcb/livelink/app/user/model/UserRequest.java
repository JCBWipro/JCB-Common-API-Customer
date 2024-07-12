package com.wipro.jcb.livelink.app.user.model;

import java.util.List;

public class UserRequest {
	
	private String firstName;
	private String lastName;
	private Long age;
	private List<ContactRequest> contactRequest;
	
	UserRequest(){}

	public UserRequest(String firstName, String lastName, Long age, List<ContactRequest> contactRequest) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.contactRequest = contactRequest;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public List<ContactRequest> getContactRequest() {
		return contactRequest;
	}

	public void setContactRequest(List<ContactRequest> contactRequest) {
		this.contactRequest = contactRequest;
	}
	
	
	
}
