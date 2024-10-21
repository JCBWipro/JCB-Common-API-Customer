package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to WheelLoaderIntelliLoadV3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelLoaderIntelliLoadV3 implements IntelliReport {

	private String reportName = "INTELLILOAD";

	private List<WeekelyBucketCount> weekelyBucketCount;

	private List<WeekelyDutyCycle> weekelyDutyCycle;

	private String message;

	List<AdvanceReportChart> chartList;

}
