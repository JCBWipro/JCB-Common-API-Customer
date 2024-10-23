package com.wipro.jcb.livelink.app.reports.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.constants.MessagesConstantsList;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeatureInfo;
import com.wipro.jcb.livelink.app.reports.entity.MachineWheelLoaderData;
import com.wipro.jcb.livelink.app.reports.repo.MachineBHLDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineCompactionCoachDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineExcavatorRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineFeatureDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineTelehandlerDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineWheelLoaderDataRepository;
import com.wipro.jcb.livelink.app.reports.report.AdvanceReportChart;
import com.wipro.jcb.livelink.app.reports.report.AverageSpeedRoading;
import com.wipro.jcb.livelink.app.reports.report.CanBHLV2;
import com.wipro.jcb.livelink.app.reports.report.CanBHLV3;
import com.wipro.jcb.livelink.app.reports.report.CanCompactorV2;
import com.wipro.jcb.livelink.app.reports.report.CanCompactorV3;
import com.wipro.jcb.livelink.app.reports.report.CanExcavatorV2;
import com.wipro.jcb.livelink.app.reports.report.CanExcavatorV3;
import com.wipro.jcb.livelink.app.reports.report.CanLoaderV2;
import com.wipro.jcb.livelink.app.reports.report.CanLoaderV3;
import com.wipro.jcb.livelink.app.reports.report.CanTelehandler;
import com.wipro.jcb.livelink.app.reports.report.CompactionDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.CompactionVibrationOnOffData;
import com.wipro.jcb.livelink.app.reports.report.CompactionWeeklyDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.CompactorV1;
import com.wipro.jcb.livelink.app.reports.report.DistanceTraveledRoading;
import com.wipro.jcb.livelink.app.reports.report.DutyCycleBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavationModesBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorPowerModes;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorTravelAndSwingTime;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionDuty;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionExcavation;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse;
import com.wipro.jcb.livelink.app.reports.report.GearTimeSpentBHL;
import com.wipro.jcb.livelink.app.reports.report.GearUtilizationWLS;
import com.wipro.jcb.livelink.app.reports.report.HammerAbuseEventCount;
import com.wipro.jcb.livelink.app.reports.report.HammerUsedHours;
import com.wipro.jcb.livelink.app.reports.report.IntelliReport;
import com.wipro.jcb.livelink.app.reports.report.MachineCompassBHL;
import com.wipro.jcb.livelink.app.reports.report.MachinePowerBands;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReport;
import com.wipro.jcb.livelink.app.reports.report.WeekelyBucketCount;
import com.wipro.jcb.livelink.app.reports.report.WeekelyDutyCycle;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderGearUtilization;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderIntelliLoadV2;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderIntelliLoadV3;
import com.wipro.jcb.livelink.app.reports.response.StandardMachineBaseResponse;
import com.wipro.jcb.livelink.app.reports.response.StdMachineImagesResponse;
import com.wipro.jcb.livelink.app.reports.service.MachineFeatureInfoService;
import com.wipro.jcb.livelink.app.reports.service.MachineService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@PropertySource("application.properties")
public class MachineServiceImpl implements MachineService {
	
	@Value("${livelink.premium.image.machine-utilization}")
	private String machineUtilizationImg;

	@Value("${livelink.premium.image.fuel-utilization}")
	private String fuelUtilizationImg;

	@Value("${livelink.premium.image.machine-location}")
	private String machineLocationImg;

	@Value("${livelink.premium.image.machine-operating-system}")
	private String machineOSImg;

	@Value("${livelink.premium.image.machine-utilization-ios}")
	private String machineUtilizationImgPath;

	@Value("${livelink.premium.image.fuel-utilization-ios}")
	private String fuelUtilizationImgPath;

	@Value("${livelink.premium.image.machine-location-ios}")
	private String machineLocationImgPath;

	@Value("${livelink.premium.image.machine-operating-system-ios}")
	private String machineOSImgPath;
	
	@Value("${livelink.premium.image.machine-navigation}")
	private String machineNavigationImg;
	
	@Value("${livelink.premium.image.machine-fencing}")
	private String machineFencingImg;
	
	@Value("${livelink.premium.image.machine-navigation-ios}")
	private String machineNavigationImgIos;
	
	@Value("${livelink.premium.image.machine-fencing-ios}")
	private String machineFencingImgIos;
	
	@Autowired
    ReportUtilities utilities;
	
	@Autowired
    MachineRepository machineRepository;
	
	@Autowired
	private MachineFeatureDataRepo machineFeatureDataRepo;
	
	@Autowired
	private MachineFeatureInfoService machineFeatureService;
	
	@Autowired
    MachineBHLDataRepo machineBHLDataRepo;
	
	@Autowired
	private MachineWheelLoaderDataRepository machineWheelLoaderDataRepository;
	
	@Autowired
	private MachineExcavatorRepo machineExcavatorRepo;
    
    @Autowired
	private MachineCompactionCoachDataRepository machineCompactionCoachDataRepository;
    
    @Autowired
    private MachineTelehandlerDataRepository machineTelehandlerDataRepository;
    
	@Override
	public VisualizationReport getReportInstanceV2(String vin, Date startDate, Date endDate) {
 
		if (machineFeatureService.isExist(vin)) {
			List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlag(vin, true);
			for (MachineFeatureInfo machineFeatureInfo : list) {
				switch (machineFeatureInfo.getType()) {
				case "CANBHL":
					return loadBHLMachineReportReportV2(vin, startDate, endDate);
				case "CANWLS":
					return loadWheelLoadersReportV2(vin, startDate, endDate);
				case "CANEXCAVATOR":
					return loadExcavatorsReportV2(vin, startDate, endDate);
				}
			}
		}

		return null;
	}
	
	private VisualizationReport loadBHLMachineReportReportV2(String vin, Date startDate, Date endDate) {
		final CanBHLV2 canBHL = new CanBHLV2();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium") || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			canBHL.setDutyCycleBHLList(machineBHLDataRepo.getDutyCycleData(vin, startDate, endDate));
			canBHL.setExcavationModesList(machineBHLDataRepo.getExcavationModesData(vin, startDate, endDate));
			canBHL.setGearTimeSpentBHLList(machineBHLDataRepo.getGearTimeSpentData(vin, startDate, endDate));
			canBHL.setMachineCompassBHLList(machineBHLDataRepo.getMachineCompassBHLData(vin, startDate, endDate));
			List<FuelConsumptionBHL> machineFuelConsumptionData = machineBHLDataRepo.getFuelConsumptionData(vin,
					startDate, endDate);
			List<FuelConsumptionBHL> machineAverageFuelConsumption = machineBHLDataRepo.getAverageFuelConsumption(vin,
					startDate, endDate);
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canBHL.getDutyCycleBHLList().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (DutyCycleBHL dutyCycleBhl : canBHL.getDutyCycleBHLList()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getDutyCycleBHLList().add(new DutyCycleBHL(day, 0.0, 0.0, 0.0, 0.0, 0.0));
					canBHL.getExcavationModesList().add(new ExcavationModesBHL(day, 0.0, 0.0, 0.0));
					canBHL.getGearTimeSpentBHLList().add(new GearTimeSpentBHL(day, 0.0, 0.0, 0.0, 0.0));
					canBHL.getMachineCompassBHLList().add(new MachineCompassBHL(day, 0.0, 0.0, 0.0));
				}
				Collections.sort(canBHL.getDutyCycleBHLList(), new Comparator<DutyCycleBHL>() {
					@Override
					public int compare(DutyCycleBHL d1, DutyCycleBHL d2) {
						return d1.getDay().compareTo(d2.getDay());
					}
				});
				Collections.sort(canBHL.getExcavationModesList(), new Comparator<ExcavationModesBHL>() {
					@Override
					public int compare(ExcavationModesBHL em1, ExcavationModesBHL em2) {
						return em1.getDay().compareTo(em2.getDay());
					}
				});
				Collections.sort(canBHL.getGearTimeSpentBHLList(), new Comparator<GearTimeSpentBHL>() {
					@Override
					public int compare(GearTimeSpentBHL gt1, GearTimeSpentBHL gt2) {
						return gt1.getDay().compareTo(gt2.getDay());
					}
				});
				Collections.sort(canBHL.getMachineCompassBHLList(), new Comparator<MachineCompassBHL>() {
					@Override
					public int compare(MachineCompassBHL mc1, MachineCompassBHL mc2) {
						return mc1.getDay().compareTo(mc2.getDay());
					}
				});
			}
			List<FuelConsumptionResponse> bhlFuelConsumption = new ArrayList<>();
			List<FuelConsumptionResponse> averageFuelConsumption = new ArrayList<>();

