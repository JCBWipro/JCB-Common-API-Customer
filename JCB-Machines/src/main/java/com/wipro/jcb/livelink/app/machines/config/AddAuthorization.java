package com.wipro.jcb.livelink.app.machines.config;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", defaultValue = "Basic bGl2ZWxpbms6fGleZXxpXms=", example = "Basic bGl2ZWxpbms6fGleZXxpXms=", value = "Authorization", required = true, dataType = "string", paramType = "header"), })
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AddAuthorization {
}
