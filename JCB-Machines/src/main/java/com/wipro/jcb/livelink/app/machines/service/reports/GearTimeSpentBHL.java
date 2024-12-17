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
@NoArgsConstructor
@AllArgsConstructor
public class GearTimeSpentBHL {
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "firstGear", example = "1.1", required = true)
    @JsonProperty("firstGear")
    private Double firstGear;

    @Schema(description = "secoundGear", example = "1.1", required = true)
    @JsonProperty("secoundGear")
    private Double secoundGear;

    @Schema(description = "thirdGear", example = "1.1", required = true)
    @JsonProperty("thirdGear")
    private Double thirdGear;

    @Schema(description = "forthGear", example = "1.1", required = true)
    @JsonProperty("forthGear")
    private Double forthGear;

    @Override
    public String toString() {
        return "GearTimeSpentBHL [day=" + day + ", firstGear=" + firstGear + ", secoundGear=" + secoundGear
                + ", thirdGear=" + thirdGear + ", forthGear=" + forthGear + "]";
    }



}
