package com.wipro.jcb.livelink.app.machines.service.reports;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public interface FuelConsumption{

    Date getdate();
    Double gettotalFuelUsed();
    Double getfuelAverage();
    
}

