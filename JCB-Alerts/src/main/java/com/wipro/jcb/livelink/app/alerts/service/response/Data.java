package com.wipro.jcb.livelink.app.alerts.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-11-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Data {

    @Schema(description = "Alert Description", example = "You have 3 new health alert", required = true)
    private String alertDesc;
    @Schema(description = "Type of alert", example = "Health Alert", required = true)
    private String alertType;
    @Schema(description = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    private String alertKey;
    @Schema(description = "Name", example = "New Health Alert", required = true)
    private String title;
}
