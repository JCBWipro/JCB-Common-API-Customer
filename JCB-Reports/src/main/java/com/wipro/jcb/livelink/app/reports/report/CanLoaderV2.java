package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CanLoaderV2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CanLoaderV2 extends VisualizationReport {

	private String reportName = "CANWLS";

	private List<WheelLoaderGearUtilization> wlsGearUtilization;

	private List<FuelConsumptionResponse> wlsFuelConsumption;

	private String message;

}
