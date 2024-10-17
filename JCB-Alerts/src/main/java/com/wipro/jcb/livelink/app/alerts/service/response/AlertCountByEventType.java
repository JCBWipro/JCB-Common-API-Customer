package com.wipro.jcb.livelink.app.alerts.service.response;

import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class AlertCountByEventType {
    @Schema(description =  "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private Long machineCount;
    @Schema(description =  "XXXXX", required = true)
    private EventType eventType;
    @Override
    public String toString() {
        return "AlertCountByEventType [machineCount=" + machineCount + ", eventType=" + eventType + "]";
    }
}
