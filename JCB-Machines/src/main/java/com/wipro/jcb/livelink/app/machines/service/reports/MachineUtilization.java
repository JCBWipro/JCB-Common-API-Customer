package com.wipro.jcb.livelink.app.machines.service.reports;

import java.util.Date;

/**
 * This MachineUtilization is to Handle Response related to Machine Utilization
 * Data
 */
public interface MachineUtilization {

	Date getdate();
	Double getworking();
	Double getidle();
	Double getoff();
}
