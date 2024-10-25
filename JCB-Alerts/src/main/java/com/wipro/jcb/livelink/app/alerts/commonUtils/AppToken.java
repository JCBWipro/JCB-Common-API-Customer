package com.wipro.jcb.livelink.app.alerts.commonUtils;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}