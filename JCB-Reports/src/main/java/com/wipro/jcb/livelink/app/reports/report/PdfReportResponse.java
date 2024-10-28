package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import com.wipro.jcb.livelink.app.reports.entity.Alert;
import com.wipro.jcb.livelink.app.reports.entity.MachineFuelHistory;
import com.wipro.jcb.livelink.app.reports.response.DailyAverageUtilizationData;
import com.wipro.jcb.livelink.app.reports.response.EngineHistoryDataListV2;
import com.wipro.jcb.livelink.app.reports.response.FuelHistoryDataListV2;
import com.wipro.jcb.livelink.app.reports.response.MachineDetails;
import com.wipro.jcb.livelink.app.reports.response.MachineEngineStatus;
import com.wipro.jcb.livelink.app.reports.response.MachinePowerBand;
import com.wipro.jcb.livelink.app.reports.response.MachineWorkingIdleStatus;
import com.wipro.jcb.livelink.app.reports.response.UtilizationDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PdfReportResponse extends PdfReportData {

	private MachineDetails machineDetails;

	private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
	private List<EngineHistoryDataListV2> engineHistoryDayDataList;

	private MachinePowerBand machinePowerBand;
	List<AdvanceReportChart> chartList;

	private List<DailyAverageUtilizationData> dayUtilization;
	private MachineEngineStatus machineEngineStatus;
	private MachineWorkingIdleStatus machineWorkingIdleStatus;
	private MachineFuelHistory machineFuelStatus;
	private List<Alert> machineAlertDetails;
	private UtilizationDetails utilizationDetails;

	private String fromDate;
	private String toDate;
	private String todayDate;

	BHLReport bhlReport = new BHLReport();
	WLSReport wLSReport = new WLSReport();
	ExcavatorReport excavatorReport = new ExcavatorReport();
	CompactorReport compactorReport = new CompactorReport();
	TelehandlerReport telehandlerReport = new TelehandlerReport();

}
