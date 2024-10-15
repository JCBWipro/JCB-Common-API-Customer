package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfile {
    @Schema(description = "First name of user", example = "Leroy")
    private String firstName;
    @Schema(description ="Last name of user", example = "Holland")
    private String lastName;
    @Schema(description = "Email of user", example = "Leroy@mymail.com")
    private String email;
    @Schema(description = "contact of user", example = "9807867676")
    private String number;
    @Schema(description = "Image of user", example = "URL for Leroy.png")
    private String image;
    @Schema(description = "Thumbnail image of user", example = "URL for Leroy.png")
    private String thumbnail;
    @Schema(description = "User nationality", example = "India")
    private String country;
    @Schema(description = "Language decided by user", example = "English")
    private String smsLanguage;
    @Schema(description = "Current timeZone ", example = "GMT+5:50")
    private String timeZone;
    @Schema(description = "User role ", example = "roleName of user")
    private String roleName;

}
