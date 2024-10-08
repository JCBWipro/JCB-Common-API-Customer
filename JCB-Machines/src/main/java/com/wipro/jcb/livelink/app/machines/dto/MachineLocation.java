package com.wipro.jcb.livelink.app.machines.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This MachineLocation is to Handle Response related to vin, MachineLocationDetail and nextPage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineLocation {

	private String vin;
	private List<MachineLocationDetail> data;
	private Boolean nextPage;

}
