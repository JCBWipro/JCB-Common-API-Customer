package com.wipro.jcb.livelink.app.machines.commonUtils;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/3/2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@XmlRootElement
public class AppToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -3973131399546447256L;
    /*
     * appFCMKey : Unique Device Key
     */
    private String appFCMKey;
    /*
     * expiresAt : Session expire date time
     */
    private Long expiresAt;
    /*
     * issuedAt : token issue date time
     */
    private Long issuedAt;
    /*
     * os : os used to connect to appserver eg : Android/IoS
     */
    private String os;
    /*
     * enable : Represents active/inactive eg : true/false
     */
    private Boolean enable;

    public AppToken(String appFCMKey, Long expiresAt, Long issuedAt, String os, Boolean enable) {
        super();
        this.appFCMKey = appFCMKey;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.os = os;
        this.enable = enable;
    }

    public AppToken() {
        super();
    }

    @Override
    public String toString() {
        return "AppToken [appFCMKey=" + appFCMKey + ", expiresAt=" + expiresAt + ", issuedAt=" + issuedAt + ", os=" + os
                + ", enable=" + enable + "]";
    }
}
