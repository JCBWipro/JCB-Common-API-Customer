package com.wipro.jcb.livelink.app.machines.service.reports;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This MachineProfileRequest is to Handle MachineProfile related Data
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class MachineUtilizationResponse extends UtilizationReport {

	private List<MachineUtilization> machineUtilization;

}
