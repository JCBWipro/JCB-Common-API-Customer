package com.wipro.jcb.livelink.app.mob.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:01-08-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class PasswordUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "New password is required")
    private String newPassword;
}
