package com.wipro.jcb.livelink.app.machines.service.response;

/**
 * This LocationHistory is to handle Latitude and Longitude response for history of Machine Location
 */
public interface LocationHistory {

	Double getlatitude();
	Double getlongitude();
	String getDateTime();

}
