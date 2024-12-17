package com.wipro.jcb.livelink.app.reports.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to AdvanceReportsV2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceReportsV2 {

	private String vin;

	private String dateRange;

	private VisualizationReport visualizationReport;

	private IntelliReport intelliReport;

	private IntelliReport intelliDigReport;

}
