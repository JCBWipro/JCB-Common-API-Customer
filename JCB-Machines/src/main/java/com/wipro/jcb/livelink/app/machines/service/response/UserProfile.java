package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "First name of user", example = "Leroy")
    private String firstName;
    @ApiModelProperty(value = "Last name of user", example = "Holland")
    private String lastName;
    @ApiModelProperty(value = "Email of user", example = "Leroy@mymail.com")
    private String email;
    @ApiModelProperty(value = "contact of user", example = "9807867676")
    private String number;
    @ApiModelProperty(value = "Image of user", example = "URL for Leroy.png")
    private String image;
    @ApiModelProperty(value = "Thumbnail image of user", example = "URL for Leroy.png")
    private String thumbnail;
    @ApiModelProperty(value = "User nationality", example = "India")
    private String country;
    @ApiModelProperty(value = "Language decided by user", example = "English")
    private String smsLanguage;
    @ApiModelProperty(value = "Current timeZone ", example = "GMT+5:50")
    private String timeZone;
    @ApiModelProperty(value = "User role ", example = "roleName of user")
    private String roleName;

}
