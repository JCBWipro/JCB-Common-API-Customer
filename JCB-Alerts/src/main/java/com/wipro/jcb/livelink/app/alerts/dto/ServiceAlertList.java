package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertCount;
import lombok.*;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/23/2024
 */
@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceAlertList {
    List<ServiceAlert> serviceAlert;
    AlertCount alertCount;
}
