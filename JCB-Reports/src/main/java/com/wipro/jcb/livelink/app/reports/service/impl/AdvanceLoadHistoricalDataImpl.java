package com.wipro.jcb.livelink.app.reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.reports.entity.PremiumRequest;
import com.wipro.jcb.livelink.app.reports.repo.MachineChampionsDetailsRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineFeedParserDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.repo.PremiumRequestRepo;
import com.wipro.jcb.livelink.app.reports.service.AdvanceLoadHistoricalDataService;
import com.wipro.jcb.livelink.app.reports.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdvanceLoadHistoricalDataImpl implements AdvanceLoadHistoricalDataService {

	@Autowired
	private MachineRepository machineRepository;

	@Autowired
	private MachineFeedParserDataRepo machineFeedParserDataRepo;

	@Autowired
	private MachineChampionsDetailsRepository machineChampionsDetailsRepository;

	@Autowired
	private PremiumRequestRepo premiumRequestRepo;
	
	@Autowired
	private EmailService emailService;

	@Override
	public String joinpremium(String userName, String vin) {
		String joinPremiumResponse = "SUCCESS";
		try {
			Machine machine = machineRepository.findByVinAndUserName(vin, userName);
			log.info("Dealer Email " + machine.getDealerId());

			MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
			PremiumRequest premiumRequestMachine = premiumRequestRepo.findByVinAndUserId(vin, userName);
			if (machine != null && premiumRequestMachine == null) {

				String championsEmail = machineChampionsDetailsRepository.findByChampionsEmail(machine.getDealerId());
				if (championsEmail != null && !championsEmail.isEmpty() && !championsEmail.equals("-")
						&& !championsEmail.equals("NONE") && !championsEmail.equals("NA")) {
					log.info("Champions Email " + vin + "-" + userName + "-" + championsEmail);
					emailService.sendPremiumRequestEmail(vin, userName, machine, machineFeedParserData, championsEmail);
				} else {
					log.info("No Champions Email For " + vin + "-" + userName);
				}

				premiumRequestMachine = new PremiumRequest();
				premiumRequestMachine.setVin(vin);
				premiumRequestMachine.setUserId(userName);
				premiumRequestMachine.setCount(1);
				premiumRequestRepo.save(premiumRequestMachine);
				joinPremiumResponse = "SUCCESS-Your request sent to dealer successfully.";
			} else {
				log.info("Request has been already submitted " + vin + "-" + userName);
				premiumRequestMachine.setCount(premiumRequestMachine.getCount() + 1);
				joinPremiumResponse = "SUCCESS-Your request has been already submitted to the dealer. Support team will contact you soon.";
			}
			log.info("The request sent to dealer successfully." + vin);
		} catch (final Exception ex) {
			log.error("Error while setting geofence parameter", ex);
			return "Failed";
		}
		return joinPremiumResponse;
	}

}
