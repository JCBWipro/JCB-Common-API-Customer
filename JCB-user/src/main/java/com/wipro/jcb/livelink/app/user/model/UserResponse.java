package com.wipro.jcb.livelink.app.user.model;

public class UserResponse {
	
	private int id;
	private String firstName;
	private String lastName;
	private Long age;
	
	public UserResponse(){}
	
	public UserResponse(int id, String firstName, String lastName, Long age) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.id = id;
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
	
	

}
