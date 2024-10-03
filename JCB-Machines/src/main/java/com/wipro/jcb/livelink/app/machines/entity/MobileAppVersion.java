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

    @Column(name = "current_version")
    private String currentVersion;

    @Column(name = "blocked_version")
    private String blockedVersion;

   @Column(name = "recent_version")
    private String recentVersion;

    public MobileAppVersion(String os, String currentVersion, String blockedVersion) {
        this.os = os;
        this.currentVersion = currentVersion;
        this.blockedVersion = blockedVersion;
    }

    public MobileAppVersion(String os, String recentVersion) {
        this.os = os;
        this.recentVersion = recentVersion;
    }
}