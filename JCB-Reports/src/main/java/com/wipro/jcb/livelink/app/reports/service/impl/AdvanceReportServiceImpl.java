package com.wipro.jcb.livelink.app.reports.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.report.IntelliReportResponse;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReportResponse;
import com.wipro.jcb.livelink.app.reports.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.reports.service.MachineService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdvanceReportServiceImpl implements AdvanceReportService {
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
	@Autowired
	private ReportUtilities utilities;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private MachineService machineService;

	@Override
	public VisualizationReportResponse getVisualizationReportV2(String vin, String startDate, String endDate)
			throws ProcessCustomError {
		final Machine machine = machineRepository.findByVin(vin);
		if(machine != null) {
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			
			Date startRangeDate=null;
			Date endRangeDate =null;
			
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			
			Date fromDate =utilities.getDate(startDate);Date toDate=utilities.getDate(endDate);
			log.info("Date Range "+standardDate+"-"+fromDate+"-"+toDate);
			if(fromDate.before(standardDate) || toDate.before(standardDate)) {
				startRangeDate = utilities.getDate(utilities.getStartDate(90));
				endRangeDate = utilities.getDate(utilities.getStartDate(1));
			}else {
				startRangeDate = utilities.getDate(startDate);
				endRangeDate = utilities.getDate(endDate);
			}
			log.info("Date Range "+standardDate+"-"+startDate+"-"+endDate);
			return new VisualizationReportResponse(machine.getVin(),
						format.format(startRangeDate) + " - " + format.format(endRangeDate),
						machineService.getReportInstanceV2(machine.getVin(), startRangeDate, endRangeDate));
		}
		else {
			throw new ProcessCustomError("No such machine exist.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public VisualizationReportResponse getVisualizationReportV3(String vin, String startDate, String endDate)
			throws ProcessCustomError {
		final Machine machine = machineRepository.findByVin(vin);
		if (machine != null) {
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));

			Date startRangeDate = null;
			Date endRangeDate = null;

			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate() - 91);

			Date fromDate = utilities.getDate(startDate);
			Date toDate = utilities.getDate(endDate);
			log.info("Date Range " + standardDate + "-" + fromDate + "-" + toDate);
			if (fromDate.before(standardDate) || toDate.before(standardDate)) {
				startRangeDate = utilities.getDate(utilities.getStartDate(90));
				endRangeDate = utilities.getDate(utilities.getStartDate(1));
			} else {
				startRangeDate = utilities.getDate(startDate);
				endRangeDate = utilities.getDate(endDate);
			}
			log.info("Date Range " + standardDate + "-" + startDate + "-" + endDate);
			return new VisualizationReportResponse(machine.getVin(),
					format.format(startRangeDate) + " - " + format.format(endRangeDate),
					machineService.getReportInstanceV3(machine.getVin(), startRangeDate, endRangeDate));
		} else {
			throw new ProcessCustomError("No such machine exist.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public IntelliReportResponse getIntelliReportV3(String vin, String startDate, String endDate) throws ProcessCustomError{
		final Machine machine = machineRepository.findByVin(vin);
		if(machine != null) {
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			
			Date startRangeDate=null;
			Date endRangeDate =null;
			
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			
			Date fromDate =utilities.getDate(startDate);Date toDate=utilities.getDate(endDate);
			log.info("Date Range "+standardDate+"-"+fromDate+"-"+toDate);
			if(fromDate.before(standardDate) || toDate.before(standardDate)) {
				startRangeDate = utilities.getDate(utilities.getStartDate(90));
				endRangeDate = utilities.getDate(utilities.getStartDate(1));
			}else {
				startRangeDate = utilities.getDate(startDate);
				endRangeDate = utilities.getDate(endDate);
			}
			log.info("Date Range "+standardDate+"-"+startRangeDate+"-"+endRangeDate);
			
			return new IntelliReportResponse(machine.getVin(),
						format.format(startRangeDate) + " - " + format.format(endRangeDate),
						machineService.getIntelliReportV3(machine.getVin(), startRangeDate, endRangeDate));
		}
		else {
			throw new ProcessCustomError("No such machine exist.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
