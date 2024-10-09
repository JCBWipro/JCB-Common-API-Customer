package com.wipro.jcb.livelink.app.machines.service.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This MachineLocationHistory is to handle response for history of Machine Location
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineLocationHistory {

	private String vin;
	private List<LocationHistory> locationHistory;

}
