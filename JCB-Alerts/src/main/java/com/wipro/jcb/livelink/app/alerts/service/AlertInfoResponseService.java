package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.dto.AlertResponse;
import com.wipro.jcb.livelink.app.alerts.entity.Alert;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertInfoResponse;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertObject;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 *
 */
@Component
public interface AlertInfoResponseService {
    AlertInfoResponse getAlertInfo(final Alert alert, String startDate, String endDate)
            throws ProcessCustomError;

    AlertResponse getAlerts(String userName, String startDate, String endDate, int pageNumber, int pageSize,
                            String filter, String search, Boolean isVTwo) throws ProcessCustomError;

    AlertObject getAlertInfoObj(String userName, String id, String vin, String startDate, String endDate)
            throws ProcessCustomError;
}