package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CanCompactorV2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanCompactorV2 implements IntelliReport {

	private String reportName = "INTELLICOMPACTOR";

	CompactionDutyCycleData compactionDutyCycleData;

	List<CompactionWeeklyDutyCycleData> compactionWeeklyDutyCycleData;

	List<CompactionVibrationOnOffData> compactionVibrationOnOffData;

	private String message;

}