			if (machineFuelConsumptionData != null && !machineFuelConsumptionData.isEmpty()) {
				for (FuelConsumptionBHL fuelData : machineFuelConsumptionData) {
					Date date = fuelData.getDay();
					bhlFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}
				if (daysDifference > bhlFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : bhlFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date day : list) {
						bhlFuelConsumption.add(new FuelConsumptionResponse(day, 0.0));
					}
					Collections.sort(bhlFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});

				}
			} else {
				log.info("Null Values");
			}

			if (machineAverageFuelConsumption != null && !machineAverageFuelConsumption.isEmpty()) {

				// Average Fuel Consumption
				for (FuelConsumptionBHL fuelData : machineAverageFuelConsumption) {
					Date date = fuelData.getDay();
					averageFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}

				if (daysDifference > averageFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : averageFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date day : list) {
						averageFuelConsumption.add(new FuelConsumptionResponse(day, 0.0));
					}
					Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});

				}
			}
			canBHL.setWlsFuelConsumption(bhlFuelConsumption);
			canBHL.setAverageFuelConsumption(averageFuelConsumption);

		} else {
			canBHL.setDutyCycleBHLList(new ArrayList<DutyCycleBHL>());
			canBHL.setExcavationModesList(new ArrayList<ExcavationModesBHL>());
			canBHL.setGearTimeSpentBHLList(new ArrayList<GearTimeSpentBHL>());
			canBHL.setMachineCompassBHLList(new ArrayList<MachineCompassBHL>());
			canBHL.setWlsFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canBHL.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canBHL.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadBHLMachineReportReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("CANBHL API Duration :" + elapsedTime + "-" + vin);
		return canBHL;
	}
	
	private VisualizationReport loadWheelLoadersReportV2(String vin, Date startDate, Date endDate) {
		final CanLoaderV2 canLoader = new CanLoaderV2();
		long start = System.currentTimeMillis();
		List<WheelLoaderGearUtilization> wlsGearUtilization = new ArrayList<>();
		List<FuelConsumptionResponse> wlsFuelConsumption = new ArrayList<>();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			List<MachineWheelLoaderData> machineWheelLoaderData = machineWheelLoaderDataRepository
					.findByVinAndDayBetweenOrderByDayAsc(vin, startDate, endDate);
			for (MachineWheelLoaderData wheelLoader : machineWheelLoaderData) {
				Map<String, Double> forward = new HashMap<String, Double>();
				Map<String, Double> backward = new HashMap<String, Double>();
				forward.put("gear1", wheelLoader.getGear1FwdUtilization());
				forward.put("gear2", wheelLoader.getGear2FwdUtilization());
				forward.put("gear3", wheelLoader.getGear3FwdUtilization());
				forward.put("gear4", wheelLoader.getGear4FwdUtilization());
				backward.put("gear1", wheelLoader.getGear1BkwdUtilization());
				backward.put("gear2", wheelLoader.getGear2BkwdUtilization());
				backward.put("gear3", wheelLoader.getGear3BkwdUtilization());
				backward.put("gear4", wheelLoader.getGear4BkwdUtilization());
				Date date = wheelLoader.getDay();
				wlsGearUtilization.add(new WheelLoaderGearUtilization(date, forward, backward));
				wlsFuelConsumption.add(new FuelConsumptionResponse(date, wheelLoader.getTotalFuelUsedInLtrs()));
			}
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > wlsFuelConsumption.size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : wlsFuelConsumption) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					wlsFuelConsumption.add(new FuelConsumptionResponse(day, 0.0));
					Map<String, Double> forwardNew = new HashMap<String, Double>();
					forwardNew.put("gear1", 0.0);
					forwardNew.put("gear2", 0.0);
					forwardNew.put("gear3", 0.0);
					forwardNew.put("gear4", 0.0);
					wlsGearUtilization.add(new WheelLoaderGearUtilization(day, forwardNew, forwardNew));
				}
				Collections.sort(wlsFuelConsumption, new Comparator<FuelConsumptionResponse>() {
					@Override
					public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(wlsGearUtilization, new Comparator<WheelLoaderGearUtilization>() {
					@Override
					public int compare(WheelLoaderGearUtilization o1, WheelLoaderGearUtilization o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
			canLoader.setWlsFuelConsumption(wlsFuelConsumption);
			canLoader.setWlsGearUtilization(wlsGearUtilization);
			log.info("loadWheelLoadersReport: returning response for machine: " + vin);
			long end = System.currentTimeMillis();
			long elapsedTime = end - start;
			log.info("CANWLS API Duration :" + elapsedTime + "-" + vin);
		} else {
			canLoader.setWlsFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canLoader.setWlsGearUtilization(new ArrayList<WheelLoaderGearUtilization>());
			canLoader.setMessage("Join Livelink premium to access these reports data");
		}
		return canLoader;
	}
	
	private VisualizationReport loadExcavatorsReportV2(String vin, Date startDate, Date endDate) {
		final CanExcavatorV2 canExcavator = new CanExcavatorV2();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			List<FuelConsumptionResponse> excavatorFuelConsumption = new ArrayList<>();
			List<FuelConsumption> repoResponse = machineExcavatorRepo.getFuelConsumptionReport(vin, startDate, endDate);
			for(FuelConsumption fuelConsumption : repoResponse) {
				FuelConsumptionResponse fuelConsumptionResponse = new FuelConsumptionResponse(fuelConsumption.getdate(), fuelConsumption.gettotalFuelUsed(), fuelConsumption.getfuelAverage());
				excavatorFuelConsumption.add(fuelConsumptionResponse);
			}
			canExcavator.setExcavatorFuelConsumption(excavatorFuelConsumption);
			canExcavator.setPowerModes(machineExcavatorRepo.getPowerModes(vin, startDate, endDate));
			canExcavator.setTravelAndSwingTime(machineExcavatorRepo.getTravelAndSwingTime(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canExcavator.getExcavatorFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : canExcavator.getExcavatorFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					canExcavator.getExcavatorFuelConsumption().add(new FuelConsumptionResponse(day, 0.0));
					canExcavator.getPowerModes().add(new ExcavatorPowerModes(day, 0.0, 0.0, 0.0, 0.0));
					canExcavator.getTravelAndSwingTime().add(new ExcavatorTravelAndSwingTime(day, 0.0, 0.0, 0.0));
				}
				Collections.sort(canExcavator.getExcavatorFuelConsumption(), new Comparator<FuelConsumptionResponse>() {
					@Override
					public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(canExcavator.getPowerModes(), new Comparator<ExcavatorPowerModes>() {
					@Override
					public int compare(ExcavatorPowerModes o1, ExcavatorPowerModes o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(canExcavator.getTravelAndSwingTime(), new Comparator<ExcavatorTravelAndSwingTime>() {
					@Override
					public int compare(ExcavatorTravelAndSwingTime o1, ExcavatorTravelAndSwingTime o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
		} else {
			canExcavator.setExcavatorFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canExcavator.setPowerModes(new ArrayList<ExcavatorPowerModes>());
			canExcavator.setTravelAndSwingTime(new ArrayList<ExcavatorTravelAndSwingTime>());
			canExcavator.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadExcavatorsReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("CANEXCAVATOR API Duration :" + elapsedTime + "-" + vin);
		return canExcavator;
	}
	
	@Override
	public IntelliReport getIntelliReportV2(String vin, Date startDate, Date endDate) {
		if (machineFeatureService.isExist(vin)) {
			List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlag(vin, true);
			for (MachineFeatureInfo machineFeatureInfo : list) {
				switch (machineFeatureInfo.getType()) {
				case "INTELLILOAD":
					return loadWheelLoaderIntelliReportV2(vin, startDate, endDate);
				case "INTELLICOMPACTOR":
					return loadCompactorMachineReportV2(vin, startDate, endDate);
				}
			}
		}

		return null;
	}
	
	private IntelliReport loadWheelLoaderIntelliReportV2(String vin, Date startDate, Date endDate) {
		WheelLoaderIntelliLoadV2 wheelLoaderIntelliLoad = new WheelLoaderIntelliLoadV2();
		long start = System.currentTimeMillis();

		Machine machine = machineRepository.findByVin(vin);
		if(machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			log.info("DATES "+standardDate+"-"+startDate+"-"+endDate);
			if(standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			wheelLoaderIntelliLoad.setWeekelyBucketCount(
					machineWheelLoaderDataRepository.getBucketCountData(vin, startDate, endDate));
			wheelLoaderIntelliLoad
					.setWeekelyDutyCycle(machineWheelLoaderDataRepository.getDutyCycleData(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > wheelLoaderIntelliLoad.getWeekelyBucketCount().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (WeekelyBucketCount weekelyBucketCount : wheelLoaderIntelliLoad.getWeekelyBucketCount()) {
					list.remove(weekelyBucketCount.getDate());
				}
				for (Date day : list) {
					wheelLoaderIntelliLoad.getWeekelyBucketCount().add(new WeekelyBucketCount(day, null));
					wheelLoaderIntelliLoad.getWeekelyDutyCycle().add(new WeekelyDutyCycle(day, null));
				}
				Collections.sort(wheelLoaderIntelliLoad.getWeekelyBucketCount(), new Comparator<WeekelyBucketCount>() {
					@Override
					public int compare(WeekelyBucketCount o1, WeekelyBucketCount o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(wheelLoaderIntelliLoad.getWeekelyDutyCycle(), new Comparator<WeekelyDutyCycle>() {
					@Override
					public int compare(WeekelyDutyCycle o1, WeekelyDutyCycle o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
		} else {
			wheelLoaderIntelliLoad.setWeekelyBucketCount(new ArrayList<WeekelyBucketCount>());
			wheelLoaderIntelliLoad.setWeekelyDutyCycle(new ArrayList<WeekelyDutyCycle>());
			wheelLoaderIntelliLoad.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadWheelLoaderIntelliReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLILOAD API Duration :" + elapsedTime + "-" + vin);
		return wheelLoaderIntelliLoad;
	}
	
	private IntelliReport loadCompactorMachineReportV2(String vin, Date startDate, Date endDate) {
		final CanCompactorV2 canCompactor = new CanCompactorV2();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if(machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			if(standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium") || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			canCompactor
					.setCompactionDutyCycleData(machineCompactionCoachDataRepository.findByVinForLifeBetweenDay(vin));
			canCompactor.setCompactionWeeklyDutyCycleData(
					machineCompactionCoachDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, startDate, endDate));
			canCompactor.setCompactionVibrationOnOffData(
					machineCompactionCoachDataRepository.findVibrationData(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canCompactor.getCompactionWeeklyDutyCycleData().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (CompactionWeeklyDutyCycleData compactionWeeklyDutyCycleData : canCompactor
						.getCompactionWeeklyDutyCycleData()) {
					list.remove(compactionWeeklyDutyCycleData.getDate());
				}
				for (Date day : list) {
					canCompactor.getCompactionWeeklyDutyCycleData()
							.add(new CompactionWeeklyDutyCycleData(null, null, null, day));
					canCompactor.getCompactionVibrationOnOffData()
							.add(new CompactionVibrationOnOffData(day, null, null));
				}
				Collections.sort(canCompactor.getCompactionWeeklyDutyCycleData(),
						new Comparator<CompactionWeeklyDutyCycleData>() {

							@Override
							public int compare(CompactionWeeklyDutyCycleData o1, CompactionWeeklyDutyCycleData o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
				Collections.sort(canCompactor.getCompactionVibrationOnOffData(),
						new Comparator<CompactionVibrationOnOffData>() {
							@Override
							public int compare(CompactionVibrationOnOffData o1, CompactionVibrationOnOffData o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
		} else {
			canCompactor.setCompactionDutyCycleData(new CompactionDutyCycleData(null, null, null));
			canCompactor.setCompactionVibrationOnOffData(new ArrayList<CompactionVibrationOnOffData>());
			canCompactor.setCompactionWeeklyDutyCycleData(new ArrayList<CompactionWeeklyDutyCycleData>());
			canCompactor.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadCompactorMachineReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLICOMPACTOR API Duration :" + elapsedTime + "-" + vin);
		return canCompactor;
	}

	@Override
	public IntelliReport loadIntelliDigReport(String vin, Date startDate, Date endDate) {
		return null;
	}
	
	@Override
	public VisualizationReport getReportInstanceV3(String vin, Date startDate, Date endDate) {
 
		if (machineFeatureService.isExist(vin)) {
			List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlag(vin, true);
			for (MachineFeatureInfo machineFeatureInfo : list) {
				switch (machineFeatureInfo.getType()) {
				case "CANBHL":
					return loadBHLMachineReportReportV3(vin, startDate, endDate);
				case "CANWLS":
					return loadWheelLoadersReportV3(vin, startDate, endDate);
				case "CANEXCAVATOR":
					return loadExcavatorsReportV3(vin, startDate, endDate);
				case "CANCOMPACTOR":
					return loadCompactorReportV3(vin, startDate, endDate);
				case "CANTELEHANDLER":
					return loadTelehandlerReportV3(vin, startDate, endDate);
				}
			}
		}

		return null;
	}
	
	private VisualizationReport loadBHLMachineReportReportV3(String vin, Date startDate, Date endDate) {
		final CanBHLV3 canBHL = new CanBHLV3();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium") || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			canBHL.setDutyCycleBHLList(machineBHLDataRepo.getDutyCycleDataV3(vin, startDate, endDate));
			canBHL.setExcavationModesList(machineBHLDataRepo.getExcavationModesDataV3(vin, startDate, endDate));
			canBHL.setGearTimeSpentBHLList(machineBHLDataRepo.getGearTimeSpentDataV3(vin, startDate, endDate));
			canBHL.setMachineCompassBHLList(machineBHLDataRepo.getMachineCompassBHLDataV3(vin, startDate, endDate));
			canBHL.setFuelConsumptionDuty(machineBHLDataRepo.getFuelConsumptionDuty(vin, startDate, endDate));
			canBHL.setFuelConsumptionExcavation(machineBHLDataRepo.getFuelConsumptionExcavation(vin, startDate, endDate));
			canBHL.setDistanceRoading(machineBHLDataRepo.getDistanceTraveledRoading(vin, startDate, endDate));
			canBHL.setAverageRoading(machineBHLDataRepo.getAverageSpeedRoading(vin, startDate, endDate));
			List<FuelConsumptionBHL> machineFuelConsumptionData = machineBHLDataRepo.getFuelConsumptionDataV3(vin,startDate, endDate);
			List<FuelConsumptionBHL> machineAverageFuelConsumption = machineBHLDataRepo.getAverageFuelConsumptionV3(vin,startDate, endDate);
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canBHL.getDutyCycleBHLList().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (DutyCycleBHL dutyCycleBhl : canBHL.getDutyCycleBHLList()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getDutyCycleBHLList().add(new DutyCycleBHL(day, null, null, null, null, null));
					canBHL.getExcavationModesList().add(new ExcavationModesBHL(day, null, null, null));
					canBHL.getGearTimeSpentBHLList().add(new GearTimeSpentBHL(day, null, null, null, null));
					canBHL.getMachineCompassBHLList().add(new MachineCompassBHL(day, null, null, null));
				}
				
			}
			
			Collections.sort(canBHL.getDutyCycleBHLList(), new Comparator<DutyCycleBHL>() {
				@Override
				public int compare(DutyCycleBHL d1, DutyCycleBHL d2) {
					return d1.getDay().compareTo(d2.getDay());
				}
			});
			Collections.sort(canBHL.getExcavationModesList(), new Comparator<ExcavationModesBHL>() {
				@Override
				public int compare(ExcavationModesBHL em1, ExcavationModesBHL em2) {
					return em1.getDay().compareTo(em2.getDay());
				}
			});
			Collections.sort(canBHL.getGearTimeSpentBHLList(), new Comparator<GearTimeSpentBHL>() {
				@Override
				public int compare(GearTimeSpentBHL gt1, GearTimeSpentBHL gt2) {
					return gt1.getDay().compareTo(gt2.getDay());
				}
			});
			Collections.sort(canBHL.getMachineCompassBHLList(), new Comparator<MachineCompassBHL>() {
				@Override
				public int compare(MachineCompassBHL mc1, MachineCompassBHL mc2) {
					return mc1.getDay().compareTo(mc2.getDay());
				}
			});
			
			List<FuelConsumptionResponse> bhlFuelConsumption = new ArrayList<>();
			List<FuelConsumptionResponse> averageFuelConsumption = new ArrayList<>();

			if (machineFuelConsumptionData != null && !machineFuelConsumptionData.isEmpty()) {
				for (FuelConsumptionBHL fuelData : machineFuelConsumptionData) {
					Date date = fuelData.getDay();
					bhlFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}
				if (daysDifference > bhlFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : bhlFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date day : list) {
						bhlFuelConsumption.add(new FuelConsumptionResponse(day, null));
					}
					
				}
			} else {
				log.info("Null Values");
			}

			if (machineAverageFuelConsumption != null && !machineAverageFuelConsumption.isEmpty()) {

				// Average Fuel Consumption
				for (FuelConsumptionBHL fuelData : machineAverageFuelConsumption) {
					Date date = fuelData.getDay();
					averageFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}

				if (daysDifference > averageFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : averageFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date day : list) {
						averageFuelConsumption.add(new FuelConsumptionResponse(day, null));
					}
					Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});

				}
			}
			Collections.sort(bhlFuelConsumption, new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			canBHL.setWlsFuelConsumption(bhlFuelConsumption);
			canBHL.setAverageFuelConsumption(averageFuelConsumption);
			
			//Fuel Consumption Duty
			if (daysDifference > canBHL.getFuelConsumptionDuty().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionDuty dutyCycleBhl : canBHL.getFuelConsumptionDuty()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getFuelConsumptionDuty().add(new FuelConsumptionDuty(day, null, null, null, null, null, null, null, null));
					
				}
				
			}
			Collections.sort(canBHL.getFuelConsumptionDuty(), new Comparator<FuelConsumptionDuty>() {
				@Override
				public int compare(FuelConsumptionDuty d1, FuelConsumptionDuty d2) {
					return d1.getDay().compareTo(d2.getDay());
				}
			});
			if (daysDifference > canBHL.getFuelConsumptionExcavation().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionExcavation dutyCycleBhl : canBHL.getFuelConsumptionExcavation()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getFuelConsumptionExcavation().add(new FuelConsumptionExcavation(day, null, null, null, null, null, null));
					
				}
				}
			
			Collections.sort(canBHL.getFuelConsumptionExcavation(), new Comparator<FuelConsumptionExcavation>() {
				@Override
				public int compare(FuelConsumptionExcavation d1, FuelConsumptionExcavation d2) {
					return d1.getDay().compareTo(d2.getDay());
				}
			});
			
			// Distance Roading
			if (daysDifference > canBHL.getDistanceRoading().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (DistanceTraveledRoading dutyCycleBhl : canBHL.getDistanceRoading()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getDistanceRoading().add(new DistanceTraveledRoading(day, null));
					
				}
			}
			
			Collections.sort(canBHL.getDistanceRoading(), new Comparator<DistanceTraveledRoading>() {
				@Override
				public int compare(DistanceTraveledRoading d1, DistanceTraveledRoading d2) {
					return d1.getDay().compareTo(d2.getDay());
				}
			});
			//Average Roading
			if (daysDifference > canBHL.getAverageRoading().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (AverageSpeedRoading dutyCycleBhl : canBHL.getAverageRoading()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					canBHL.getAverageRoading().add(new AverageSpeedRoading(day, null));
					
				}
				
			}
			
			Collections.sort(canBHL.getAverageRoading(), new Comparator<AverageSpeedRoading>() {
				@Override
				public int compare(AverageSpeedRoading d1, AverageSpeedRoading d2) {
					return d1.getDay().compareTo(d2.getDay());
				}
			});
			
			long fuelConsumptionCount = canBHL.getWlsFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = canBHL.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long fuelConsumptionDutyCount = canBHL.getFuelConsumptionDuty().stream().filter(f->f.getExcavationMode()==null && 
					f.getExcavationModePercentage()==null && f.getIdleMode()==null && f.getIdleModePercentage()==null && f.getLoadingMode()==null && f.getLoadingModePercentage()==null && f.getRoadindMode()==null && f.getRoadindModePercentage()==null).count();
			long fuelConsumptionExcavationCount = canBHL.getFuelConsumptionExcavation().stream().filter(f->f.getEconomyMode()==null && f.getEconomyModePercentage()==null && f.getPlusMode()==null && f.getPlusModePercentage()==null && f.getStandardMode()==null && f.getStandardModePercentage()==null ).count();
			long distanceRoadingCount = canBHL.getDistanceRoading().stream().filter(e ->  e.getDistanceTraveledRoading()==null).count();
			long averageRoadingCount = canBHL.getAverageRoading().stream().filter(e ->  e.getAverageSpeedRoading()==null).count();
			long excavationModesCount = canBHL.getExcavationModesList().stream().filter(e ->  e.getActiveModeHrs()==null && e.getEconomyModeHrs()==null && e.getPowerModeHrs()==null).count();
			long dutyCycleCount = canBHL.getDutyCycleBHLList().stream().filter(e ->  e.getAttachment()==null && e.getExcavation()==null&& e.getIdling()==null && e.getLoading()==null && e.getRoading()==null).count();
			long machineCompassCount = canBHL.getMachineCompassBHLList().stream().filter(e ->  e.getForwardDirection()==null && e.getNeutralDirection()==null && e.getReverseDirection()==null).count();
			long gearTimeSpentCount = canBHL.getGearTimeSpentBHLList().stream().filter(e ->  e.getFirstGear()==null && e.getSecoundGear()==null && e.getThirdGear()==null && e.getForthGear()==null).count();
			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
			 
			if(canBHL.getWlsFuelConsumption()!=null && !canBHL.getWlsFuelConsumption().isEmpty() && fuelConsumptionCount!=canBHL.getWlsFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("WlsFuelConsumption"));
			}
			if(canBHL.getAverageFuelConsumption()!=null && !canBHL.getAverageFuelConsumption().isEmpty() && averageConsumptionCount!=canBHL.getAverageFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			
			if(canBHL.getFuelConsumptionDuty()!=null && !canBHL.getFuelConsumptionDuty().isEmpty() && fuelConsumptionDutyCount!=canBHL.getFuelConsumptionDuty().size()) {
				chartList.add(new AdvanceReportChart("FuelConsumptionDuty"));
			}
			
			if(canBHL.getFuelConsumptionExcavation()!=null && !canBHL.getFuelConsumptionExcavation().isEmpty() && fuelConsumptionExcavationCount!=canBHL.getFuelConsumptionExcavation().size()) {
				chartList.add(new AdvanceReportChart("FuelConsumptionExcavation"));
			}
			
			if(canBHL.getDistanceRoading()!=null && !canBHL.getDistanceRoading().isEmpty() && distanceRoadingCount!=canBHL.getDistanceRoading().size()) {
				chartList.add(new AdvanceReportChart("DistanceRoading"));
			}
			
			if(canBHL.getAverageRoading()!=null && !canBHL.getAverageRoading().isEmpty() && averageRoadingCount!=canBHL.getAverageRoading().size()) {
				chartList.add(new AdvanceReportChart("AverageRoading"));
			}
			if(canBHL.getExcavationModesList()!=null && !canBHL.getExcavationModesList().isEmpty() && excavationModesCount!=canBHL.getExcavationModesList().size()) {
				chartList.add(new AdvanceReportChart("ExcavationModesList"));
			}
		
			if(canBHL.getDutyCycleBHLList()!=null && !canBHL.getDutyCycleBHLList().isEmpty() && dutyCycleCount!=canBHL.getDutyCycleBHLList().size()) {
			chartList.add(new AdvanceReportChart("DutyCycleBHLList"));
			}
		  if(canBHL.getMachineCompassBHLList()!=null && !canBHL.getMachineCompassBHLList().isEmpty() && machineCompassCount!=canBHL.getMachineCompassBHLList().size()) {
				chartList.add(new AdvanceReportChart("MachineCompassBHLList"));
			}
			if(canBHL.getGearTimeSpentBHLList()!=null && !canBHL.getGearTimeSpentBHLList().isEmpty() && gearTimeSpentCount!=canBHL.getGearTimeSpentBHLList().size()) {
				chartList.add(new AdvanceReportChart("GearTimeSpentBHLList"));
			}
			
			canBHL.setChartList(chartList);
		} else {
			canBHL.setDutyCycleBHLList(new ArrayList<DutyCycleBHL>());
			canBHL.setExcavationModesList(new ArrayList<ExcavationModesBHL>());
			canBHL.setGearTimeSpentBHLList(new ArrayList<GearTimeSpentBHL>());
			canBHL.setMachineCompassBHLList(new ArrayList<MachineCompassBHL>());
			canBHL.setWlsFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canBHL.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canBHL.setFuelConsumptionDuty(new ArrayList<FuelConsumptionDuty>());
			canBHL.setFuelConsumptionExcavation(new ArrayList<FuelConsumptionExcavation>());
			canBHL.setDistanceRoading(new ArrayList<DistanceTraveledRoading>());
			canBHL.setAverageRoading(new ArrayList<AverageSpeedRoading>());
			canBHL.setMessage("Join Livelink premium to access these reports data");
			canBHL.setChartList(new ArrayList<AdvanceReportChart>());
		}
		
		log.info("loadBHLMachineReportReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("CANBHL API Duration :" + elapsedTime + "-" + vin);
		return canBHL;
	}
	
	private VisualizationReport loadWheelLoadersReportV3(String vin, Date startDate, Date endDate) {
		final CanLoaderV3 canLoader = new CanLoaderV3();
		try {
		long start = System.currentTimeMillis();
		List<WheelLoaderGearUtilization> wlsGearUtilization = new ArrayList<>();
		List<FuelConsumptionResponse> wlsFuelConsumption = new ArrayList<>();
		List<FuelConsumptionResponse> averageFuelConsumption = new ArrayList<>();
		List<MachinePowerBands> fuelPowerBand = new ArrayList<>();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			List<GearUtilizationWLS> machineWheelLoaderData = machineWheelLoaderDataRepository.getGearUtilizationDataV3(vin, startDate, endDate);
			for (GearUtilizationWLS wheelLoader : machineWheelLoaderData) {
				Map<String, Double> forward = new HashMap<String, Double>();
				Map<String, Double> backward = new HashMap<String, Double>();
				forward.put("gear1", wheelLoader.getGear1FwdUtilization());
				forward.put("gear2", wheelLoader.getGear2FwdUtilization());
				forward.put("gear3", wheelLoader.getGear3FwdUtilization());
				forward.put("gear4", wheelLoader.getGear4FwdUtilization());
				backward.put("gear1", wheelLoader.getGear1BkwdUtilization());
				backward.put("gear2", wheelLoader.getGear2BkwdUtilization());
				backward.put("gear3", wheelLoader.getGear3BkwdUtilization());
				backward.put("gear4", wheelLoader.getGear4BkwdUtilization());
				Date date = wheelLoader.getDay();
				wlsGearUtilization.add(new WheelLoaderGearUtilization(date, forward, backward));
				wlsFuelConsumption.add(new FuelConsumptionResponse(date, wheelLoader.getTotalFuelUsedInLtrs()));
				averageFuelConsumption.add(new FuelConsumptionResponse(date,wheelLoader.getAverageFuelConsumption()));
				fuelPowerBand.add(new MachinePowerBands(date, wheelLoader.getFuelUsedInLPBLtrs(), wheelLoader.getFuelUsedInMPBLtrs(), wheelLoader.getFuelUsedInHPBLtrs(), wheelLoader.getFuelLoss()));
			}
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),
					TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > wlsFuelConsumption.size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : wlsFuelConsumption) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					wlsFuelConsumption.add(new FuelConsumptionResponse(day, null));
					wlsGearUtilization.add(new WheelLoaderGearUtilization(day, null, null));
				}
				
			}
			
			Collections.sort(wlsFuelConsumption, new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			Collections.sort(wlsGearUtilization, new Comparator<WheelLoaderGearUtilization>() {
				@Override
				public int compare(WheelLoaderGearUtilization o1, WheelLoaderGearUtilization o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			if (daysDifference > averageFuelConsumption.size()) {

				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : averageFuelConsumption) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					averageFuelConsumption.add(new FuelConsumptionResponse(day, null));
				}
			}
			
			Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			if (daysDifference > fuelPowerBand.size()) {
				
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands powerBands : fuelPowerBand) {
					list.remove(powerBands.getDate());
				}
				for (Date day : list) {
					fuelPowerBand.add(new MachinePowerBands(day, null,null,null,null));
				}
			}
			
			Collections.sort(fuelPowerBand, new Comparator<MachinePowerBands>() {
				@Override
				public int compare(MachinePowerBands o1, MachinePowerBands o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			canLoader.setWlsFuelConsumption(wlsFuelConsumption);
			canLoader.setWlsGearUtilization(wlsGearUtilization);
			canLoader.setAverageFuelConsumption(averageFuelConsumption);
			canLoader.setFuelPowerBand(fuelPowerBand);
			
			long fuelConsumptionCount = canLoader.getWlsFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageFuelCount = canLoader.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long gearUtilizationCount = canLoader.getWlsGearUtilization().stream().filter(e ->  e.getForward()==null && e.getBackward()==null).count();
			long fuelPowerbandCount = canLoader.getFuelPowerBand().stream().filter(e ->  e.getHighPowerBand()==null && e.getIdlePowerBand()==null && e.getLowPowerBand()==null && e.getMediumPowerBand()==null).count();
			
			List<AdvanceReportChart>  chartList =new ArrayList<>();
			
			if(canLoader.getWlsFuelConsumption()!=null && !canLoader.getWlsFuelConsumption().isEmpty() && fuelConsumptionCount!=wlsFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("WlsFuelConsumption"));
			}
			if(canLoader.getAverageFuelConsumption()!=null && !canLoader.getAverageFuelConsumption().isEmpty() && averageFuelCount!=averageFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			if(canLoader.getWlsGearUtilization()!=null && !canLoader.getWlsGearUtilization().isEmpty() && gearUtilizationCount!=wlsGearUtilization.size()) {
				chartList.add(new AdvanceReportChart("WlsGearUtilization"));
			}
			if(canLoader.getFuelPowerBand()!=null && !canLoader.getFuelPowerBand().isEmpty() && fuelPowerbandCount!=fuelPowerBand.size()) {
				chartList.add(new AdvanceReportChart("FuelPowerBand"));
			}
			canLoader.setChartList(chartList);
			log.info("loadWheelLoadersReport: returning response for machine: " + vin);
			long end = System.currentTimeMillis();
			long elapsedTime = end - start;
			log.info("CANWLS API Duration :" + elapsedTime + "-" + vin);
		} else {
			canLoader.setWlsFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canLoader.setWlsGearUtilization(new ArrayList<WheelLoaderGearUtilization>());
			canLoader.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canLoader.setMessage("Join Livelink premium to access these reports data");
			canLoader.setChartList(new ArrayList<AdvanceReportChart>());
			canLoader.setFuelPowerBand(new ArrayList<MachinePowerBands>());
		}
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Exception occured for advancedreportV3 WLS API Param-"+vin+"Exception -"+e.getMessage());
		}
		return canLoader;
	}
	
	private VisualizationReport loadExcavatorsReportV3(String vin, Date startDate, Date endDate) {
		final CanExcavatorV3 canExcavator = new CanExcavatorV3();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			canExcavator.setExcavatorFuelConsumption(machineExcavatorRepo.getFuelConsumptionDataV3(vin, startDate, endDate));
			canExcavator.setPowerModes(machineExcavatorRepo.getPowerModesV3(vin, startDate, endDate));
			canExcavator.setTravelAndSwingTime(machineExcavatorRepo.getTravelAndSwingTimeV3(vin, startDate, endDate));
			canExcavator.setAverageFuelConsumption(machineExcavatorRepo.getAverageConsumptionData(vin, startDate, endDate));
			canExcavator.setHammerUsedHours(machineExcavatorRepo.getHammerUserHours(vin, startDate, endDate));
			canExcavator.setHammerAbuseEventCount(machineExcavatorRepo.getHammerAbuseEventCount(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canExcavator.getExcavatorFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : canExcavator.getExcavatorFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					canExcavator.getExcavatorFuelConsumption().add(new FuelConsumptionResponse(day, null));
					canExcavator.getPowerModes().add(new ExcavatorPowerModes(day, null, null, null, null));
					canExcavator.getTravelAndSwingTime().add(new ExcavatorTravelAndSwingTime(day, null, null, null));
					canExcavator.getHammerUsedHours().add(new HammerUsedHours(day, null));
					canExcavator.getHammerAbuseEventCount().add(new HammerAbuseEventCount(day,null));
				}
				
			}
			
			Collections.sort(canExcavator.getExcavatorFuelConsumption(), new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			Collections.sort(canExcavator.getPowerModes(), new Comparator<ExcavatorPowerModes>() {
				@Override
				public int compare(ExcavatorPowerModes o1, ExcavatorPowerModes o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			Collections.sort(canExcavator.getTravelAndSwingTime(), new Comparator<ExcavatorTravelAndSwingTime>() {
				@Override
				public int compare(ExcavatorTravelAndSwingTime o1, ExcavatorTravelAndSwingTime o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			Collections.sort(canExcavator.getHammerUsedHours(), new Comparator<HammerUsedHours>() {
				@Override
				public int compare(HammerUsedHours o1, HammerUsedHours o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			Collections.sort(canExcavator.getHammerAbuseEventCount(), new Comparator<HammerAbuseEventCount>() {
				@Override
				public int compare(HammerAbuseEventCount o1, HammerAbuseEventCount o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			if (daysDifference > canExcavator.getAverageFuelConsumption().size()) {

				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : canExcavator.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				
				for (Date day : list) {
					canExcavator.getAverageFuelConsumption().add(new FuelConsumptionResponse(day, null));
					
				}
				

			}
			
			Collections.sort(canExcavator.getAverageFuelConsumption(), new Comparator<FuelConsumptionResponse>() {
				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			
			List<AdvanceReportChart>  chartList =new ArrayList<>();
			long fuelConsumptionCount = canExcavator.getExcavatorFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = canExcavator.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long timeTravelCount = canExcavator.getTravelAndSwingTime().stream().filter(e ->  e.getSwingHrs()==null && e.getTotalHrs()==null && e.getTravelHrs()==null).count();
			long powermodeCount = canExcavator.getPowerModes().stream().filter(e ->  e.getGBand()==null && e.getHBand()==null && e.getHPlusBand()==null && e.getLBand()==null).count();
			long hammerusedhrs = canExcavator.getHammerUsedHours().stream().filter(e -> e.getHammerUsedTimeHrs()==null).count();
			long hammerabuseCount = canExcavator.getHammerAbuseEventCount().stream().filter(e -> e.getHammerAbuseCount() ==null).count();
			
			if(canExcavator.getExcavatorFuelConsumption()!=null && !canExcavator.getExcavatorFuelConsumption().isEmpty() && fuelConsumptionCount!=canExcavator.getExcavatorFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("ExcavatorFuelConsumption"));
			}
			if(canExcavator.getAverageFuelConsumption()!=null && !canExcavator.getAverageFuelConsumption().isEmpty() && averageConsumptionCount!=canExcavator.getAverageFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			if(canExcavator.getTravelAndSwingTime()!=null && !canExcavator.getTravelAndSwingTime().isEmpty() && timeTravelCount!=canExcavator.getTravelAndSwingTime().size()) {
				chartList.add(new AdvanceReportChart("TravelAndSwingTime"));
			}
			if(canExcavator.getPowerModes()!=null && !canExcavator.getPowerModes().isEmpty() && powermodeCount!=canExcavator.getPowerModes().size()) {
				chartList.add(new AdvanceReportChart("PowerModes"));
			}
			if(canExcavator.getHammerUsedHours()!=null  && !canExcavator.getHammerUsedHours().isEmpty() && hammerusedhrs != canExcavator.getHammerUsedHours().size())
				chartList.add(new AdvanceReportChart("HammerUsedHours"));
			if(canExcavator.getHammerAbuseEventCount()!=null && !canExcavator.getHammerAbuseEventCount().isEmpty() && hammerabuseCount!=canExcavator.getHammerAbuseEventCount().size())
				chartList.add(new AdvanceReportChart("HammerAbuseEventCount"));
			
			canExcavator.setChartList(chartList);

		} else {
			canExcavator.setExcavatorFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canExcavator.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			canExcavator.setPowerModes(new ArrayList<ExcavatorPowerModes>());
			canExcavator.setTravelAndSwingTime(new ArrayList<ExcavatorTravelAndSwingTime>());
			canExcavator.setChartList(new ArrayList<AdvanceReportChart>());
			canExcavator.setHammerUsedHours(new ArrayList<HammerUsedHours>());
			canExcavator.setHammerAbuseEventCount(new ArrayList<HammerAbuseEventCount>());
			canExcavator.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadExcavatorsReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("CANEXCAVATOR API Duration :" + elapsedTime + "-" + vin);
		return canExcavator;
	}
	
private VisualizationReport loadCompactorReportV3(String vin, Date startDate, Date endDate) {
		
		log.info("Can Compactor "+vin);
		final CompactorV1 compactor = new CompactorV1();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if(machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			if(standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")|| machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			compactor.setCompactionFuelConsumption(machineCompactionCoachDataRepository.getFuelConsumptionData(vin, startDate, endDate));
			compactor.setAverageFuelConsumption(machineCompactionCoachDataRepository.getAverageFuelConsumption(vin, startDate, endDate));
			compactor.setFuelPowerBand(machineCompactionCoachDataRepository.getFuelPowerBand(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;

			if (daysDifference > compactor.getCompactionFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : compactor.getCompactionFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactor.getCompactionFuelConsumption().add(new FuelConsumptionResponse(day, null));

				}

			}
			
			Collections.sort(compactor.getCompactionFuelConsumption(), new Comparator<FuelConsumptionResponse>() {

				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
				
			if (daysDifference > compactor.getAverageFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : compactor.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactor.getAverageFuelConsumption().add(new FuelConsumptionResponse(day,null));
					
				}
				
			}
			
			Collections.sort(compactor.getAverageFuelConsumption(),
					new Comparator<FuelConsumptionResponse>() {

						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});
			
			
			if (daysDifference > compactor.getFuelPowerBand().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands fuelConsumption : compactor.getFuelPowerBand()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactor.getFuelPowerBand().add(new MachinePowerBands(day,null,null,null,null));
					
				}
				
			}
			
			Collections.sort(compactor.getFuelPowerBand(),
					new Comparator<MachinePowerBands>() {

						@Override
						public int compare(MachinePowerBands o1, MachinePowerBands o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});
			
			long fuelConsumptionCount = compactor.getCompactionFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = compactor.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long fuelPowerbandCount = compactor.getFuelPowerBand().stream().filter(e ->  e.getHighPowerBand()==null && e.getIdlePowerBand()==null && e.getLowPowerBand()==null && e.getMediumPowerBand()==null).count();

			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
			 	if(compactor.getCompactionFuelConsumption()!=null && !compactor.getCompactionFuelConsumption().isEmpty() && fuelConsumptionCount!=compactor.getCompactionFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("CompactionFuelConsumption"));
				}
				if(compactor.getAverageFuelConsumption()!=null && !compactor.getAverageFuelConsumption().isEmpty() && averageConsumptionCount!=compactor.getAverageFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
				}
				if(compactor.getFuelPowerBand()!=null && !compactor.getFuelPowerBand().isEmpty() && fuelPowerbandCount!=compactor.getFuelPowerBand().size()) {
					chartList.add(new AdvanceReportChart("FuelPowerBand"));
				}
				
				compactor.setChartList(chartList);

		} else {
			compactor.setChartList(new ArrayList<AdvanceReportChart>());
			compactor.setCompactionFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			compactor.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			compactor.setFuelPowerBand(new ArrayList<MachinePowerBands>());
			compactor.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadCompactorMachineReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLICOMPACTOR API Duration :" + elapsedTime + "-" + vin);
		return compactor;
	}

	private VisualizationReport loadTelehandlerReportV3(String vin, Date startDate, Date endDate) {

		final CanTelehandler telehandler = new CanTelehandler();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate() - 91);
			if (standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")|| machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			telehandler.setTelehandlerFuelConsumption(machineTelehandlerDataRepository.getFuelConsumptionData(vin, startDate, endDate));
			telehandler.setAverageFuelConsumption(machineTelehandlerDataRepository.getAverageFuelConsumption(vin, startDate, endDate));
			telehandler.setFuelPowerBand(machineTelehandlerDataRepository.getFuelPowerBand(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;

			if (daysDifference > telehandler.getTelehandlerFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : telehandler.getTelehandlerFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandler.getTelehandlerFuelConsumption().add(new FuelConsumptionResponse(day, null));

				}

			}

			Collections.sort(telehandler.getTelehandlerFuelConsumption(), new Comparator<FuelConsumptionResponse>() {

				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});

			if (daysDifference > telehandler.getAverageFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : telehandler.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandler.getAverageFuelConsumption().add(new FuelConsumptionResponse(day, null));

				}

			}
			
			Collections.sort(telehandler.getAverageFuelConsumption(), new Comparator<FuelConsumptionResponse>() {

				@Override
				public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});

			if (daysDifference > telehandler.getFuelPowerBand().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands fuelConsumption : telehandler.getFuelPowerBand()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandler.getFuelPowerBand().add(new MachinePowerBands(day, null, null, null, null));

				}

			}

			Collections.sort(telehandler.getFuelPowerBand(), new Comparator<MachinePowerBands>() {

				@Override
				public int compare(MachinePowerBands o1, MachinePowerBands o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
			long fuelConsumptionCount = telehandler.getTelehandlerFuelConsumption().stream().filter(e -> e.getTotalFuelUsed() == null).count();
			long averageConsumptionCount = telehandler.getAverageFuelConsumption().stream().filter(e -> e.getTotalFuelUsed() == null).count();
			long fuelPowerbandCount = telehandler.getFuelPowerBand().stream().filter(e -> e.getHighPowerBand() == null
					&& e.getIdlePowerBand() == null && e.getLowPowerBand() == null && e.getMediumPowerBand() == null)
					.count();

			List<AdvanceReportChart> chartList = new ArrayList<>();
			if (telehandler.getTelehandlerFuelConsumption() != null
					&& !telehandler.getTelehandlerFuelConsumption().isEmpty()
					&& fuelConsumptionCount != telehandler.getTelehandlerFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("TelehandlerFuelConsumption"));
			}
			if (telehandler.getAverageFuelConsumption() != null && !telehandler.getAverageFuelConsumption().isEmpty()
					&& averageConsumptionCount != telehandler.getAverageFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			if (telehandler.getFuelPowerBand() != null && !telehandler.getFuelPowerBand().isEmpty()
					&& fuelPowerbandCount != telehandler.getFuelPowerBand().size()) {
				chartList.add(new AdvanceReportChart("FuelPowerBand"));
			}

			telehandler.setChartList(chartList);

		} else {
			telehandler.setChartList(new ArrayList<AdvanceReportChart>());
			telehandler.setCompactionFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			telehandler.setAverageFuelConsumption(new ArrayList<FuelConsumptionResponse>());
			telehandler.setFuelPowerBand(new ArrayList<MachinePowerBands>());
			telehandler.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadtelehandlerMachineReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLITELEHANDLER API Duration :" + elapsedTime + "-" + vin);
		return telehandler;

	}
	
	@Override
	public IntelliReport getIntelliReportV3(String vin, Date startDate, Date endDate) {
		if (machineFeatureService.isExist(vin)) {
			List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlag(vin, true);
			for (MachineFeatureInfo machineFeatureInfo : list) {
				switch (machineFeatureInfo.getType()) {
				case "INTELLILOAD":
					return loadWheelLoaderIntelliReportV3(vin, startDate, endDate);
				case "INTELLICOMPACTOR":
					return loadCompactorMachineReportV3(vin, startDate, endDate);
				}
			}
		}

		return null;
	}
	
	private IntelliReport loadWheelLoaderIntelliReportV3(String vin, Date startDate, Date endDate) {
		WheelLoaderIntelliLoadV3 wheelLoaderIntelliLoad = new WheelLoaderIntelliLoadV3();
		long start = System.currentTimeMillis();

		Machine machine = machineRepository.findByVin(vin);
		if(machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			log.info("DATES "+standardDate+"-"+startDate+"-"+endDate);
			if(standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium")  || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			wheelLoaderIntelliLoad.setWeekelyBucketCount(machineWheelLoaderDataRepository.getBucketCountData(vin, startDate, endDate));
			wheelLoaderIntelliLoad.setWeekelyDutyCycle(machineWheelLoaderDataRepository.getDutyCycleData(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > wheelLoaderIntelliLoad.getWeekelyBucketCount().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (WeekelyBucketCount weekelyBucketCount : wheelLoaderIntelliLoad.getWeekelyBucketCount()) {
					list.remove(weekelyBucketCount.getDate());
				}
				for (Date day : list) {
					wheelLoaderIntelliLoad.getWeekelyBucketCount().add(new WeekelyBucketCount(day, null));
					wheelLoaderIntelliLoad.getWeekelyDutyCycle().add(new WeekelyDutyCycle(day, null));
				}
				Collections.sort(wheelLoaderIntelliLoad.getWeekelyBucketCount(), new Comparator<WeekelyBucketCount>() {
					@Override
					public int compare(WeekelyBucketCount o1, WeekelyBucketCount o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(wheelLoaderIntelliLoad.getWeekelyDutyCycle(), new Comparator<WeekelyDutyCycle>() {
					@Override
					public int compare(WeekelyDutyCycle o1, WeekelyDutyCycle o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
			long weekelyBucketCount = wheelLoaderIntelliLoad.getWeekelyBucketCount().stream().filter(e ->  e.getTotalBucketCount()==null).count();
			long weekelyDutyCycleCount = wheelLoaderIntelliLoad.getWeekelyDutyCycle().stream().filter(e ->  e.getCumulativeLoadedWeight()==null).count();
			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
				if(wheelLoaderIntelliLoad.getWeekelyBucketCount()!=null && !wheelLoaderIntelliLoad.getWeekelyBucketCount().isEmpty() && wheelLoaderIntelliLoad.getWeekelyBucketCount().size()!=weekelyBucketCount) {
					chartList.add(new AdvanceReportChart("WeekelyBucketCount"));
				}else {
					wheelLoaderIntelliLoad.setWeekelyBucketCount(new ArrayList<WeekelyBucketCount>());
				}
				if(wheelLoaderIntelliLoad.getWeekelyDutyCycle()!=null && !wheelLoaderIntelliLoad.getWeekelyDutyCycle().isEmpty() && wheelLoaderIntelliLoad.getWeekelyDutyCycle().size()!=weekelyDutyCycleCount) {
					chartList.add(new AdvanceReportChart("WeekelyDutyCycle"));
				}else {
					wheelLoaderIntelliLoad.setWeekelyDutyCycle(new ArrayList<WeekelyDutyCycle>());
				}
				
			wheelLoaderIntelliLoad.setChartList(chartList);

		} else {
			wheelLoaderIntelliLoad.setWeekelyBucketCount(new ArrayList<WeekelyBucketCount>());
			wheelLoaderIntelliLoad.setWeekelyDutyCycle(new ArrayList<WeekelyDutyCycle>());
			wheelLoaderIntelliLoad.setChartList(new ArrayList<AdvanceReportChart>());
			wheelLoaderIntelliLoad.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadWheelLoaderIntelliReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLILOAD API Duration :" + elapsedTime + "-" + vin);
		return wheelLoaderIntelliLoad;
	}
	
	private IntelliReport loadCompactorMachineReportV3(String vin, Date startDate, Date endDate) {
		final CanCompactorV3 canCompactor = new CanCompactorV3();
		long start = System.currentTimeMillis();
		Machine machine = machineRepository.findByVin(vin);
		if(machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			Date standardDate = new Date();
			standardDate.setDate(standardDate.getDate()-91);
			if(standardDate.after(startDate) && standardDate.after(endDate)) {
				startDate = utilities.getDate(utilities.getStartDate(90));
				endDate = utilities.getDate(utilities.getStartDate(1));
			}
		}
		if (machine.getPremiumFlag() != null && (machine.getPremiumFlag().equalsIgnoreCase("Premium") || machine.getPremiumFlag().equalsIgnoreCase("Standard"))) {
			canCompactor.setCompactionDutyCycleData(machineCompactionCoachDataRepository.findByVinForLifeBetweenDay(vin));
			canCompactor.setCompactionWeeklyDutyCycleData(machineCompactionCoachDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, startDate, endDate));
			canCompactor.setCompactionVibrationOnOffData(machineCompactionCoachDataRepository.findVibrationData(vin, startDate, endDate));
			
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > canCompactor.getCompactionWeeklyDutyCycleData().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (CompactionWeeklyDutyCycleData compactionWeeklyDutyCycleData : canCompactor
						.getCompactionWeeklyDutyCycleData()) {
					list.remove(compactionWeeklyDutyCycleData.getDate());
				}
				for (Date day : list) {
					canCompactor.getCompactionWeeklyDutyCycleData()
							.add(new CompactionWeeklyDutyCycleData(null, null, null, day));
					canCompactor.getCompactionVibrationOnOffData()
							.add(new CompactionVibrationOnOffData(day, null, null));
				}
				Collections.sort(canCompactor.getCompactionWeeklyDutyCycleData(),
						new Comparator<CompactionWeeklyDutyCycleData>() {

							@Override
							public int compare(CompactionWeeklyDutyCycleData o1, CompactionWeeklyDutyCycleData o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
				Collections.sort(canCompactor.getCompactionVibrationOnOffData(),
						new Comparator<CompactionVibrationOnOffData>() {

							@Override
							public int compare(CompactionVibrationOnOffData o1, CompactionVibrationOnOffData o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			
			long weekelyDutyCycleCount = canCompactor.getCompactionWeeklyDutyCycleData().stream().filter(e ->  e.getHighVibration()==null && e.getLowVibration()==null && e.getStaticPass()==null).count();
			long vibrationOnOffDataCount = canCompactor.getCompactionVibrationOnOffData().stream().filter(e ->  e.getVibration_off_hrs()==null && e.getVibration_on_hrs()==null).count();
			
			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
			 	if(canCompactor.getCompactionWeeklyDutyCycleData()!=null && !canCompactor.getCompactionWeeklyDutyCycleData().isEmpty() && canCompactor.getCompactionWeeklyDutyCycleData().size()!=weekelyDutyCycleCount) {
					chartList.add(new AdvanceReportChart("CompactionWeeklyDutyCycleData"));
				}else {
					
					canCompactor.setCompactionWeeklyDutyCycleData(new ArrayList<CompactionWeeklyDutyCycleData>());
				}
			 	if(canCompactor.getCompactionDutyCycleData().getHighVibration()!=null && canCompactor.getCompactionDutyCycleData().getLowVibration()!=null && canCompactor.getCompactionDutyCycleData().getStaticPass()!=null) {
					chartList.add(new AdvanceReportChart("CompactionDutyCycleData"));
				}else {
					canCompactor.setCompactionDutyCycleData(null);
				}
				
				if(canCompactor.getCompactionVibrationOnOffData()!=null && !canCompactor.getCompactionVibrationOnOffData().isEmpty() && canCompactor.getCompactionVibrationOnOffData().size()!=vibrationOnOffDataCount) {
					chartList.add(new AdvanceReportChart("CompactionVibrationOnOffData"));
				}else {
					
					canCompactor.setCompactionVibrationOnOffData(new ArrayList<CompactionVibrationOnOffData>());
				}
				
				canCompactor.setChartList(chartList);

		} else {
			canCompactor.setCompactionDutyCycleData(new CompactionDutyCycleData(null, null, null));
			canCompactor.setCompactionVibrationOnOffData(new ArrayList<CompactionVibrationOnOffData>());
			canCompactor.setCompactionWeeklyDutyCycleData(new ArrayList<CompactionWeeklyDutyCycleData>());
			canCompactor.setChartList(new ArrayList<AdvanceReportChart>());
			canCompactor.setMessage("Join Livelink premium to access these reports data");
		}

		log.info("loadCompactorMachineReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLICOMPACTOR API Duration :" + elapsedTime + "-" + vin);
		return canCompactor;
	}
	
	@Override
	public StandardMachineBaseResponse getStandardMachineImages() {
		StandardMachineBaseResponse standardMachineBaseResponse = new StandardMachineBaseResponse();
		List<StdMachineImagesResponse> machineImageList = new ArrayList<StdMachineImagesResponse>();
		String awsUrl = MessagesConstantsList.AWS_IMAGE_PREMIUM_IMAGES_URL;
		
		StdMachineImagesResponse imgResponseOne = new StdMachineImagesResponse();
		imgResponseOne.setId(1);
		imgResponseOne.setImageName("Machine Utilization");
		imgResponseOne.setImageUrl(awsUrl+machineUtilizationImg);
		imgResponseOne.setImagePath(awsUrl+machineUtilizationImgPath);

		StdMachineImagesResponse imgResponseTwo = new StdMachineImagesResponse();
		imgResponseTwo.setId(2);
		imgResponseTwo.setImageName("Fuel Utilization");
		imgResponseTwo.setImageUrl(awsUrl+fuelUtilizationImg);
		imgResponseTwo.setImagePath(awsUrl+fuelUtilizationImgPath);

		StdMachineImagesResponse imgResponseThree = new StdMachineImagesResponse();
		imgResponseThree.setId(3);
		imgResponseThree.setImageName("Machine Location");
		imgResponseThree.setImageUrl(awsUrl+machineLocationImg);
		imgResponseThree.setImagePath(awsUrl+machineLocationImgPath);

		StdMachineImagesResponse imgResponseFour = new StdMachineImagesResponse();
		imgResponseFour.setId(4);
		imgResponseFour.setImageName("Machine Operating System");
		imgResponseFour.setImageUrl(awsUrl+machineOSImg);
		imgResponseFour.setImagePath(awsUrl+machineOSImgPath);
		
		StdMachineImagesResponse imgResponseFive = new StdMachineImagesResponse();
		imgResponseFive.setId(5);
		imgResponseFive.setImageName("Machine Navigation");
		imgResponseFive.setImageUrl(awsUrl+machineNavigationImg);
		imgResponseFive.setImagePath(awsUrl+machineNavigationImgIos);
		
		StdMachineImagesResponse imgResponseSix = new StdMachineImagesResponse();
		imgResponseSix.setId(6);
		imgResponseSix.setImageName("Machine Fencing");
		imgResponseSix.setImageUrl(awsUrl+machineFencingImg);
		imgResponseSix.setImagePath(awsUrl+machineFencingImgIos);

		machineImageList.add(imgResponseOne);
		machineImageList.add(imgResponseTwo);
		machineImageList.add(imgResponseThree);
		machineImageList.add(imgResponseFour);
		machineImageList.add(imgResponseFive);
		machineImageList.add(imgResponseSix);
		standardMachineBaseResponse.setData(machineImageList);

		return standardMachineBaseResponse;
	}

}
