package com.wipro.jcb.livelink.app.alerts.service.response;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertCountByEventType {
    @Schema(description =  "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private Long machineCount;
    @Schema(description =  "XXXXX", required = true)
    private EventType eventType;
}
