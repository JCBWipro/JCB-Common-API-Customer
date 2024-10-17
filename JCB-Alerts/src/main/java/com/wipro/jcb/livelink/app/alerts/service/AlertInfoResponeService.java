package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.dto.AlertResponse;
import com.wipro.jcb.livelink.app.alerts.entity.Alert;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertInfoResponse;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 *
 */
@Component
public interface AlertInfoResponeService {
    AlertInfoResponse getAlertInfo(final Alert alert, String startDate, String endDate)
            throws ProcessCustomError;

   /* AlertObject getAlertInfoObj(String userName, String id, String vin, String startDate, String endDate)
            throws ProcessCustomError;*/

    AlertResponse getAlerts(String userName, String startDate, String endDate, int pageNumber, int pageSize,
                            String filter, String search, Boolean isVTwo) throws ProcessCustomError;

    //ServiceAlertInfo getServiceAlertInfo(final Alert alert, String vin) throws ProcessCustomError;

    //ServiceAlert getServiceAlertInfoObj(String userName, String id, String vin) throws ProcessCustomError;

    /*ServiceAlertList getServiceAlertsList(String userName, String startDate, String endDate, int pageNumber,
                                          int pageSize, String filter, String search, Boolean isVTwo) throws ProcessCustomError;*/

    //boolean setAlertReadFlag(String userName, List<String> alertIds) throws ProcessCustomError;
}