package com.wipro.jcb.livelink.app.alerts.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
public class AlertInfoData {
    @Schema(description =  "Alert Description", example = "The machine is approaching service", required = true)
    private String description;
    @Schema(description =  "Alert Location", example = "Pune", required = true)
    private String location;
    @Schema(description =  "Machine Platform", example = "Backhoe Loader", required = true)
    private String profile;
    @Schema(description =  "Dealer name for the machine", example = "Dealer Name", required = true)
    private String dealerName;
    @Schema(description =  "Dealer phone no for the machine", example = "123456789", required = true)
    private String dealerNumber;
    @Schema(description =  "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    private EventType alertType;
    @Schema(description =  "Nature of fault", example = "-", required = true)
    private String natureOfFault;
    @Schema(description =  "name of model", example = "2DX", required = true)
    private String modelName;
    @Schema(description =  "isDtcAlert", example = "false", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;

}
