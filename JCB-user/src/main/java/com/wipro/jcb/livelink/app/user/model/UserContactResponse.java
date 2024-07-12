package com.wipro.jcb.livelink.app.user.model;

import java.util.List;

public class UserContactResponse {

	private int id;
	private String firstName;
	private String lastName;
	private Long age;
	private List<ContactResponse> contact;

	public UserContactResponse() {
	}

	public UserContactResponse(int id, String firstName, String lastName, Long age, List<ContactResponse> contact) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<ContactResponse> getContact() {
		return contact;
	}

	public void setContact(List<ContactResponse> contact) {
		this.contact = contact;
	}

}
