package com.wipro.jcb.livelink.app.machines.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MobileAppVersion", indexes = {
        @Index(name = "MobileAppVersionIndexes", columnList = "os")})
public class MobileAppVersion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String os;

    private String currentVersion;

    private String blockedVersion;

    private String recentVersion;

    public MobileAppVersion(String android, String curAndroidVer, String blkAndroidVer) {
    }

    public MobileAppVersion(String android, String android1) {
    }
}