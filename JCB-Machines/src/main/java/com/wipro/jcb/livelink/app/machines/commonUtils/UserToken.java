package com.wipro.jcb.livelink.app.machines.commonUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.wipro.jcb.livelink.app.machines.constants.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserToken implements Serializable {
    
	@Serial
    private static final long serialVersionUID = 1L;
   
    private String userName;
    private ConcurrentHashMap<String, AppToken> accessToken;
    private String livelinkToken;
    private UserType userType;
    private Long expiresAt;
    private Long issuedAt;
    private Boolean isActive;
    private Long userInactiveAt;
    private ConcurrentHashMap<String, AppToken> accessTokenMap;
    
    public UserToken() {
        this.accessToken = new ConcurrentHashMap<>();
    }

}
