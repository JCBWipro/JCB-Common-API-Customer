package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This GeofenceParam is to handle Location like Latitude and Longitude related Details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceParam {

	private Double centerLatitude = 20.0111;
	private Double centerLongitude = 76.554;
	private Long radis = 12L;

}