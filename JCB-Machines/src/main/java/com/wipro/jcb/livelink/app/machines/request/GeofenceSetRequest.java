package com.wipro.jcb.livelink.app.machines.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This GeofenceSetRequest is to set Location based related data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceSetRequest {

	private Double centerLatitude;
	private Double centerLongitude;
	private Long radis;
	private String vin;

}
