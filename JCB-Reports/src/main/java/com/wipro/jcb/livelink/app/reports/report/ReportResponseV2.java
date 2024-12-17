package com.wipro.jcb.livelink.app.reports.report;

import java.util.LinkedList;
import java.util.List;

import com.wipro.jcb.livelink.app.reports.entity.MachineFuelConsumptionData;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to ReportResponseV2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseV2 {

	@Schema(description = "Total machine count", required = true)
	private Long totalMachineCount;
	@Schema(description = "Total machine hours", example = "280Hrs", required = true)
	private Double totalMachineHours;
	@Schema(description = "Requested time range", example = "26 June 17 - 2 july 17", required = true)
	private String dateRange;
	@Schema(description = "List of machine utilization of provided machine", required = true)
	private List<AggregatedMachineUtilization> utilization = new LinkedList<>();
	@Schema(description = "List of machine fuel consumption of provided machine", required = true)
	private List<MachineFuelConsumptionData> fuel = new LinkedList<>();
	@Schema(description = "List of machine performance of provided machine", required = true)
	private List<AggregatedMachinePerformance> performance = new LinkedList<>();

	@Schema(description = "List of machine utilization of provided machine", required = true)
	private List<AggregatedMachineUtilization> updatedUtilization = new LinkedList<>();
	@Schema(description = "List of machine fuel consumption of provided machine", required = true)
	private List<MachineFuelConsumptionData> updatedFuel = new LinkedList<>();
	@Schema(description = "List of machine performance of provided machine", required = true)
	private List<AggregatedMachinePerformance> updatedPerformance = new LinkedList<>();

	private String message;

}

