package com.wipro.jcb.livelink.app.machines.reports;

import java.util.Date;

/**
 * This MachineUtilization is to Handle Response related to Machine Utilization
 * Data
 */
public interface MachineUtilization {

	Date getday();
	Double getworking_hours();
	Double getidle_hours();
	Double getoff_hours();
}
