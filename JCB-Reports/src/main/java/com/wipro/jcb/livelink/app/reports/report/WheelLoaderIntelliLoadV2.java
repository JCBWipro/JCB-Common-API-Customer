package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is to Handle Response Related to WheelLoaderIntelliLoadV2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelLoaderIntelliLoadV2 implements IntelliReport {

	private String reportName = "INTELLILOAD";

	private List<WeekelyBucketCount> weekelyBucketCount;

	private List<WeekelyDutyCycle> weekelyDutyCycle;

	private String message;

}
