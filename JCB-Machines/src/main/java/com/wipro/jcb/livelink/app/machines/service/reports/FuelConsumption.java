package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
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
public class FuelConsumption implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date date;

    @Schema(description = "Total Fuel Used", example = "0.0", required = true)
    @JsonProperty("totalFuelUsed")
    private Double totalFuelUsed;

    @Schema(description = "Fuel Average", example = "0.0", required = true)
    @JsonProperty("fuelAverage")
    private Double fuelAverage;

    @Override
    public String toString() {
        return "FuelConsumption [date=" + date + ", totalFuelUsed=" + totalFuelUsed + "]";
    }

}

