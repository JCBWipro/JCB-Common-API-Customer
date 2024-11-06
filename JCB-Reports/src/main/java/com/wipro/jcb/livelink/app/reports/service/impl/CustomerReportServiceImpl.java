package com.wipro.jcb.livelink.app.reports.service.impl;

import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.constants.FuelLevelNAConfig;
import com.wipro.jcb.livelink.app.reports.constants.MessagesConstantsList;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFuelConsumptionData;
import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.repo.MachineFuelConsumptionDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachinePerformanceDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineUtilizationDataRepository;
import com.wipro.jcb.livelink.app.reports.report.AggregatedMachinePerformance;
import com.wipro.jcb.livelink.app.reports.report.AggregatedMachineUtilization;
import com.wipro.jcb.livelink.app.reports.report.ReportResponseV2;
import com.wipro.jcb.livelink.app.reports.service.CustomerReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@PropertySource("application.properties")
public class CustomerReportServiceImpl implements CustomerReportService {
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
	@Value("${livelinkserver.loadHistoricalDataForDays}")
	private int loadHistoricalDataForDays;
	
	@Autowired
    ReportUtilities utilities;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private MachineUtilizationDataRepository machineUtilizationDataRepository;
	
	@Autowired
	private MachinePerformanceDataRepository machinePerformanceDataRepository;
	
	@Autowired
	private MachineFuelConsumptionDataRepository machineFuelConsumptionDataRepository;

