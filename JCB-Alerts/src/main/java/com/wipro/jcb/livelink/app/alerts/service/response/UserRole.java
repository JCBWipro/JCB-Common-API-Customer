package com.wipro.jcb.livelink.app.alerts.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 *
 */
@Setter
@Getter
public class UserRole {
    @Schema(description = "User Role/Type", example = "Customer", required = true)
    String name;

    public UserRole(String name) {
        super();
        this.name = name;
    }

}
