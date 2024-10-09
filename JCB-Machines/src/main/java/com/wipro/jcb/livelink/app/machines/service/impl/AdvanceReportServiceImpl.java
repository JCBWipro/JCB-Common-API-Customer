package com.wipro.jcb.livelink.app.machines.service.impl;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.reports.UtilizationReportResponse;
import com.wipro.jcb.livelink.app.machines.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;

@Service
public class AdvanceReportServiceImpl implements AdvanceReportService {
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
	@Autowired
	private Utilities utilities;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private MachineService machineService;
	
	@Override
	public UtilizationReportResponse getMachineUtilization(String vin, String startDate, String endDate)
			throws ProcessCustomError {
		final Machine machine = machineRepository.findByVin(vin);
		if (machine != null) {
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			return new UtilizationReportResponse(machine.getVin(),
					format.format(utilities.getDate(startDate)) + " - " + format.format(utilities.getDate(endDate)),
					machineService.getMachineUtilization(machine.getVin(), utilities.getDate(startDate),
							utilities.getDate(endDate)));
		} else {
			throw new ProcessCustomError("No such machine exist.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
