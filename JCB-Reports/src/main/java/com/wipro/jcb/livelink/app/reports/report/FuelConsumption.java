package com.wipro.jcb.livelink.app.reports.report;

import java.util.Date;

/**
 * This interface is to Handle Response related to FuelConsumption
 */
public interface FuelConsumption{

    Date getdate();
    Double gettotalFuelUsed();
    Double getfuelAverage();
    
}

