package com.wipro.jcb.livelink.app.machines.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@SecuritySchemes(value = {@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")})
@OpenAPIDefinition(info = @Info(title = "Authorization API", version = "1.0"))
@Retention(RetentionPolicy.RUNTIME)
public @interface AddAuthorization {
}
