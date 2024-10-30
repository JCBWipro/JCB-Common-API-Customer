package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 */

/**
 * Platforms is a data model class that represents the name of a platform.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Platforms {
    @ApiModelProperty(value = "Name of platform", example = "India", required = true)
    String platform;
}
