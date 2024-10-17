package com.wipro.jcb.livelink.app.alerts.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlertInfoResponse {
    @Schema(description =  "Detail about alert for provided machine", required = true)
    private AlertInfoData alert;
    @Schema(description =  "Service history", required = true)
    private List<ServiceHistoryDetails> history;

    @Override
    public String toString() {
        return "AlertInfoResponse [alert=" + alert + ", history=" + history + "]";
    }
}