	@Override
	public ReportResponseV2 getCustomerReportV2(String userName, String filter, String startDate, String endDate) throws ProcessCustomError {
		final ReportResponseV2 reportResponse = new ReportResponseV2();
		try {
			Date historyStartDate = null;
			Date historyEndDate = null;
			List<String> vinList = new ArrayList<>();
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			if (("optional".equals(startDate) || "optional".equals(endDate))) {

				reportResponse.setDateRange(
						format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays))) + " - "
								+ format.format(utilities.getDate(utilities.getEndDate(-1))));
				historyStartDate = utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays));
				historyEndDate = utilities.getDate(utilities.getEndDate(-1));
			} else {

				reportResponse.setDateRange(format.format(utilities.getDate(startDate)) + " - "
						+ format.format(utilities.getDate(endDate)));
				historyStartDate = utilities.getDate(startDate);
				historyEndDate = utilities.getDate(endDate);
			}

			if (!"optional".equals(filter)) {
				if (filter.contains(",")) {
					final String[] vins = filter.split(",");
					vinList = Arrays.asList(vins);
				} else {
					vinList.add(filter);
				}
				reportResponse.setTotalMachineCount(Long.valueOf(vinList.size()));
				reportResponse.setTotalMachineHours(
						machineRepository.getTotalMachineHoursForListedMachines(userName, vinList));
			}

			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate() - 91);

			Machine machine = machineRepository.findByVin(filter);
			reportResponse.setTotalMachineCount(1L);
			reportResponse.setTotalMachineHours(machine.getTotalMachineHours());

			if (machine.getPremiumFlag() != null && machine.getPremiumFlag().equalsIgnoreCase("Premium")) {
				reportResponse.setDateRange(format.format(historyStartDate) + " - " + format.format(historyEndDate));
				log.info("Date range for Premium " + historyStartDate + "-" + historyEndDate);
				getMachineReportsV2(reportResponse, vinList, historyStartDate, historyEndDate, false);
			} else if (machine.getPremiumFlag() != null && machine.getPremiumFlag().equalsIgnoreCase("Standard")) {
				if (historyStartDate.after(standardDate) && historyEndDate.after(standardDate)) {
					log.info("Date range for Standard " + historyStartDate + "-" + historyEndDate);
					reportResponse
							.setDateRange(format.format(historyStartDate) + " - " + format.format(historyEndDate));
					getMachineReportsV2(reportResponse, vinList, historyStartDate, historyEndDate, false);
				} else {
					reportResponse.setMessage(
							"The selected machine has standard machine,so please select date below 90 days");
				}
			} else {
				log.info("Date range for LLPlus " + historyStartDate + "-" + historyEndDate);
				historyStartDate = utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays));
				historyEndDate = utilities.getDate(utilities.getEndDate(-1));
				reportResponse.setDateRange(format.format(historyStartDate) + " - " + format.format(historyEndDate));
				getMachineReportsV2(reportResponse, vinList, historyStartDate, historyEndDate, false);
			}
		} catch (final ProcessCustomError ex) {
			throw ex;
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("Failed to process request with message " + ex.getMessage());
			throw new ProcessCustomError(MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return reportResponse;
	}
	
	
	private void getMachineReportsV2(final ReportResponseV2 reportResponse, List<String> vinList, Date historyStartDate,
			Date historyEndDate, boolean defaultDates) throws ProcessCustomError {

		if (null != vinList && !vinList.isEmpty()) {

			final Date endDate = utilities.getDate(utilities.getStartDate(1));

			final long daysDifference = TimeUnit.DAYS.convert(
					Math.abs((defaultDates ? endDate : historyEndDate).getTime() - historyStartDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;

			reportResponse.setUtilization(machineUtilizationDataRepository
					.getByVinListAndDayBetweenOrderByDayAsc(vinList, historyStartDate, historyEndDate));

			List<AggregatedMachineUtilization> machineUtilizationData = new LinkedList<>(
					reportResponse.getUtilization());
			if (daysDifference > machineUtilizationData.size()) {

				List<Date> dateList = utilities.getDateMap(historyStartDate, defaultDates ? endDate : historyEndDate);

				for (AggregatedMachineUtilization aggregatedMachineUtilization : machineUtilizationData) {
					dateList.remove(aggregatedMachineUtilization.getDay());
				}

				for (Date day : dateList) {
					machineUtilizationData.add(new AggregatedMachineUtilization(day, null, null, null));
				}

				Collections.sort(machineUtilizationData, new Comparator<AggregatedMachineUtilization>() {

					@Override
					public int compare(AggregatedMachineUtilization o1, AggregatedMachineUtilization o2) {
						return o1.getDay().compareTo(o2.getDay());
					}
				});

			}

			reportResponse.setUpdatedUtilization(machineUtilizationData);
			reportResponse.setPerformance(machinePerformanceDataRepository
					.getByVinListAndDayBetweenOrderByDayAsc(vinList, historyStartDate, historyEndDate));

			List<AggregatedMachinePerformance> machinePerformanceData = new LinkedList<>(
					reportResponse.getPerformance());

			if (daysDifference > machinePerformanceData.size()) {
				List<Date> dateList = utilities.getDateMap(historyStartDate, defaultDates ? endDate : historyEndDate);

				for (AggregatedMachinePerformance aggregatedMachinePerformace : machinePerformanceData) {
					dateList.remove(aggregatedMachinePerformace.getDay());
				}

				for (Date day : dateList) {
					machinePerformanceData.add(new AggregatedMachinePerformance(day, null, null, null));
				}

				Collections.sort(machinePerformanceData, new Comparator<AggregatedMachinePerformance>() {

					@Override
					public int compare(AggregatedMachinePerformance o1, AggregatedMachinePerformance o2) {
						return o1.getDay().compareTo(o2.getDay());
					}
				});

			}

			reportResponse.setUpdatedPerformance(machinePerformanceData);

			if (vinList.size() == 1) {

				Machine machine = machineRepository.findByVin(vinList.get(0));

				if (!((!FuelLevelNAConfig.getExceptionMachines().contains(machine.getVin()))
						&& (FuelLevelNAConfig.getFuellevelnaconfig().containsKey(machine.getPlatform())
								&& FuelLevelNAConfig.getFuellevelnaconfig().get(machine.getPlatform())
										.contains(machine.getVin().substring(3, 8))))) {

					reportResponse.setFuel(machineFuelConsumptionDataRepository
							.getByVinListAndDayBetweenOrderByDayAsc(vinList, historyStartDate, historyEndDate));

					List<MachineFuelConsumptionData> machineFuelData = new LinkedList<>(reportResponse.getFuel());
					List<MachineFuelConsumptionData> removeMachineFuelNA = new LinkedList<>();

					if (daysDifference > machineFuelData.size()) {
						List<Date> dateList = utilities.getDateMap(historyStartDate,
								defaultDates ? endDate : historyEndDate);
						for (MachineFuelConsumptionData machineFuelConsumptionData : machineFuelData) {
							dateList.remove(machineFuelConsumptionData.getDay());
							if (!StringUtils.isEmpty(machineFuelConsumptionData.getFuelLevel())
									&& machineFuelConsumptionData.getFuelLevel().equals("NA"))
								removeMachineFuelNA.add(machineFuelConsumptionData);
						}
						if (!removeMachineFuelNA.isEmpty())
							machineFuelData.removeAll(removeMachineFuelNA);
						for (Date day : dateList) {
							machineFuelData.add(new MachineFuelConsumptionData(day, null, null));
						}

						Collections.sort(machineFuelData, new Comparator<MachineFuelConsumptionData>() {

							@Override
							public int compare(MachineFuelConsumptionData o1, MachineFuelConsumptionData o2) {
								return o1.getDay().compareTo(o2.getDay());
							}
						});

					} else {
						for (MachineFuelConsumptionData machineFuelConsumptionData : machineFuelData) {
							if (!StringUtils.isEmpty(machineFuelConsumptionData.getFuelLevel())
									&& machineFuelConsumptionData.getFuelLevel().equals("NA"))
								removeMachineFuelNA.add(machineFuelConsumptionData);
						}
						if (!removeMachineFuelNA.isEmpty())
							machineFuelData.removeAll(removeMachineFuelNA);
					}
					reportResponse.setUpdatedFuel(machineFuelData);
					return;
				}
			}
			reportResponse.setFuel(new ArrayList<MachineFuelConsumptionData>());
			reportResponse.setUpdatedFuel(new ArrayList<MachineFuelConsumptionData>());
		} else {
			log.error("Vin list of report is empty ");
			throw new ProcessCustomError(MessagesConstantsList.VIN_LIST_IS_EMPTY, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
