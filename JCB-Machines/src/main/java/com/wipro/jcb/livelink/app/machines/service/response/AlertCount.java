package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertCount {
    @ApiModelProperty(value = "service", example = "300", required = false)
    private Integer service = 0;
    @ApiModelProperty(value = "health", example = "500", required = false)
    private Integer health =0;
    @ApiModelProperty(value = "security", example = "200", required = false)
    private Integer security= 0;
    @ApiModelProperty(value = "utilization", example = "300", required = false)
    private Integer utilization=0;
    @ApiModelProperty(value = "location", example = "800", required = false)
    private Integer location =0;

    public AlertCount() {
    }

    public AlertCount(Integer service, Integer health, Integer security, Integer utilization, Integer location) {
        super();
        this.service = service;
        this.health = health;
        this.security = security;
        this.utilization = utilization;
        this.location = location;
    }

    @Override
    public String toString() {
        return "AlertCount [service=" + service + ", health=" + health + ", security=" + security + ", utilization="
                + utilization + ", location=" + location + "]";
    }
}
