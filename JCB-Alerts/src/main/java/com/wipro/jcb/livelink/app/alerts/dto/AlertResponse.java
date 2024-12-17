package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertCount;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@ToString
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
}
