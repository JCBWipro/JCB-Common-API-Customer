package com.wipro.jcb.livelink.app.reports.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * This Class is to Handle Response related to DutyCycleBHL
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DutyCycleBHL {
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "travel attachment", example = "1.1", required = true)
    @JsonProperty("attachment")
    private Double attachment;

    @Schema(description = "idling", example = "1.1", required = true)
    @JsonProperty("idling")
    private Double idling;

    @Schema(description = "excavation", example = "1.1", required = true)
    @JsonProperty("excavation")
    private Double excavation;

    @Schema(description = "loading", example = "1.1", required = true)
    @JsonProperty("loading")
    private Double loading;

    @Schema(description = "roading", example = "1.1", required = true)
    @JsonProperty("roading")
    private Double roading;

    @Override
    public String toString() {
        return "DutyCycleBHL [day=" + day + ", attachment=" + attachment + ", idling=" + idling + ", excavation="
                + excavation + ", loading=" + loading + ", roading=" + roading + "]";
    }
}
