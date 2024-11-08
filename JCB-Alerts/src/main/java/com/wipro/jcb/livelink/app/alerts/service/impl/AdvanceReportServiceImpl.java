package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;
import com.wipro.jcb.livelink.app.alerts.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.alerts.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Service
@PropertySource("application.properties")
public class AdvanceReportServiceImpl implements AdvanceReportService {

    @Autowired
    MachineService machineService;

    @Override
    public NotificationListResponseDto getNotificationListByGroupingDate(String userName, Date startDate, Date endDate, String pageNumber, String pageSize){
        return machineService.getNotificationListByGroupingDate(userName,startDate,endDate,pageNumber,pageSize);
    }
}
