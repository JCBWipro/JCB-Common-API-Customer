package com.wipro.jcb.livelink.app.reports.report;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to MachineCompassBHL
 */
@Data
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

