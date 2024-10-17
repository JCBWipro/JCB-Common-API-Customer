package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertCount;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertResponse {
    @Schema(description = "List of Health alerts generated", required = true)
    List<AlertObject> health;
    @Schema(description =  "List of Security alerts generated", required = true)
    List<AlertObject> security;
    @Schema(description =  "List of Utilization alerts generated", required = true)
    List<AlertObject> utilization;
    @Schema(description = "List of Location alerts generated", required = true)
    List<AlertObject> location;
    private AlertCount alertCount;

    @Override
    public String toString() {
        return "AlertResponse [health=" + health + ", security=" + security + ", utilization=" + utilization
                + ", location=" + location + ", alertCount=" + alertCount + "]";
    }
}
