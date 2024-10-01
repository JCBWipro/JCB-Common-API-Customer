package com.wipro.jcb.livelink.app.machines.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineFuelConsumptionDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.service.MachineProfileService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.service.response.GeofenceParam;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;
import com.wipro.jcb.livelink.app.machines.service.response.TimefenceParam;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

/**
 * This MachineProfileServiceImpl is to handle MachineProfile related data
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class MachineProfileServiceImpl implements MachineProfileService {

	@Autowired
	Utilities utilities;

	@Autowired
	AppServerTokenServiceImpl appServerTokenServiceImpl;

	@Autowired
	MachineRepository machineRepository;

	@Autowired
	ServletContext servletContext;

	@Value("file:")
	private Resource res;

	@Autowired
	ServletContext context;

	@Value("${custom.formatter.timezone}")
	private String timezone;

	@Autowired
	MachineFuelConsumptionDataRepository machineFuelConsumptionDataRepository;

	@Autowired
	MachineResponseService machineResponseService;


	@JsonIgnore
	private DateFormat df = new SimpleDateFormat(AppServerConstants.DateFormatForMachineUpdate);

	/**
	 * Retrieves MachineProfile Details by userName and vin
	 * 
	 * @param userName
	 * @param vin
	 */
	@Override
	public MachineProfile getMachineProfile(String userName, String vin) throws ProcessCustomError {
		final Machine machine = machineRepository.findByVinAndUserName(vin, userName);
		String machineType = machineFuelConsumptionDataRepository.getMachineTypeByVin(vin);
		SimpleDateFormat renewalDate = new SimpleDateFormat("dd/MM/yyyy");
		log.debug(" Processing getMachineProfile request for vin " + vin + "MachineType" + machineType);
		if (machine != null) {
			try {
				TimefenceParam timefenceParam = new TimefenceParam(machine.getStartTime(), machine.getEndTime());
				GeofenceParam geofenceParam = new GeofenceParam(machine.getCenterLat(), machine.getCenterLong(),
						machine.getRadius());

				CustomerInfo customerInfo = new CustomerInfo(
						machine.getCustomerName() != null ? machine.getCustomerName() : "-",
						machine.getCustomerNumber() != null ? machine.getCustomerNumber() : "-",
						machine.getCustomerAddress() != null ? machine.getCustomerAddress() : "-");

				MachineProfile machineProfile = new MachineProfile(vin, machine.getModel(), machine.getPlatform(),
						machine.getTag(), machine.getDealerName() != null ? machine.getDealerName() : "-",
						machine.getDealerNumber() != null ? machine.getDealerNumber() : "-", "", "", "", null, null,
						false, machine.getImage(), machine.getThumbnail(), machine.getFirmwareVersion(),
						machine.getImeiNumber(), machine.getImsiNumber(), machine.getTransitMode(), machine.getSite(),
						geofenceParam, timefenceParam, customerInfo,
						machine.getRenewalDate() == null ? "" : renewalDate.format(machine.getRenewalDate()));
				machineType = (null != machineType) ? machineType : "-";
				machineProfile.setMachineType(machineType);
				machineProfile.setFirmwareType(machineResponseService.getMachinetype(vin));
				return machineProfile;
			} catch (final Exception ex) {
				log.error("getMachineProfile processing failed for vin " + vin + "with ");
				throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			log.error("getMachineProfile: Machine not found with vin " + vin);
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED);
		}
	}

}