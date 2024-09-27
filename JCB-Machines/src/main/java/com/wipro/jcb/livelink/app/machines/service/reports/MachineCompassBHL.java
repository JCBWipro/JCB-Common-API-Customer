package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MachineCompassBHL {
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "forwardDirection", example = "1.1", required = true)
    @JsonProperty("forwardDirection")
    private Double forwardDirection;

    @Schema(description = "reverseDirection", example = "1.1", required = true)
    @JsonProperty("reverseDirection")
    private Double reverseDirection;

    @Schema(description = "neutralDirection", example = "1.1", required = true)
    @JsonProperty("neutralDirection")
    private Double neutralDirection;

    @Override
    public String toString() {
        return "MachineCompassBHL [day=" + day + ", forwardDirection=" + forwardDirection + ", reverseDirection="
                + reverseDirection + ", neutralDirection=" + neutralDirection + "]";
    }

}

