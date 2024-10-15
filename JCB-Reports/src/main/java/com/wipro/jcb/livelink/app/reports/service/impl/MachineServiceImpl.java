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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeatureInfo;
import com.wipro.jcb.livelink.app.reports.entity.MachineWheelLoaderData;
import com.wipro.jcb.livelink.app.reports.repo.MachineBHLDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineCompactionCoachDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineExcavatorRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineFeatureDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineWheelLoaderDataRepository;
import com.wipro.jcb.livelink.app.reports.report.CanBHLV2;
import com.wipro.jcb.livelink.app.reports.report.CanCompactorV2;
import com.wipro.jcb.livelink.app.reports.report.CanExcavatorV2;
import com.wipro.jcb.livelink.app.reports.report.CanLoaderV2;
import com.wipro.jcb.livelink.app.reports.report.CompactionDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.CompactionVibrationOnOffData;
import com.wipro.jcb.livelink.app.reports.report.CompactionWeeklyDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.DutyCycleBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavationModesBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorPowerModes;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorTravelAndSwingTime;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse;
import com.wipro.jcb.livelink.app.reports.report.GearTimeSpentBHL;
import com.wipro.jcb.livelink.app.reports.report.IntelliReport;
import com.wipro.jcb.livelink.app.reports.report.MachineCompassBHL;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReport;
import com.wipro.jcb.livelink.app.reports.report.WeekelyBucketCount;
import com.wipro.jcb.livelink.app.reports.report.WeekelyDutyCycle;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderGearUtilization;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderIntelliLoadV2;
import com.wipro.jcb.livelink.app.reports.service.MachineFeatureInfoService;
import com.wipro.jcb.livelink.app.reports.service.MachineService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@PropertySource("application.properties")
public class MachineServiceImpl implements MachineService{
	
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

}
