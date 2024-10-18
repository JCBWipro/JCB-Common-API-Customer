package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.constants.EventLevel;
import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertData {
    @Schema(description = "Generated time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
    @Schema(description = "Nature of fault", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    private EventLevel eventLevel;
    @Schema(description = "Name", example = "Fuel Level is low", required = true)
    private String name;
    @Schema(description = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    private EventType eventType;
    @Schema(description = "Unique identifier for alert", example = "12344556", required = true)
    private String id;
    @Schema(description = "Alert is active or historical", example = "false", allowableValues = "true,false", required = true)
    private Boolean isActive;
    @Schema(description = "is Dtc Alert", example = "false", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;

}
