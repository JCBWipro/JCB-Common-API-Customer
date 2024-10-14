package com.wipro.jcb.livelink.app.reports.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * This Class is to Handle Response related to FuelConsumptionBHL
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelConsumptionBHL {

    @Schema(description = "Day", example = "2024-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "totalFuelUsedInLtrs", example = "1.1", required = true)
    @JsonProperty("total_fuel_used_in_ltrs")
    private Double totalFuelUsedInLtrs;

    @Override
    public String toString() {
        return "FuelConsumptionBHL [day=" + day + ", totalFuelUsedInLtrs=" + totalFuelUsedInLtrs + "]";
    }

}
