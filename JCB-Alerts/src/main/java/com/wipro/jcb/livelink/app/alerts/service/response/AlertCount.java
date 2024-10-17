package com.wipro.jcb.livelink.app.alerts.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertCount {
    @Schema(description = "service", example = "300")
    private Integer service = 0;
    @Schema(description = "health", example = "500")
    private Integer health =0;
    @Schema(description = "security", example = "200")
    private Integer security= 0;
    @Schema(description = "utilization", example = "300")
    private Integer utilization=0;
    @Schema(description = "location", example = "800")
    private Integer location =0;

    @Override
    public String toString() {
        return "AlertCount [service=" + service + ", health=" + health + ", security=" + security + ", utilization="
                + utilization + ", location=" + location + "]";
    }
}
