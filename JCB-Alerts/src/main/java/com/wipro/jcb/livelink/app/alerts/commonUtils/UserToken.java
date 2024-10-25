package com.wipro.jcb.livelink.app.alerts.commonUtils;

import com.wipro.jcb.livelink.app.alerts.constants.UserType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Setter
@Getter
@AllArgsConstructor
@XmlRootElement
@ToString
public class UserToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /*
     * userName : unique identifier of user
     */
    private String userName;
    /*
     * accessToken : represent session key and app token mapping
     */
    private ConcurrentHashMap<String, AppToken> accessToken;
    public UserToken() {
        this.accessToken = new ConcurrentHashMap<>(); // Initialize the map here
    }
    /*
     * livelinkToken : represent livelink server token
     */
    private String livelinkToken;
    /*
     * userType : represent type of user
     */
    private UserType userType;
    /*
     * expiresAt : linelink token expire date time
     */
    private Long expiresAt;
    /*
     * issuedAt : token issue date time
     */
    private Long issuedAt;
    /*
     * isActive : represent whether user is active
     */
    private Boolean isActive;
    /*
     * userInactiveAt : user inactive date time
     */
    private Long userInactiveAt;

    private ConcurrentHashMap<String, AppToken> accessTokenMap;

}
