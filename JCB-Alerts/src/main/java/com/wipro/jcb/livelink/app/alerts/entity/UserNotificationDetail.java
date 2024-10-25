package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_notification_detail", indexes = {
        @Index(name = "notificationindex", columnList = "user_name,push_notification_token,user_type")})
public class UserNotificationDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 6836734149398846518L;

    @Id
    @Column(name = "push_notification_token")
    private String pushNotificationToken;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "enable_notification")
    private Boolean enableNotification = true;

    @Column(name = "user_type")
    private String userType;

    private String os;

    @Column(name = "enable_machine_update")
    private Boolean enableMachineUpdate = false;

    @Column(name = "working_machine_count")
    private int workingMachineCount;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "access_token")
    private String accessToken;
}
