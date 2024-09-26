package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
public class FuelConsumptionBHL {

    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "totalFuelUsedInLtrs", example = "1.1", required = true)
    @JsonProperty("total_fuel_used_in_ltrs")
    private Double totalFuelUsedInLtrs;

    public FuelConsumptionBHL(Date day, Double totalFuelUsedInLtrs) {
        super();
        this.day = day;
        this.totalFuelUsedInLtrs = totalFuelUsedInLtrs;
    }

    public FuelConsumptionBHL() {
        super();
    }

    @Override
    public String toString() {
        return "FuelConsumptionBHL [day=" + day + ", totalFuelUsedInLtrs=" + totalFuelUsedInLtrs + "]";
    }

}
