package com.wipro.jcb.livelink.app.machines.service.reports;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle MachineFuelUtilization Response
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class MachineFuelUtilizationResponse extends UtilizationReport{

	private List<FuelConsumption> fuelUtilization;

}
