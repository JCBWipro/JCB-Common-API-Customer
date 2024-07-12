package com.wipro.jcb.livelink.app.user.model;

public class ContactResponse {
	
	private int id;
	private String email;
	private String mobileNumber;
	private String city;
	private String zip;
	private String addressLine1;
	private String addressLine2;
	private String pincode;
	
	public ContactResponse(){}
	
	public ContactResponse(int id, String email, String mobileNumber, String city, String zip, String addressLine1,
			String addressLine2, String pincode) {
		super();
		this.id = id;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.city = city;
		this.zip = zip;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.pincode = pincode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	
}
