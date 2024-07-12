package com.wipro.jcb.livelink.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactResponse {

    private int id;
    private String email;
    private String mobileNumber;

    public ContactResponse() {
    }

    public ContactResponse(int id, String email, String mobileNumber) {
        super();
        this.id = id;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

}
