package com.wipro.jcb.livelink.app.reports.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.wipro.jcb.livelink.app.reports.commonUtils.BorderEvent;
import com.wipro.jcb.livelink.app.reports.commonUtils.CellBorderEvent;
import com.wipro.jcb.livelink.app.reports.commonUtils.HeaderPageEvent;
import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.constants.FuelLevelNAConfig;
import com.wipro.jcb.livelink.app.reports.entity.Alert;
import com.wipro.jcb.livelink.app.reports.entity.Customer;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeatureInfo;
import com.wipro.jcb.livelink.app.reports.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.reports.entity.MachineFuelHistory;
import com.wipro.jcb.livelink.app.reports.entity.MachineWheelLoaderData;
import com.wipro.jcb.livelink.app.reports.entity.PdfAnalyticsData;
import com.wipro.jcb.livelink.app.reports.repo.AlertRepository;
import com.wipro.jcb.livelink.app.reports.repo.CustomerRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineBHLDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineCompactionCoachDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineEngineStatusHistoryDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineExcavatorRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineFeatureDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineFeedParserDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachineFuelHistoryDataRepo;
import com.wipro.jcb.livelink.app.reports.repo.MachinePerformanceDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineTelehandlerDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineUtilizationDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.MachineWheelLoaderDataRepository;
import com.wipro.jcb.livelink.app.reports.repo.PdfAnalyticsDataRepo;
import com.wipro.jcb.livelink.app.reports.report.AdvanceReportChart;
import com.wipro.jcb.livelink.app.reports.report.AverageSpeedRoading;
import com.wipro.jcb.livelink.app.reports.report.BHLReport;
import com.wipro.jcb.livelink.app.reports.report.CompactorReport;
import com.wipro.jcb.livelink.app.reports.report.DistanceTraveledRoading;
import com.wipro.jcb.livelink.app.reports.report.DutyCycleBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorPowerModes;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorReport;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorTravelAndSwingTime;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionDuty;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionExcavation;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse;
import com.wipro.jcb.livelink.app.reports.report.GearTimeSpentBHL;
import com.wipro.jcb.livelink.app.reports.report.HammerAbuseEventCount;
import com.wipro.jcb.livelink.app.reports.report.HammerUsedHours;
import com.wipro.jcb.livelink.app.reports.report.MachineCompassBHL;
import com.wipro.jcb.livelink.app.reports.report.MachinePowerBands;
import com.wipro.jcb.livelink.app.reports.report.PdfReportData;
import com.wipro.jcb.livelink.app.reports.report.PdfReportResponse;
import com.wipro.jcb.livelink.app.reports.report.PdfS3Reponse;
import com.wipro.jcb.livelink.app.reports.report.TelehandlerReport;
import com.wipro.jcb.livelink.app.reports.report.WLSReport;
import com.wipro.jcb.livelink.app.reports.report.WheelLoaderGearUtilization;
import com.wipro.jcb.livelink.app.reports.response.DailyAverageUtilizationData;
import com.wipro.jcb.livelink.app.reports.response.DateValue;
import com.wipro.jcb.livelink.app.reports.response.DoubleValue;
import com.wipro.jcb.livelink.app.reports.response.EngineHistoryDataListV2;
import com.wipro.jcb.livelink.app.reports.response.FuelHistoryDataListV2;
import com.wipro.jcb.livelink.app.reports.response.MachineDetails;
import com.wipro.jcb.livelink.app.reports.response.MachineDutyCycle;
import com.wipro.jcb.livelink.app.reports.response.MachineEngineStatus;
import com.wipro.jcb.livelink.app.reports.response.MachineExcavationMode;
import com.wipro.jcb.livelink.app.reports.response.MachinePowerBand;
import com.wipro.jcb.livelink.app.reports.response.MachineWorkingIdleStatus;
import com.wipro.jcb.livelink.app.reports.response.UtilizationDetails;
import com.wipro.jcb.livelink.app.reports.service.LoadDataOnDemandRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoadDataOnDemandRequestImpl implements LoadDataOnDemandRequest{
	
	@Autowired
	private ReportUtilities utilities;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private MachineFeedParserDataRepo machineFeedParserDataRepo;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Autowired
	private MachineFeatureInfoServiceImpl machineFeatureService;
	
	@Autowired
	private MachineFuelHistoryDataRepo machineFuelHistoryDataRepo;
	
	@Autowired
	private MachineUtilizationDataRepository machineUtilizationDataRepository;
	
	@Autowired
	private MachinePerformanceDataRepository machinePerformanceDataRepository;
	
	@Autowired
	private MachineEngineStatusHistoryDataRepo machineEngineStatusHistoryDataRepo;
	
	@Autowired
	private MachineFeatureDataRepo machineFeatureDataRepo;
	
	@Autowired
	private MachineBHLDataRepo machineBHLDataRepo;
	
	@Autowired
	private MachineWheelLoaderDataRepository machineWheelLoaderDataRepository;
	
	@Autowired
	private MachineExcavatorRepo machineExcavatorRepo;
	
	@Autowired
	private MachineCompactionCoachDataRepository machineCompactionCoachDataRepository;
	
	@Autowired
	private MachineTelehandlerDataRepository machineTelehandlerDataRepository;
	
	@Autowired
	private PdfAnalyticsDataRepo pdfAnalyticsDataRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	private final Integer TABLEWIDTH=105;
	String IMAGELOACATION ="/Users/vi20475016/Documents/Docs/PDF_Response";
	private static final DecimalFormat decfor = new DecimalFormat("0.00");
	
	@Override
	public PdfReportData getPdfReportData(String vin, Date startdate, Date enddate) {
		final PdfReportResponse pdfReportResponse =  getPdfJsonData(vin,startdate,enddate);
		return pdfReportResponse;
	}
	
	private PdfReportResponse getPdfJsonData(String vin,Date startdate, Date enddate) {
		PdfReportResponse response = new PdfReportResponse();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date day =null,day2=null;
			if(startdate!=null && enddate!=null) {
				day = startdate; // start date
				day2 = enddate; // end date
				
				response.setFromDate(dateformat.format(startdate));
				enddate.setDate(enddate.getDate()-1);
				response.setToDate(dateformat.format(enddate));
				enddate.setDate(enddate.getDate()+1);
				response.setTodayDate(utilities.getDDMMYY(0));
					
			}else {
				day = utilities.getDate(utilities.getStartDate(7));
				day2 = utilities.getDate(utilities.getStartDate(1));
				response.setFromDate(utilities.getDDMMYY(7));
				response.setToDate(utilities.getDDMMYY(1));
				response.setTodayDate(utilities.getDDMMYY(0));
			}
			
			log.info("Response : " +response.getFromDate() +" - "+response.getToDate() +" - "+response.getTodayDate());
			
			List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();
			List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();
			boolean flag = true;
			MachineDetails  vinDetails = new MachineDetails();
			final Machine machineDetails = machineRepository.findByVin(vin);
			final Customer customerDetails = customerRepo.findById(machineDetails.getCustomerId()).get();
			MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);

			vinDetails.setVin(machineDetails.getVin());
			vinDetails.setModel(machineDetails.getModel() !=null ? machineDetails.getModel() : null);
			vinDetails.setRenewalDate(machineDetails.getRenewalDate()!=null ? machineDetails.getRenewalDate() : null);
			vinDetails.setCustomerName(customerDetails!=null ? customerDetails.getName() : null);
			vinDetails.setMobileNumber(customerDetails!=null ? customerDetails.getPhonenumber() : null);
			
			List<Alert> machineAlertDetails = alertRepository.getAlertDetailsByVin(vin);
			response.setMachineAlertDetails(machineAlertDetails);
			
			Long alertCount = alertRepository.getAlertCountVin(vin);
			
			// Fuel Summary 
			MachineFuelHistory machineFuelStatus =  machineFuelHistoryDataRepo.getFuelStatusByVin(vin);
			response.setMachineFuelStatus(machineFuelStatus);
			
			// Engine On/Off Time
			MachineEngineStatus machineEngineStatus = new MachineEngineStatus();
			Double engineOn = machineUtilizationDataRepository.getEngineOnHrs(vin, day, day2);
			Double engineOff = machineUtilizationDataRepository.getEngineOffHrs(vin, day, day2);
			machineEngineStatus.setEngineOn(engineOn);
			machineEngineStatus.setEngineOff(engineOff);
			if(machineFuelStatus!=null && machineFuelStatus.getFuelLevel()!=null)
			{
				machineEngineStatus.setFuel((int) Math.round(machineFuelStatus.getFuelLevel()));	
			}
			
			response.setMachineEngineStatus(machineEngineStatus);
			
			//Utilization
			UtilizationDetails utilizationDetails = new UtilizationDetails();
			Double utilization = machineUtilizationDataRepository.getUtilization(vin, day, day2); 
			Double idleHrs = machineUtilizationDataRepository.getIdleHrs(vin, day, day2);
			Double workingHrs = machineUtilizationDataRepository.getWorkingTimeAvg(vin, day, day2);
			Double offHrs = machineUtilizationDataRepository.getOffHrs(vin, day, day2);
			
			
			utilizationDetails.setIdlePercentage((int)  Math.round(idleHrs /(idleHrs +offHrs +workingHrs) * 100));
			utilizationDetails.setUtilization(utilization);
			utilizationDetails.setIdleHrs(idleHrs);
			utilizationDetails.setAlertCount(alertCount);
			
			if(machineFeedParserData!=null && machineFeedParserData.getStatusAsOnTime()!=null)
			{
				vinDetails.setStatusOn(machineFeedParserData.getStatusAsOnTime());
				utilizationDetails.setUtilizationTill(machineFeedParserData.getTotalMachineHours());
			}else {
				vinDetails.setStatusOn(machineDetails.getStatusAsOnTime()!=null ? machineDetails.getStatusAsOnTime() : null);
				utilizationDetails.setUtilizationTill(machineDetails.getTotalMachineHours()!=null ? machineDetails.getTotalMachineHours() : null);
			}
			
			response.setUtilizationDetails(utilizationDetails);
			
			//Cumulative Time Distribution
			MachineWorkingIdleStatus machineWorkingIdleStatus = new MachineWorkingIdleStatus();
			
			machineWorkingIdleStatus.setIdleHrs(idleHrs);
			machineWorkingIdleStatus.setWorkingHrs(workingHrs);
			response.setMachineWorkingIdleStatus(machineWorkingIdleStatus);
			
			// Power Band Summary
			MachinePowerBand machinePowerBand = machinePerformanceDataRepository.getPowerBand(vin, day, day2);
			if(machinePowerBand!=null && machinePowerBand.getPowerBandHighInHours()!=null && machinePowerBand.getPowerBandLowInHours()!=null && machinePowerBand.getPowerBandMediumInHours()!=null)
			{
				Integer highSpeed  = (int) Math.round(machinePowerBand.getPowerBandHighInHours() / (machinePowerBand.getPowerBandHighInHours() + machinePowerBand.getPowerBandMediumInHours() + machinePowerBand.getPowerBandLowInHours()) * 100);
				Integer lowSpeed  = (int) Math.round(machinePowerBand.getPowerBandLowInHours() / (machinePowerBand.getPowerBandHighInHours() + machinePowerBand.getPowerBandMediumInHours() + machinePowerBand.getPowerBandLowInHours()) * 100);
				Integer mediumSpeed  = (int) Math.round(machinePowerBand.getPowerBandMediumInHours() / (machinePowerBand.getPowerBandHighInHours() + machinePowerBand.getPowerBandMediumInHours() + machinePowerBand.getPowerBandLowInHours()) * 100);
				machinePowerBand.setHighSpeed(highSpeed);
				machinePowerBand.setLowSpeed(lowSpeed);
				machinePowerBand.setMediumSpeed(mediumSpeed);
				}
			response.setMachinePowerBand(machinePowerBand);
			
			if ((!FuelLevelNAConfig.getExceptionMachines().contains(vin))
					&& FuelLevelNAConfig.getFuellevelnaconfig().containsKey(machineDetails.getPlatform())
					&& FuelLevelNAConfig.getFuellevelnaconfig().get(machineDetails.getPlatform()).contains(vin.substring(3, 8))) {
				flag = false;
				fuelHistoryDayDataList = null;
			}
			
			LocalDate startingDate = null;
			LocalDate endingDate  = null ;
			if(startdate!=null && enddate!=null) {
			
				 startingDate = startdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				 endingDate = enddate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}else {
				Date assignedDate = new Date ();
				assignedDate.setDate(assignedDate.getDate()-7);
				Date endedDate = new Date ();
				
				 startingDate = assignedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				 endingDate = endedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			
			List<DailyAverageUtilizationData> dayUtilization = new ArrayList<>();
			
			for (LocalDate localdate = startingDate; localdate.isBefore(endingDate); localdate = localdate.plusDays(1))
			{
				Date startDate = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				LocalDate endlocalDate = localdate.plusDays(1);
				Date endDate = Date.from(endlocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

			  log.info("StartingDate and EndingDate "+localdate +" - "+startDate +" - "+endDate) ;
			  EngineHistoryDataListV2 engineHistoryData = new EngineHistoryDataListV2();
				engineHistoryData.date = startDate;
				engineHistoryData.timestamps = machineEngineStatusHistoryDataRepo.getDateByVin(vin, startDate, endDate);
				engineHistoryData.values = machineEngineStatusHistoryDataRepo.getByVin(vin, startDate, endDate);
				engineHistoryDayDataList.add(engineHistoryData);
				
				  if (flag) { 
					  FuelHistoryDataListV2 fuelHistoryData = new FuelHistoryDataListV2(); 
					  fuelHistoryData.date = new Date(startDate.getTime());
					  log.info("startDate "+startDate);
					  for(int j=24;j>0;j--) {
						  Date startTime = new Date(startDate.getTime());
						  startTime.setHours(j);
						  Date endTime = new Date(startDate.getTime());
						  endTime.setHours(j-1);
						  
						  Double fuelLevel = machineFuelHistoryDataRepo.getFuelLevelByOneHour(vin,endTime, startTime);
						  Date date = machineFuelHistoryDataRepo.getDateLevelByOneHour(vin,endTime, startTime);
						 if(fuelLevel!=null && date!=null)
						 {
							 DateValue dateValue= new DateValue();
							  dateValue.setKey(date);
							  fuelHistoryData.timestamps.add(dateValue);
							  DoubleValue doubleValue = new DoubleValue();
							  doubleValue.setVal(fuelLevel);
							  fuelHistoryData.values.add(doubleValue);
						 }
					  }
					  fuelHistoryDayDataList.add(fuelHistoryData); 
					  }
				  Date utilizationDay = startDate;
				  DailyAverageUtilizationData utilizationData = new DailyAverageUtilizationData();
				  utilizationData.setDay(utilizationDay);
				  utilizationData.setWorkingHours(machineUtilizationDataRepository.getWorkingHours(vin,utilizationDay));
				  dayUtilization.add(utilizationData);
			}
			response.setEngineHistoryDayDataList(engineHistoryDayDataList);
			response.setFuelHistoryDayDataList(fuelHistoryDayDataList);
			response.setDayUtilization(dayUtilization);
			//Fuel Consumption
			log.info("Machine Feature Detail For "+vin);
			if (machineFeatureService.isExist(vin)) {
				List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlagOrderByDayDescLimit1(vin, true,PageRequest.of(0, 1));
				if(list!=null) {
					log.info("Machine Feature List "+list.size()+"-"+list.get(0).getType());
					for (MachineFeatureInfo machineFeatureInfo : list) {
						machineFeatureInfo.setType("CANTELEHANDLER");
						if(machineFeatureInfo.getType().equals("CANBHL") || machineFeatureInfo.getType().equals("HBBHL")){
							log.info("Machine Type -CANBHL "+"-"+vin);
							vinDetails.setPlatform("BHL");
							response.setMachineDetails(vinDetails);
							return getBHLConsumptionData(vin, day, day2,response,machinePowerBand);							
						}else if(machineFeatureInfo.getType().equals("CANWLS")) {
							log.info("Machine Type -CANWLS "+"-"+vin);
							vinDetails.setPlatform("WLS");
							response.setMachineDetails(vinDetails);
							return getWheelLoadersConsumptionData(vin,  day, day2,response);						
						}
						else if(machineFeatureInfo.getType().equals("CANEXCAVATOR")|| machineFeatureInfo.getType().equals("HBEXCAVATOR") ) {
							log.info("Machine Type -CANEXCAVATOR "+"-"+vin);
							vinDetails.setPlatform("EXCAVATOR");
							response.setMachineDetails(vinDetails);
							return getExcavatorsConsumptionData(vin,  day, day2,response);						
						}
						else if(machineFeatureInfo.getType().equals("CANCOMPACTOR")   || machineFeatureInfo.getType().equals("INTELLICOMPACTOR")  ) {
							log.info("Machine Type -CANEXCAVATOR "+"-"+vin);
							vinDetails.setPlatform("CANCOMPACTOR");
							response.setMachineDetails(vinDetails);
							return getCompactorData(vin,  day, day2,response);						
						}
						else if(machineFeatureInfo.getType().equals("CANTELEHANDLER")) {
							log.info("Machine Type -CANEXCAVATOR "+"-"+vin);
							vinDetails.setPlatform("CANTELEHANDLER");
							response.setMachineDetails(vinDetails);
							return getTelehandlerData(vin,  day, day2,response);
							
						}else {
							vinDetails.setPlatform("-");
							response.setMachineDetails(vinDetails);
						}
					}
				}else {
					log.info("Machine "+vinDetails);
					vinDetails.setPlatform("-");
					response.setMachineDetails(vinDetails);
					log.info("Machine "+response.getMachineDetails());
				}
			}else {
				log.info("Machine "+vinDetails);
				vinDetails.setPlatform("-");
				response.setMachineDetails(vinDetails);
				log.info("Machine "+response.getMachineDetails());
			}
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.info("Exception occured for PDF Report API While getting response :"+vin+"Exception -"+e.getMessage());
		}
		return response;
	}
	
	private PdfReportResponse getTelehandlerData(String vin, Date startDate, Date endDate, PdfReportResponse response) {
		final TelehandlerReport telehandlerReport = new TelehandlerReport();
		
		try{
			telehandlerReport.setTelehandlerFuelConsumption(machineTelehandlerDataRepository.getFuelConsumptionData(vin, startDate, endDate));
			telehandlerReport.setAverageFuelConsumption(machineTelehandlerDataRepository.getAverageFuelConsumption(vin, startDate, endDate));
			telehandlerReport.setFuelPowerBand(machineTelehandlerDataRepository.getFuelPowerBand(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS) + 1;
				
				if (daysDifference > telehandlerReport.getTelehandlerFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : telehandlerReport.getTelehandlerFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandlerReport.getTelehandlerFuelConsumption().add(new FuelConsumptionResponse(day,null));
					
				}
				Collections.sort(telehandlerReport.getTelehandlerFuelConsumption(),
						new Comparator<FuelConsumptionResponse>() {

							@Override
							public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			
			
			if (daysDifference > telehandlerReport.getAverageFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : telehandlerReport.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandlerReport.getAverageFuelConsumption().add(new FuelConsumptionResponse(day,null));
					
				}
				Collections.sort(telehandlerReport.getAverageFuelConsumption(),
						new Comparator<FuelConsumptionResponse>() {

							@Override
							public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			
			
			if (daysDifference > telehandlerReport.getFuelPowerBand().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands fuelConsumption : telehandlerReport.getFuelPowerBand()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					telehandlerReport.getFuelPowerBand().add(new MachinePowerBands(day,null,null,null,null));
					
				}
				Collections.sort(telehandlerReport.getFuelPowerBand(),
						new Comparator<MachinePowerBands>() {

							@Override
							public int compare(MachinePowerBands o1, MachinePowerBands o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			long fuelConsumptionCount = telehandlerReport.getTelehandlerFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = telehandlerReport.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long fuelPowerbandCount = telehandlerReport.getFuelPowerBand().stream().filter(e ->  e.getHighPowerBand()==null && e.getIdlePowerBand()==null && e.getLowPowerBand()==null && e.getMediumPowerBand()==null ).count();
			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
			 	if(telehandlerReport.getTelehandlerFuelConsumption()!=null && !telehandlerReport.getTelehandlerFuelConsumption().isEmpty() && fuelConsumptionCount!=telehandlerReport.getTelehandlerFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("TelehandlerFuelConsumption"));
				}
				if(telehandlerReport.getAverageFuelConsumption()!=null && !telehandlerReport.getAverageFuelConsumption().isEmpty() && averageConsumptionCount!=telehandlerReport.getAverageFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
				}
				if(telehandlerReport.getFuelPowerBand()!=null && !telehandlerReport.getFuelPowerBand().isEmpty() && fuelPowerbandCount!=telehandlerReport.getFuelPowerBand().size()) {
					chartList.add(new AdvanceReportChart("FuelPowerBand"));
				}	
				response.setChartList(chartList);
			response.setTelehandlerReport(telehandlerReport);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private PdfReportResponse getCompactorData(String vin, Date startDate, Date endDate, PdfReportResponse response) {
		final CompactorReport compactorReport = new CompactorReport();
		
		try{
			long start = System.currentTimeMillis();
			compactorReport.setCompactionFuelConsumption(machineCompactionCoachDataRepository.getFuelConsumptionData(vin, startDate, endDate));
			compactorReport.setAverageFuelConsumption(machineCompactionCoachDataRepository.getAverageFuelConsumption(vin, startDate, endDate));
			compactorReport.setFuelPowerBand(machineCompactionCoachDataRepository.getFuelPowerBand(vin, startDate, endDate));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS) + 1;
				
				if (daysDifference > compactorReport.getCompactionFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : compactorReport.getCompactionFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactorReport.getCompactionFuelConsumption().add(new FuelConsumptionResponse(day,null));
					
				}
				Collections.sort(compactorReport.getCompactionFuelConsumption(),
						new Comparator<FuelConsumptionResponse>() {

							@Override
							public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			
			
			if (daysDifference > compactorReport.getAverageFuelConsumption().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionResponse fuelConsumption : compactorReport.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactorReport.getAverageFuelConsumption().add(new FuelConsumptionResponse(day,null));
					
				}
				Collections.sort(compactorReport.getAverageFuelConsumption(),
						new Comparator<FuelConsumptionResponse>() {

							@Override
							public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			
			
			if (daysDifference > compactorReport.getFuelPowerBand().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands fuelConsumption : compactorReport.getFuelPowerBand()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date day : list) {
					compactorReport.getFuelPowerBand().add(new MachinePowerBands(day,null,null,null,null));
					
				}
				Collections.sort(compactorReport.getFuelPowerBand(),
						new Comparator<MachinePowerBands>() {

							@Override
							public int compare(MachinePowerBands o1, MachinePowerBands o2) {
								return o1.getDate().compareTo(o2.getDate());
							}
						});
			}
			long fuelConsumptionCount = compactorReport.getCompactionFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = compactorReport.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long fuelPowerbandCount = compactorReport.getFuelPowerBand().stream().filter(e ->  e.getHighPowerBand()==null && e.getIdlePowerBand()==null && e.getLowPowerBand()==null && e.getMediumPowerBand()==null).count();
			
			 List<AdvanceReportChart>  chartList =new ArrayList<>();
			 	if(compactorReport.getCompactionFuelConsumption()!=null && !compactorReport.getCompactionFuelConsumption().isEmpty() && fuelConsumptionCount!=compactorReport.getCompactionFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("CompactionFuelConsumption"));
				}
				if(compactorReport.getAverageFuelConsumption()!=null && !compactorReport.getAverageFuelConsumption().isEmpty() && averageConsumptionCount!=compactorReport.getAverageFuelConsumption().size()) {
					chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
				}
				if(compactorReport.getFuelPowerBand()!=null && !compactorReport.getFuelPowerBand().isEmpty() && fuelPowerbandCount!=compactorReport.getFuelPowerBand().size()) {
					chartList.add(new AdvanceReportChart("FuelPowerBand"));
				}
		log.info("loadCompactorMachineReport: returning response for machine: " + vin);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("INTELLICOMPACTOR API Duration :" + elapsedTime + "-" + vin);
		response.setChartList(chartList);
		response.setCompactorReport(compactorReport);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private PdfReportResponse getExcavatorsConsumptionData(String vin, Date day, Date day2, PdfReportResponse response) {

		try {
			final ExcavatorReport excavatorReport = new ExcavatorReport();
			excavatorReport.setExcavatorFuelConsumption(machineExcavatorRepo.getFuelConsumptionData(vin, day, day2));
			excavatorReport.setPowerModes(machineExcavatorRepo.getPowerModes(vin, day, day2));
			excavatorReport.setTravelAndSwingTime(machineExcavatorRepo.getTravelAndSwingTime(vin, day, day2));
			excavatorReport.setAverageFuelConsumption(machineExcavatorRepo.getAverageConsumptionData(vin, day, day2));
			excavatorReport.setHammerUsedHours(machineExcavatorRepo.getHammerUserHours(vin, day, day2));
			excavatorReport.setHammerAbuseEventCount(machineExcavatorRepo.getHammerAbuseEventCount(vin, day, day2));
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(day2.getTime() - day.getTime()),TimeUnit.MILLISECONDS) + 1;
			if (daysDifference > excavatorReport.getExcavatorFuelConsumption().size()) {
	
				List<Date> list = utilities.getDateMap(day, day2);
				for (FuelConsumptionResponse fuelConsumption : excavatorReport.getExcavatorFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
				for (Date days : list) {
					excavatorReport.getExcavatorFuelConsumption().add(new FuelConsumptionResponse(days, null));
					excavatorReport.getPowerModes().add(new ExcavatorPowerModes(days, null, null, null, null));
					excavatorReport.getTravelAndSwingTime().add(new ExcavatorTravelAndSwingTime(days, null, null, null));
					excavatorReport.getHammerUsedHours().add(new HammerUsedHours(day, null));
					excavatorReport.getHammerAbuseEventCount().add(new HammerAbuseEventCount(day, null));
				}
				Collections.sort(excavatorReport.getExcavatorFuelConsumption(), new Comparator<FuelConsumptionResponse>() {
					@Override
					public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(excavatorReport.getPowerModes(), new Comparator<ExcavatorPowerModes>() {
					@Override
					public int compare(ExcavatorPowerModes o1, ExcavatorPowerModes o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				Collections.sort(excavatorReport.getTravelAndSwingTime(), new Comparator<ExcavatorTravelAndSwingTime>() {
					@Override
					public int compare(ExcavatorTravelAndSwingTime o1, ExcavatorTravelAndSwingTime o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				
				Collections.sort(excavatorReport.getHammerUsedHours(), new Comparator<HammerUsedHours>() {
					@Override
					public int compare(HammerUsedHours o1, HammerUsedHours o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				
				Collections.sort(excavatorReport.getHammerAbuseEventCount(), new Comparator<HammerAbuseEventCount>() {
					@Override
					public int compare(HammerAbuseEventCount o1, HammerAbuseEventCount o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
			if (daysDifference > excavatorReport.getAverageFuelConsumption().size()) {

				List<Date> list = utilities.getDateMap(day, day2);
				for (FuelConsumptionResponse fuelConsumption : excavatorReport.getAverageFuelConsumption()) {
					list.remove(fuelConsumption.getDate());
				}
	
				for (Date days : list) {
					excavatorReport.getAverageFuelConsumption().add(new FuelConsumptionResponse(days, null));
		
				}
				Collections.sort(excavatorReport.getAverageFuelConsumption(), new Comparator<FuelConsumptionResponse>() {
					@Override
					public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});

			}
			response.setExcavatorReport(excavatorReport);
			
			long fuelConsumptionCount = excavatorReport.getExcavatorFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = excavatorReport.getAverageFuelConsumption().stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long timeTravelCount = excavatorReport.getTravelAndSwingTime().stream().filter(e ->  e.getSwingHrs()==null && e.getTotalHrs()==null && e.getTravelHrs()==null).count();
			long powermodeCount = excavatorReport.getPowerModes().stream().filter(e ->  e.getGBand()==null && e.getHBand()==null && e.getHPlusBand()==null && e.getLBand()==null).count();
			long hammerusedhrs = excavatorReport.getHammerUsedHours().stream().filter(e -> e.getHammerUsedTimeHrs()==null).count();
			long hammerabuseCount = excavatorReport.getHammerAbuseEventCount().stream().filter(e -> e.getHammerAbuseCount() ==null).count();

			
			List<AdvanceReportChart>  chartList =new ArrayList<>();
			
			if(excavatorReport.getExcavatorFuelConsumption()!=null && !excavatorReport.getExcavatorFuelConsumption().isEmpty()
					&& fuelConsumptionCount!=excavatorReport.getExcavatorFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("ExcavatorFuelConsumption"));
			}
			if(excavatorReport.getAverageFuelConsumption()!=null && !excavatorReport.getAverageFuelConsumption().isEmpty() &&
					averageConsumptionCount!=excavatorReport.getAverageFuelConsumption().size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			if(excavatorReport.getTravelAndSwingTime()!=null && !excavatorReport.getTravelAndSwingTime().isEmpty()
					&& timeTravelCount!=excavatorReport.getTravelAndSwingTime().size()) {
				chartList.add(new AdvanceReportChart("TravelAndSwingTime"));
			}
			if(excavatorReport.getPowerModes()!=null && !excavatorReport.getPowerModes().isEmpty() 
					&& powermodeCount!=excavatorReport.getPowerModes().size() ) {
				chartList.add(new AdvanceReportChart("PowerModes"));
			}
			if(excavatorReport.getHammerUsedHours()!=null  && !excavatorReport.getHammerUsedHours().isEmpty() && hammerusedhrs != excavatorReport.getHammerUsedHours().size())
				chartList.add(new AdvanceReportChart("HammerUsedHours"));
			if(excavatorReport.getHammerAbuseEventCount()!=null && !excavatorReport.getHammerAbuseEventCount().isEmpty() && hammerabuseCount!=excavatorReport.getHammerAbuseEventCount().size())
				chartList.add(new AdvanceReportChart("HammerAbuseEventCount"));
			
			response.setChartList(chartList);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}
	
	private PdfReportResponse getWheelLoadersConsumptionData(String vin, Date startDate, Date endDate,
			PdfReportResponse response) {
		try {
			WLSReport wLSReport= new WLSReport();
			List<FuelConsumptionResponse> wlsFuelConsumption = new ArrayList<>();
			List<FuelConsumptionResponse> averageFuelConsumption = new ArrayList<>();
			List<WheelLoaderGearUtilization> wlsGearUtilization = new ArrayList<>();
			List<MachinePowerBands> fuelPowerBand = new ArrayList<>();
			
			List<MachineWheelLoaderData> machineWheelLoaderData = machineWheelLoaderDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, startDate, endDate);
			
			final long daysDifference =  TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()),TimeUnit.MILLISECONDS) + 1;
			
			if(machineWheelLoaderData!=null && !machineWheelLoaderData.isEmpty()) {
				log.info("trye");
				
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
					averageFuelConsumption.add(new FuelConsumptionResponse(date,wheelLoader.getAverageFuelConsumption()));
					fuelPowerBand.add(new MachinePowerBands(date, wheelLoader.getFuelUsedInLPBLtrs(), wheelLoader.getFuelUsedInMPBLtrs(), wheelLoader.getFuelUsedInHPBLtrs(), wheelLoader.getFuelLoss()));
				}
				
				if (daysDifference > wlsFuelConsumption.size()) {
					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : wlsFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date days : list) {
						wlsFuelConsumption.add(new FuelConsumptionResponse(days, null));
						wlsGearUtilization.add(new WheelLoaderGearUtilization(days, null, null));
						averageFuelConsumption.add(new FuelConsumptionResponse(days, null));
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
					Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});
				}
			}
			
			if (daysDifference > fuelPowerBand.size()) {
				
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (MachinePowerBands powerBands : fuelPowerBand) {
					list.remove(powerBands.getDate());
				}
				for (Date day : list) {
					fuelPowerBand.add(new MachinePowerBands(day, null,null,null,null));
				}
				Collections.sort(fuelPowerBand, new Comparator<MachinePowerBands>() {
					@Override
					public int compare(MachinePowerBands o1, MachinePowerBands o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
			}
			
			wLSReport.setFuelConsumption(wlsFuelConsumption);
			wLSReport.setDailyFuelAverage(averageFuelConsumption);
			wLSReport.setWlsGearUtilization(wlsGearUtilization);
			wLSReport.setFuelPowerBand(fuelPowerBand);
			
			long fuelConsumptionCount = wlsFuelConsumption.stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageFuelnCount = averageFuelConsumption.stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long gearUtilizationCount = wlsGearUtilization.stream().filter(e ->  e.getBackward()==null && e.getForward()==null).count();
			long fuelPowerbandCount = fuelPowerBand.stream().filter(e ->  e.getHighPowerBand()==null && e.getIdlePowerBand()==null && e.getLowPowerBand()==null && e.getMediumPowerBand()==null).count();
			
			List<AdvanceReportChart>  chartList =new ArrayList<>();
			

			if(wlsFuelConsumption!=null && !wlsFuelConsumption.isEmpty() && fuelConsumptionCount!=wlsFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("WlsFuelConsumption"));
			}

			if(averageFuelConsumption!=null && !averageFuelConsumption.isEmpty() && averageFuelnCount!=averageFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}

			if(wlsGearUtilization!=null && !wlsGearUtilization.isEmpty() && gearUtilizationCount!=wlsGearUtilization.size()) {
				chartList.add(new AdvanceReportChart("WlsGearUtilization"));
			}

			if(fuelPowerBand!=null && !fuelPowerBand.isEmpty() && fuelPowerbandCount!=fuelPowerBand.size()) {
				chartList.add(new AdvanceReportChart("FuelPowerBand"));
			}
			response.setChartList(chartList);
			response.setWLSReport(wLSReport);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}
	
	
	private PdfReportResponse getBHLConsumptionData(String vin, Date startDate, Date endDate, PdfReportResponse response, MachinePowerBand machinePowerBand) {
		BHLReport bhlReport= new BHLReport();
		try {
			List<FuelConsumptionBHL> machineFuelConsumptionData = machineBHLDataRepo.getFuelConsumptionData(vin, startDate, endDate);
			List<FuelConsumptionBHL> machineAverageFuelConsumption = machineBHLDataRepo.getAverageFuelConsumption(vin, startDate, endDate);
			List<GearTimeSpentBHL> gearTimeSpentBHLList = machineBHLDataRepo.getGearTimeSpentData(vin, startDate, endDate);
			List<MachineCompassBHL> machineCompassBHLList= machineBHLDataRepo.getMachineCompassBHLData(vin,  startDate, endDate);
			List<DutyCycleBHL> dutyCycleBHLList =  machineBHLDataRepo.getDutyCycleData(vin,  startDate, endDate);
			
			//New Charts 
			bhlReport.setFuelConsumptionDuty(machineBHLDataRepo.getFuelConsumptionDuty(vin, startDate, endDate));
			bhlReport.setFuelConsumptionExcavation(machineBHLDataRepo.getFuelConsumptionExcavation(vin, startDate, endDate));
			bhlReport.setDistanceRoading(machineBHLDataRepo.getDistanceTraveledRoading(vin, startDate, endDate));
			bhlReport.setAverageRoading(machineBHLDataRepo.getAverageSpeedRoading(vin, startDate, endDate));
			
			final long daysDifference = TimeUnit.DAYS.convert(Math.abs(startDate.getTime() - endDate.getTime()), TimeUnit.MILLISECONDS) + 1;
			List<FuelConsumptionResponse> bhlFuelConsumption = new ArrayList<>();
			List<FuelConsumptionResponse> averageFuelConsumption = new ArrayList<>();
			
			if(machineFuelConsumptionData!=null && !machineFuelConsumptionData.isEmpty()) {
				// Fuel Consumption
				for (FuelConsumptionBHL fuelData : machineFuelConsumptionData) {
					Date date = fuelData.getDay();
					log.info("Fuel "+date+"-"+fuelData.getTotalFuelUsedInLtrs());
					bhlFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}
				if (daysDifference > bhlFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : bhlFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date missedDay : list) {
						bhlFuelConsumption.add(new FuelConsumptionResponse(missedDay, null));
					}
					Collections.sort(bhlFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});
					
				}
			}else {
				log.info("Null Values");
			}
			
			if(machineAverageFuelConsumption!=null && !machineAverageFuelConsumption.isEmpty()) {
				
				// Average Fuel Consumption
				for (FuelConsumptionBHL fuelData : machineAverageFuelConsumption) {
					Date date = fuelData.getDay();
					log.info("daily avreage "+date+"-"+fuelData.getTotalFuelUsedInLtrs());
					averageFuelConsumption.add(new FuelConsumptionResponse(date, fuelData.getTotalFuelUsedInLtrs()));
				}
				
				if (daysDifference > averageFuelConsumption.size()) {

					List<Date> list = utilities.getDateMap(startDate, endDate);
					for (FuelConsumptionResponse fuelConsumption : averageFuelConsumption) {
						list.remove(fuelConsumption.getDate());
					}
					for (Date missedDay : list) {
						averageFuelConsumption.add(new FuelConsumptionResponse(missedDay, null));
					}
					Collections.sort(averageFuelConsumption, new Comparator<FuelConsumptionResponse>() {
						@Override
						public int compare(FuelConsumptionResponse o1, FuelConsumptionResponse o2) {
							return o1.getDate().compareTo(o2.getDate());
						}
					});
					
				}
			}
			
			if(gearTimeSpentBHLList!=null && !gearTimeSpentBHLList.isEmpty()) {
				
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (GearTimeSpentBHL dutyCycleBhl : gearTimeSpentBHLList) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date days : list) {
					gearTimeSpentBHLList.add(new GearTimeSpentBHL(days, null, null, null, null));
					machineCompassBHLList.add(new MachineCompassBHL(days, null, null, null));
					dutyCycleBHLList.add(new DutyCycleBHL(days, null, null, null, null, null));
				}
				
				Collections.sort(gearTimeSpentBHLList, new Comparator<GearTimeSpentBHL>() {
					@Override
					public int compare(GearTimeSpentBHL gt1, GearTimeSpentBHL gt2) {
						return gt1.getDay().compareTo(gt2.getDay());
					}
				});
				Collections.sort(machineCompassBHLList, new Comparator<MachineCompassBHL>() {
					@Override
					public int compare(MachineCompassBHL mc1, MachineCompassBHL mc2) {
						return mc1.getDay().compareTo(mc2.getDay());
					}
				});
				Collections.sort(dutyCycleBHLList, new Comparator<DutyCycleBHL>() {
					@Override
					public int compare(DutyCycleBHL gt1, DutyCycleBHL gt2) {
						return gt1.getDay().compareTo(gt2.getDay());
					}
				});
			}
			
			//Fuel Consumption Duty
			if (daysDifference > bhlReport.getFuelConsumptionDuty().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionDuty dutyCycleBhl : bhlReport.getFuelConsumptionDuty()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					bhlReport.getFuelConsumptionDuty().add(new FuelConsumptionDuty(day, null, null, null, null, null, null, null, null));
					
				}
				Collections.sort(bhlReport.getFuelConsumptionDuty(), new Comparator<FuelConsumptionDuty>() {
					@Override
					public int compare(FuelConsumptionDuty d1, FuelConsumptionDuty d2) {
						return d1.getDay().compareTo(d2.getDay());
					}
				});
				
			}
			
			//Fuel Consumption Excavation
			if (daysDifference > bhlReport.getFuelConsumptionExcavation().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (FuelConsumptionExcavation dutyCycleBhl : bhlReport.getFuelConsumptionExcavation()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					bhlReport.getFuelConsumptionExcavation().add(new FuelConsumptionExcavation(day, null, null, null, null, null, null));
					
				}
				Collections.sort(bhlReport.getFuelConsumptionExcavation(), new Comparator<FuelConsumptionExcavation>() {
					@Override
					public int compare(FuelConsumptionExcavation d1, FuelConsumptionExcavation d2) {
						return d1.getDay().compareTo(d2.getDay());
					}
				});
				
			}
			
			// Distance Roading
			if (daysDifference > bhlReport.getDistanceRoading().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (DistanceTraveledRoading dutyCycleBhl : bhlReport.getDistanceRoading()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					bhlReport.getDistanceRoading().add(new DistanceTraveledRoading(day, null));
					
				}
				Collections.sort(bhlReport.getDistanceRoading(), new Comparator<DistanceTraveledRoading>() {
					@Override
					public int compare(DistanceTraveledRoading d1, DistanceTraveledRoading d2) {
						return d1.getDay().compareTo(d2.getDay());
					}
				});
				
			}
			
			//Average Roading
			if (daysDifference > bhlReport.getAverageRoading().size()) {
				List<Date> list = utilities.getDateMap(startDate, endDate);
				for (AverageSpeedRoading dutyCycleBhl : bhlReport.getAverageRoading()) {
					list.remove(dutyCycleBhl.getDay());
				}
				for (Date day : list) {
					bhlReport.getAverageRoading().add(new AverageSpeedRoading(day, null));
					
				}
				Collections.sort(bhlReport.getAverageRoading(), new Comparator<AverageSpeedRoading>() {
					@Override
					public int compare(AverageSpeedRoading d1, AverageSpeedRoading d2) {
						return d1.getDay().compareTo(d2.getDay());
					}
				});
				
			}
			
			//Excavation Mode Summary
			MachineExcavationMode machineExcavationMode = machineBHLDataRepo.getExcavationModes(vin, startDate, endDate);
			if(machineExcavationMode!=null && machineExcavationMode.getActiveModeHrs()!=null && machineExcavationMode.getEconomyModeHrs()!=null && machineExcavationMode.getPowerModeHrs()!=null) {
			  machineExcavationMode.setActiveHrs((int)  Math.round(machineExcavationMode.getActiveModeHrs() / (machineExcavationMode.getActiveModeHrs() + machineExcavationMode.getEconomyModeHrs() + machineExcavationMode.getPowerModeHrs()) * 100));
			  machineExcavationMode.setEconomyHrs((int) Math.round(machineExcavationMode.getEconomyModeHrs() / (machineExcavationMode.getActiveModeHrs() + machineExcavationMode.getEconomyModeHrs() + machineExcavationMode.getPowerModeHrs()) * 100));
			  machineExcavationMode.setPowerHrs((int) Math.round(machineExcavationMode.getEconomyModeHrs() / (machineExcavationMode.getActiveModeHrs() + machineExcavationMode.getEconomyModeHrs() + machineExcavationMode.getPowerModeHrs()) * 100));
			}
			 
			//Duty Cycle Summary
			MachineDutyCycle machineDutyCycle = machineBHLDataRepo.getMachineDutyCycle(vin, startDate, endDate);
			
			bhlReport.setFuelConsumption(bhlFuelConsumption);
			bhlReport.setDailyFuelAverage(averageFuelConsumption);
			bhlReport.setMachineCompassBHLList(machineCompassBHLList);
			bhlReport.setGearTimeSpentBHLList(gearTimeSpentBHLList);
			bhlReport.setMachineDutyCycle(machineDutyCycle);
			bhlReport.setMachineExcavationMode(machineExcavationMode);
			
			long fuelConsumptionCount = bhlFuelConsumption.stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long averageConsumptionCount = averageFuelConsumption.stream().filter(e ->  e.getTotalFuelUsed()==null).count();
			long fuelConsumptionDutyCount = bhlReport.getFuelConsumptionDuty().stream().filter(f->f.getExcavationMode()==null && 
					f.getExcavationModePercentage()==null && f.getIdleMode()==null && f.getIdleModePercentage()==null && f.getLoadingMode()==null && f.getLoadingModePercentage()==null && f.getRoadindMode()==null && f.getRoadindModePercentage()==null).count();
			long fuelConsumptionExcavationCount = bhlReport.getFuelConsumptionExcavation().stream().filter(f->f.getEconomyMode()==null && f.getEconomyModePercentage()==null && f.getPlusMode()==null && f.getPlusModePercentage()==null && f.getStandardMode()==null && f.getStandardModePercentage()==null ).count();
			long distanceRoadingCount = bhlReport.getDistanceRoading().stream().filter(e ->  e.getDistanceTraveledRoading()==null).count();
			long averageRoadingCount = bhlReport.getAverageRoading().stream().filter(e ->  e.getAverageSpeedRoading()==null).count();
			long machineCompassCount = machineCompassBHLList.stream().filter(e ->  e.getForwardDirection()==null && e.getNeutralDirection()==null && e.getReverseDirection()==null).count();
			long gearTimeSpentCount = gearTimeSpentBHLList.stream().filter(e ->  e.getFirstGear()==null && e.getSecoundGear()==null && e.getThirdGear()==null && e.getForthGear()==null).count();
			List<AdvanceReportChart>  chartList =new ArrayList<>();
			 
			if(bhlFuelConsumption!=null && !bhlFuelConsumption.isEmpty() && fuelConsumptionCount!=bhlFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("WlsFuelConsumption"));
			}
			if(averageFuelConsumption!=null && !averageFuelConsumption.isEmpty() && averageConsumptionCount!=averageFuelConsumption.size()) {
				chartList.add(new AdvanceReportChart("AverageFuelConsumption"));
			}
			if(bhlReport.getFuelConsumptionDuty()!=null && !bhlReport.getFuelConsumptionDuty().isEmpty() && fuelConsumptionDutyCount!=bhlReport.getFuelConsumptionDuty().size()) {
				chartList.add(new AdvanceReportChart("FuelConsumptionDuty"));
			}
			if(bhlReport.getFuelConsumptionExcavation()!=null && !bhlReport.getFuelConsumptionExcavation().isEmpty() && fuelConsumptionExcavationCount!=bhlReport.getFuelConsumptionExcavation().size()) {
				chartList.add(new AdvanceReportChart("FuelConsumptionExcavation"));
			}
			if(bhlReport.getDistanceRoading()!=null && !bhlReport.getDistanceRoading().isEmpty() && distanceRoadingCount!=bhlReport.getDistanceRoading().size()) {
				chartList.add(new AdvanceReportChart("DistanceRoading"));
			}
			if(bhlReport.getAverageRoading()!=null && !bhlReport.getAverageRoading().isEmpty() && averageRoadingCount!=bhlReport.getAverageRoading().size()) {
				chartList.add(new AdvanceReportChart("AverageRoading"));
			}
			if(machinePowerBand!=null && machinePowerBand.getHighSpeed()!=null && machinePowerBand.getLowSpeed()!=null && machinePowerBand.getMediumSpeed()!=null) {
				chartList.add(new AdvanceReportChart("MachinePowerBand"));
			}
			if(bhlReport.getMachineDutyCycle().getAttachment()!=null && bhlReport.getMachineDutyCycle().getExcavation()!=null && bhlReport.getMachineDutyCycle().getIdling()!=null && bhlReport.getMachineDutyCycle().getLoading()!=null && bhlReport.getMachineDutyCycle().getRoading()!=null) {
				chartList.add(new AdvanceReportChart("DutyCycleBHLList"));
				}
			if(bhlReport.getMachineCompassBHLList()!=null && !bhlReport.getMachineCompassBHLList().isEmpty() && machineCompassCount!=bhlReport.getMachineCompassBHLList().size()) {
				chartList.add(new AdvanceReportChart("MachineCompassBHLList"));
			}
			if(bhlReport.getGearTimeSpentBHLList()!=null && !bhlReport.getGearTimeSpentBHLList().isEmpty() && gearTimeSpentCount!=bhlReport.getGearTimeSpentBHLList().size()) {
				chartList.add(new AdvanceReportChart("GearTimeSpentBHLList"));
			}
			response.setChartList(chartList);
			response.setBhlReport(bhlReport);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public PdfReportData getPdfReport(String vin, Date startdate, Date enddate) {
		PdfS3Reponse pdfResponse = new PdfS3Reponse();
		try {

			String fileName = vin+".pdf";
			File file = new File(fileName);

			FileOutputStream outputStream = new FileOutputStream(file);
			generatePdf(outputStream,vin,startdate,enddate);
			
			String mimeType = URLConnection.guessContentTypeFromName(fileName);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			try {
				String filePath  ="/Users/vi20475016/Documents/Docs/PDF_Response/" + fileName;
				pdfResponse.setFilePath(filePath);
				pdfResponse.setFileName(fileName);
				log.info("pdf File Uploaded in S3 Path : "+filePath);
				
				//Analystics Data
				Date date = new Date();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				date = utilities.getDate(df.format(date));
				Long count = pdfAnalyticsDataRepo.countByVinAndDay(vin, date);
				if (count != 0) {
					PdfAnalyticsData analyticsData = pdfAnalyticsDataRepo.findByVin(vin, date);
					Long rowCount = analyticsData.getCount() + 1;
					pdfAnalyticsDataRepo.updateCountByVinAndDate(rowCount, vin, date);
				} else {
					PdfAnalyticsData analyticsData = new PdfAnalyticsData();
					analyticsData.setCount(1);
					analyticsData.setDay(date);
					analyticsData.setVin(vin);
					pdfAnalyticsDataRepo.save(analyticsData);
				}
				log.info("AnalyticsData added in table");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception occured for PDF Report API :"+vin+"Exception -"+e.getMessage());
			}
			
			}catch(Exception e)
		{
			e.printStackTrace();
		}
		return pdfResponse;
	}
	
	private void generatePdf(FileOutputStream outputStream, String vin,Date startdate,Date enddate) throws IOException {
		 
		try {
			final PdfReportResponse pdfReportResponse =  getPdfJsonData(vin,startdate,enddate);
			Rectangle pageSize = new Rectangle(1080, 3150);
			
			if(pdfReportResponse.getMachineDetails().getPlatform()!=null && pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("BHL")) {
				pageSize = new Rectangle(1080, 5000);
			}
			if(pdfReportResponse.getMachineDetails().getPlatform()!=null && pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("EXCAVATOR"))
				pageSize = new Rectangle(1080, 3450);
			if(pdfReportResponse.getMachineDetails().getPlatform()!=null && (pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("CANCOMPACTOR") ||pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("INTELLICOMPACTOR") || pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("CANTELEHANDLER"))) {
                pageSize = new Rectangle(1080, 3150);
			}
            if(pdfReportResponse.getMachineDetails().getPlatform()!=null && pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("-")) {
				pageSize = new Rectangle(1080, 3150);
			}
			
			Document document = new Document(pageSize);
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			// Add Header and Footer
			// PDF Width and Hight
			HeaderPageEvent event = new HeaderPageEvent(IMAGELOACATION,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse.getMachineDetails().getPlatform());
			
			writer.setPageEvent(event);
			// write to document
			document.open();

			// header spacing
			brakeSpace(document);
			brakeSpace(document);
			brakeSpace(document);
			brakeSpace(document);

			/**
			 * Customer and Machine details
			 */
			log.info("Start Chart");
			customerAndMachineDetails(document,pdfReportResponse.getMachineDetails());
			
			if(pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("BHL") || pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("HBBHL")) {
				if(pdfReportResponse.getChartList()!=null && !pdfReportResponse.getChartList().isEmpty())
				 {
					 log.info("BHL Report  : "+pdfReportResponse.getChartList().size());
					 bhlReport(document, writer,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse,pdfReportResponse.getChartList());
				 }
			}
			
			if(pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("WLS")) {
				 if(pdfReportResponse.getChartList()!=null && !pdfReportResponse.getChartList().isEmpty())
				 {
					 log.info("WLS Report  : "+pdfReportResponse.getChartList().size());
					 wlsReport(document, writer,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse,pdfReportResponse.getChartList());
				 }
			}
			
			if(pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("EXCAVATOR") || pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("HBEXCAVATOR")) {
				 if(pdfReportResponse.getChartList()!=null && !pdfReportResponse.getChartList().isEmpty())
				 {
					 log.info("Excavator Report :  "+pdfReportResponse.getChartList().size());
					 excavatorreport(document, writer,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse,pdfReportResponse.getChartList());
				 }
			}
			
			if(pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("CANCOMPACTOR")) {
				 if(pdfReportResponse.getChartList()!=null && !pdfReportResponse.getChartList().isEmpty())
				 {
					 log.info("Compactor Report :  "+pdfReportResponse.getChartList().size());
					 compactorReport(document, writer,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse,pdfReportResponse.getChartList());
				 }
				
			}
			
			if(pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("CANTELEHANDLER") || pdfReportResponse.getMachineDetails().getPlatform().equalsIgnoreCase("INTELLICOMPACTOR")) {
				 if(pdfReportResponse.getChartList()!=null && !pdfReportResponse.getChartList().isEmpty())
				 {
					 log.info("Telehander Report :  "+pdfReportResponse.getChartList().size());
					 telehandlerReport(document, writer,pdfReportResponse.getFromDate(),pdfReportResponse.getToDate(),pdfReportResponse,pdfReportResponse.getChartList());
				 }
			}
			
			alertAndServiceDetails(document,pdfReportResponse.getMachineDetails(),pdfReportResponse.getMachineAlertDetails(),pdfReportResponse.getTodayDate(),pdfReportResponse.getUtilizationDetails().getAlertCount(),pdfReportResponse.getUtilizationDetails());
			fuelSummaryDetails(document,writer,pdfReportResponse.getMachineFuelStatus(),pdfReportResponse.getFuelHistoryDayDataList(),pdfReportResponse.getMachineEngineStatus());
			engineOnOffSummary(document,writer,pdfReportResponse.getEngineHistoryDayDataList(),pdfReportResponse.getMachineEngineStatus());
			utilizationSummaryDetails(document,pdfReportResponse.getUtilizationDetails(),pdfReportResponse.getDayUtilization(),pdfReportResponse.getMachineWorkingIdleStatus());
			
			if(pdfReportResponse.getBhlReport().getMachineExcavationMode()!=null) {
				excavationModeSummary(document,pdfReportResponse.getBhlReport().getMachineExcavationMode());
			}
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
			log.info("Exception occured for PDF Report API While generating report :"+vin+"Exception -"+e.getMessage());
		}
		
	}
	
	private void excavationModeSummary(Document document, MachineExcavationMode machineExcavationMode)
			throws IOException, DocumentException {

		PdfPTable excavationDetailsTable = new PdfPTable(1);
		excavationDetailsTable.setWidthPercentage(TABLEWIDTH);
		excavationDetailsTable.setSpacingBefore(4);

		DecimalFormat dec = new DecimalFormat("#0.00");
		
		// Table header
		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("EXCAVATION MODE SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);

		// Inner Header
		PdfPCell innerHeader = new PdfPCell();
		innerHeader.setPaddingLeft(18);
		innerHeader.setPaddingTop(20);
		innerHeader.setBorder(0);
		Paragraph innerTitle = new Paragraph();
		innerTitle.setAlignment(Element.ALIGN_LEFT);
		innerTitle
				.add(new Phrase("Excavation hours are further categorized into Economy hrs, Plus hrs and Standard hrs.",
						new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, new BaseColor(39, 39, 42))));
		innerHeader.addElement(innerTitle);

		PdfPTable culativeContentTable = new PdfPTable(2);
		float[] columnWidths2 = new float[] { 25f, 50f };
		try {
			culativeContentTable.setWidths(columnWidths2);
		} catch (DocumentException e1) {

			e1.printStackTrace();
		}
		culativeContentTable.setWidthPercentage(100);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		// headerCell.setCellEvent(new CellBorderEvent());
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Eco Mode Hrs ",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		culativeContentTable.addCell(headerCell);

		PdfPCell headerCell1 = new PdfPCell();
		headerCell1.setBorder(0);
		headerCell1.setPaddingLeft(20);
		headerCell1.setPaddingTop(8);
		Integer ecoValues = 0, powerValues = 0, activeValues = 0;
		if (machineExcavationMode.getEconomyHrs() != null && machineExcavationMode.getEconomyHrs() > 90) {
			ecoValues = machineExcavationMode.getEconomyHrs() - 15;
		} else {
			ecoValues = machineExcavationMode.getEconomyHrs();
		}

		if (machineExcavationMode.getPowerHrs() != null && machineExcavationMode.getPowerHrs() > 90) {
			powerValues = machineExcavationMode.getPowerHrs() - 15;
		} else {
			powerValues = machineExcavationMode.getPowerHrs();
		}
		if (machineExcavationMode.getActiveHrs() != null && machineExcavationMode.getActiveHrs() > 90) {
			activeValues = machineExcavationMode.getActiveHrs() - 15;
		} else {
			activeValues = machineExcavationMode.getActiveHrs();
		}

		String eco = "<div class=\"container\" style=\"padding-top:6px;display:table\"> <div class=\"skills html \" style=\"position: relative;float:left;display: table-cell;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:5px;transform: translateY(10px);padding-top:-10px\">"
				+ machineExcavationMode.getEconomyHrs() + "%</div></div>";
		String plus = "<div class=\"container\" style=\"padding-top:6px;display:table\"> <div class=\"skills html \" style=\"position: relative;float:left;display: table-cell;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:5px;transform: translateY(10px);padding-top:-10px\">"
				+ machineExcavationMode.getPowerHrs() + "%</div></div>";
		String std = "<div class=\"container\" style=\"padding-top:6px;display:table\"> <div class=\"skills html \" style=\"position: relative;float:left;display: table-cell;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:5px;transform: translateY(10px);padding-top:-10px\">"
				+ machineExcavationMode.getActiveHrs() + "%</div></div>";
		String ecoCSS = ".html {width: " + ecoValues
				+ "%; background-color: #f5bf17;} .container {width: 70%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";
		String plusCSS = ".html {width: " + powerValues
				+ "%; background-color: #f5bf17;} .container {width: 70%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";
		String stdCSS = ".html {width: " + activeValues
				+ "%; background-color: #f5bf17;} .container {width: 70%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";

		for (Element e : XMLWorkerHelper.parseToElementList(eco, ecoCSS)) {
			headerCell1.addElement(e);
		}

		culativeContentTable.addCell(headerCell1);

		PdfPCell emptyCell1 = new PdfPCell();
		emptyCell1.setBorder(0);
		emptyCell1.setPaddingLeft(20);
		emptyCell1.setPadding(10);
		emptyCell1.setPaddingBottom(10);
		Paragraph emptyText1 = new Paragraph();
		emptyText1.setAlignment(Element.ALIGN_LEFT);
		if (machineExcavationMode.getEconomyModeHrs() != null) {
			emptyText1.add(new Phrase(dec.format(machineExcavationMode.getEconomyModeHrs()),
					new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			emptyText1.add("-");
		}

		emptyCell1.addElement(emptyText1);

		culativeContentTable.addCell(emptyCell1);

		emptyCell1 = new PdfPCell();
		emptyCell1.setColspan(2);
		emptyCell1.setBorder(0);

		culativeContentTable.addCell(emptyCell1);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		// headerCell.setCellEvent(new CellBorderEvent());
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Plus Mode Hrs ",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		culativeContentTable.addCell(headerCell);

		headerCell1 = new PdfPCell();
		headerCell1.setBorder(0);
		headerCell1.setPaddingLeft(20);
		headerCell1.setPaddingTop(8);

		for (Element e : XMLWorkerHelper.parseToElementList(plus, plusCSS)) {
			headerCell1.addElement(e);
		}

		culativeContentTable.addCell(headerCell1);

		emptyCell1 = new PdfPCell();
		emptyCell1.setBorder(0);
		emptyCell1.setPaddingLeft(20);
		emptyCell1.setPadding(10);
		emptyCell1.setPaddingBottom(10);
		emptyText1 = new Paragraph();
		emptyText1.setAlignment(Element.ALIGN_LEFT);
		if (machineExcavationMode.getPowerModeHrs() != null) {
			emptyText1.add(new Phrase(dec.format(machineExcavationMode.getPowerModeHrs()),
					new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			emptyText1.add("-");
		}

		emptyCell1.addElement(emptyText1);

		culativeContentTable.addCell(emptyCell1);

		emptyCell1 = new PdfPCell();
		emptyCell1.setBorder(0);
		culativeContentTable.addCell(emptyCell1);
		// 3rd

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Std Mode Hrs ",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		culativeContentTable.addCell(headerCell);

		headerCell1 = new PdfPCell();
		headerCell1.setBorder(0);
		headerCell1.setPaddingLeft(20);
		headerCell1.setPaddingTop(8);

		for (Element e : XMLWorkerHelper.parseToElementList(std, stdCSS)) {
			headerCell1.addElement(e);
		}

		culativeContentTable.addCell(headerCell1);

		emptyCell1 = new PdfPCell();
		emptyCell1.setBorder(0);
		emptyCell1.setPaddingLeft(20);
		emptyCell1.setPadding(10);
		emptyCell1.setPaddingBottom(10);
		emptyText1 = new Paragraph();
		emptyText1.setAlignment(Element.ALIGN_LEFT);
		if (machineExcavationMode.getActiveModeHrs() != null) {
			emptyText1.add(new Phrase(dec.format(machineExcavationMode.getActiveModeHrs()),
					new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			emptyText1.add("-");
		}

		emptyCell1.addElement(emptyText1);

		culativeContentTable.addCell(emptyCell1);

		emptyCell1 = new PdfPCell();
		emptyCell1.setBorder(0);
		culativeContentTable.addCell(emptyCell1);

		PdfPCell Content = new PdfPCell();
		Content.setPadding(15);
		Content.setBorder(0);
		Content.addElement(culativeContentTable);

		// Footer

		PdfPCell footerCell = new PdfPCell();
		footerCell.setBorder(0);
		footerCell.setPaddingTop(7f);
		footerCell.setBackgroundColor(new BaseColor(255, 249, 229));
		Paragraph footerText = new Paragraph();
		footerText.setAlignment(Element.ALIGN_LEFT);
		footerText.add(new Phrase(
				" Eco mode reduces fuel consumption. Use Eco mode for low fuel consumption and plus mode for better productivity.",
				new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(255, 146, 0))));
		footerCell.addElement(footerText);

		Image lightIcon = Image.getInstance(IMAGELOACATION + "/lightbulbicon.png");
		lightIcon.setAlignment(Image.ALIGN_RIGHT);
		lightIcon.scalePercent(50);

		PdfPCell leftCell = new PdfPCell();
		leftCell.setBackgroundColor(new BaseColor(255, 249, 229));
		leftCell.setPaddingTop(10f);
		leftCell.setBorder(0);
		leftCell.setPaddingTop(4);
		leftCell.addElement(lightIcon);

		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 5f, 95f });
		footerTable.addCell(leftCell);
		footerTable.addCell(footerCell);

		PdfPCell footerContent = new PdfPCell();
		footerContent.setPadding(5);
		footerContent.setBorder(0);
		footerContent.addElement(footerTable);

		excavationDetailsTable.addCell(header);
		excavationDetailsTable.addCell(innerHeader);
		excavationDetailsTable.addCell(Content);
		excavationDetailsTable.addCell(footerContent);
		excavationDetailsTable.setTableEvent(new BorderEvent());
		document.add(excavationDetailsTable);

	}
	
	private void utilizationSummaryDetails(Document document, UtilizationDetails utilizationDetails,
			List<DailyAverageUtilizationData> dayUtilization, MachineWorkingIdleStatus machineWorkingIdleStatus)
			throws IOException, DocumentException {

		PdfPTable utilizationDetailsTable = new PdfPTable(1);
		utilizationDetailsTable.setWidthPercentage(TABLEWIDTH);
		utilizationDetailsTable.setSpacingBefore(4);

		DecimalFormat dec = new DecimalFormat("#0.00");

		DecimalFormat DOUBLE_FORMAT = new DecimalFormat(".##");
		// Table header
		PdfPCell header = new PdfPCell();
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("UTILISATION SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);

		// Table inner content
		PdfPCell content = new PdfPCell();
		content.setPadding(10);
		content.setBorder(0);

		// splitting table to 3 columns
		PdfPTable contentTable = new PdfPTable(2);
		contentTable.setWidthPercentage(100);

		contentTable.addCell(setContent("Utilisation hrs last 7 days",
				utilizationDetails.getUtilization() != null ? dec.format(utilizationDetails.getUtilization()) : null));
		contentTable.addCell(setContent("Utilisation hrs till date",
				utilizationDetails.getUtilizationTill() != null
						? DOUBLE_FORMAT.format(utilizationDetails.getUtilizationTill())
						: null));
		contentTable.addCell(setContent("Idle hrs last 7 days",
				utilizationDetails.getIdleHrs() != null ? dec.format(utilizationDetails.getIdleHrs()) : null));
		contentTable.addCell(setContent("Idle hrs(%)",
				utilizationDetails.getIdlePercentage() != null ? utilizationDetails.getIdlePercentage() + "%" : null));
		content.addElement(contentTable);

		PdfPCell daywiseContent = new PdfPCell();
		daywiseContent.setPaddingLeft(10);
		daywiseContent.setBorder(0);
		Paragraph innerTitle = new Paragraph();
		innerTitle.setAlignment(Element.ALIGN_LEFT);
		innerTitle.add(new Phrase("Day wise utilization",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(39, 39, 42))));
		daywiseContent.addElement(innerTitle);

		PdfPCell wayContent = new PdfPCell();
		wayContent.setPadding(10);
		wayContent.setBorder(0);

		log.info("dayUtilization size" + dayUtilization.size());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		// splitting table to 8 columns
		PdfPTable waywiseContentTable = new PdfPTable(8);
		waywiseContentTable.setWidths(new float[] { 13f, 10f, 10f, 10f, 10f, 10f, 10f, 10f });
		waywiseContentTable.setWidthPercentage(100);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(Rectangle.BOTTOM);
		headerCell.setBorderWidthBottom(4f);
		headerCell.setBorderColorBottom(BaseColor.WHITE);
		headerCell.setPadding(9);
		headerCell.setFixedHeight(45f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(
				new Phrase("| Date ", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		waywiseContentTable.addCell(headerCell);

		for (int i = 0; i < dayUtilization.size(); i++) {
			headerCell = new PdfPCell();
			headerCell.setPaddingLeft(20);
			headerCell.setPaddingTop(10);
			headerCell.setBorderWidthTop(1f);
			headerCell.setBorderWidthLeft(1f);
			headerCell.setBorderWidthRight(1f);
			headerCell.setBorderWidthBottom(1f);
			headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
			headerCell.setBorderColorBottom(new BaseColor(231, 231, 231));
			headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
			headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
			Paragraph valueText = new Paragraph();
			valueText.setAlignment(Element.ALIGN_CENTER);
			valueText.add(new Phrase(dateformat.format(dayUtilization.get(i).getDay()),
					new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(29, 36, 38))));
			headerCell.addElement(valueText);

			waywiseContentTable.addCell(headerCell);
		}

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		headerCell.setPaddingTop(17f);
		headerCell.setBorder(Rectangle.TOP);
		headerCell.setBorderWidthTop(4f);
		headerCell.setBorderColorTop(BaseColor.WHITE);
		headerCell.setFixedHeight(45f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Utilization hrs",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		waywiseContentTable.addCell(headerCell);

		for (int i = 0; i < dayUtilization.size(); i++) {
			headerCell = new PdfPCell();

			headerCell.setPaddingLeft(20);
			headerCell.setPaddingTop(10);
			headerCell.setBorderWidthLeft(1f);
			headerCell.setBorderWidthBottom(1f);
			headerCell.setBorderWidthTop(1f);
			headerCell.setBorderWidthRight(1f);
			headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
			headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
			headerCell.setBorderColorBottom(new BaseColor(255, 255, 255));
			headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
			Paragraph valueText = new Paragraph();
			valueText.setAlignment(Element.ALIGN_CENTER);
			if (dayUtilization.get(i).getWorkingHours() != null) {
				valueText.add(new Phrase(dec.format(dayUtilization.get(i).getWorkingHours()),
						new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(29, 36, 38))));
			} else {
				valueText.add("");
			}

			headerCell.addElement(valueText);

			waywiseContentTable.addCell(headerCell);
		}

		wayContent.addElement(waywiseContentTable);

		// Cumulative Idle/working distribution

		PdfPCell cumulativeContent = new PdfPCell();
		cumulativeContent.setPaddingLeft(10);
		cumulativeContent.setBorder(0);
		Paragraph cumulativeTitle = new Paragraph();
		cumulativeTitle.setAlignment(Element.ALIGN_LEFT);
		cumulativeTitle.add(new Phrase("Cumulative Idle and Working time distribution for the last 7 days",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(39, 39, 42))));
		cumulativeContent.addElement(cumulativeTitle);

		PdfPTable culativeContentTable = new PdfPTable(2);
		float[] columnWidths2 = new float[] { 10f, 45f };
		try {
			culativeContentTable.setWidths(columnWidths2);
		} catch (DocumentException e1) {

			e1.printStackTrace();
		}
		culativeContentTable.setWidthPercentage(100);

		headerCell = new PdfPCell();
		headerCell.setBorder(Rectangle.BOTTOM);
		headerCell.setBorderWidthBottom(4f);
		headerCell.setBorderColorBottom(BaseColor.WHITE);
		headerCell.setPadding(9);
		// headerCell.setCellEvent(new CellBorderEvent());
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("Idle Time Hrs ",
				new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		culativeContentTable.addCell(headerCell);

		PdfPCell headerCell1 = new PdfPCell();
		headerCell1.setBorder(0);
		headerCell1.setPaddingLeft(20);
		headerCell1.setPaddingTop(10);

		String on = "<div class=\"container\" style=\"padding-top:6px;\"> <div class=\"skills html \" style=\"position: relative;float:left;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:15px;transform: translateY(10px);padding-top:-10px\">"
				+ dec.format(machineWorkingIdleStatus.getIdleHrs()) + "</div></div>";
		String off = "<div class=\"container\" style=\"padding-top:6px;\"> <div class=\"skills html \" style=\"position: relative;float:left;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:15px;transform: translateY(10px);padding-top:-10px\">"
				+ dec.format(machineWorkingIdleStatus.getWorkingHrs()) + "</div></div>";
		String onCSS = ".html {width: " + machineWorkingIdleStatus.getIdleHrs()
				+ "%; background-color: #f5bf17;} .container {width: 50%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";
		String offCSS = ".html {width: " + machineWorkingIdleStatus.getWorkingHrs()
				+ "%; background-color: #f5bf17;} .container {width: 50%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";

		for (Element e : XMLWorkerHelper.parseToElementList(on, onCSS)) {
			headerCell1.addElement(e);
		}

		culativeContentTable.addCell(headerCell1);

		headerCell = new PdfPCell();
		headerCell.setBorder(Rectangle.TOP);
		headerCell.setBorderWidthTop(4f);
		headerCell.setBorderColorTop(BaseColor.WHITE);
		headerCell.setPadding(9);
		headerCell.setPaddingTop(13f);
		// headerCell.setCellEvent(new CellBorderEvent());
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("Working Time Hrs ",
				new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		culativeContentTable.addCell(headerCell);

		headerCell1 = new PdfPCell();
		headerCell1.setBorder(0);
		headerCell1.setPaddingLeft(20);
		headerCell1.setPaddingTop(10);

		for (Element e : XMLWorkerHelper.parseToElementList(off, offCSS)) {
			headerCell1.addElement(e);
		}

		culativeContentTable.addCell(headerCell1);

		PdfPCell Content = new PdfPCell();
		Content.setPadding(10);
		Content.setBorder(0);
		Content.addElement(culativeContentTable);

		PdfPCell footerContent = new PdfPCell();
		// footerContent.setPadding(7);
		footerContent.setBorder(0);
		footerContent.setPaddingTop(10f);
		footerContent.setFixedHeight(40f);
		footerContent.setBackgroundColor(new BaseColor(255, 249, 229));
		Paragraph footerText = new Paragraph();
		footerText.setAlignment(Element.ALIGN_LEFT);
		footerText.add(new Phrase(
				"Fuel can be saved by reducing idle time.Engine can be switched off when idling time is high.",
				new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(255, 146, 0))));
		footerContent.addElement(footerText);

		Image jcbImage = Image.getInstance(IMAGELOACATION + "/lightbulbicon.png");
		jcbImage.setAlignment(Image.ALIGN_LEFT);
		jcbImage.scalePercent(50);

		PdfPCell leftCell = new PdfPCell();
		// leftCell.setPadding(7);
		leftCell.setPaddingTop(10f);
		leftCell.setBackgroundColor(new BaseColor(255, 249, 229));
		leftCell.setBorder(0);
		leftCell.addElement(jcbImage);

		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 5f, 95f });
		footerTable.addCell(leftCell);
		footerTable.addCell(footerContent);

		PdfPCell newContent = new PdfPCell();
		newContent.setPadding(5);
		newContent.setBorder(0);

		newContent.addElement(footerTable);

		utilizationDetailsTable.addCell(header);
		utilizationDetailsTable.addCell(content);
		utilizationDetailsTable.addCell(daywiseContent);
		utilizationDetailsTable.addCell(wayContent);
		utilizationDetailsTable.addCell(cumulativeContent);
		utilizationDetailsTable.addCell(Content);
		utilizationDetailsTable.addCell(newContent);
		utilizationDetailsTable.setTableEvent(new BorderEvent());
		try {
			document.add(utilizationDetailsTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
	
	private void engineOnOffSummary(Document document, PdfWriter writer,
			List<EngineHistoryDataListV2> engineHistoryDayDataList, MachineEngineStatus machineEngineStatus)
			throws IOException, DocumentException {

		PdfPTable engineOnOffTable = new PdfPTable(1);
		engineOnOffTable.setWidthPercentage(TABLEWIDTH);
		engineOnOffTable.setSpacingBefore(4);

		// Table header
		PdfPCell header = new PdfPCell();
		header.setPadding(15);
		header.setBorder(0);

		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("ENGINE ON/OFF SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);

		// Chart Header
		PdfPCell chartHeader = new PdfPCell();
		chartHeader.setPaddingTop(5);
		chartHeader.setPaddingLeft(20);
		chartHeader.setBorder(0);
		Paragraph alertTitle = new Paragraph();
		alertTitle.setAlignment(Element.ALIGN_LEFT);
		alertTitle.add(new Phrase("Day wise engine on/off time for the last 7 days",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));

		chartHeader.addElement(alertTitle);

		PdfPCell chartCell = new PdfPCell();
		chartCell.setBorder(0);

		TaskSeriesCollection dataset = getGanttDataset(engineHistoryDayDataList);

		JFreeChart chart = ChartFactory.createGanttChart("", "", "", dataset, false, false, false);
		chart.setBackgroundPaint(Color.white);
		chart.setBackgroundPaint(new Color(255, 255, 255));
		chart.getPlot().setOutlinePaint(null);
		chart.getPlot().setOutlineStroke(null);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(231, 231, 231));
		plot.getRangeAxis().setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
		plot.getRangeAxis().setTickLabelPaint(Color.BLACK);
		plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
		plot.getDomainAxis().setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlineStroke(new BasicStroke(1F));
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setDomainGridlinePosition(CategoryAnchor.END);
		plot.getDomainAxis().setLowerMargin(0);
		plot.getDomainAxis().setCategoryMargin(0);
		plot.getDomainAxis().setUpperMargin(0);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setMaximumBarWidth(0.13f);
		renderer.setSeriesPaint(0, new Color(89, 227, 193));
		renderer.setDrawBarOutline(false);

		DateAxis dateAxis = (DateAxis) plot.getRangeAxis();
		DateTickUnit unit = new DateTickUnit(DateTickUnitType.HOUR, 1);
		DateFormat chartFormatter = new SimpleDateFormat("H");
		dateAxis.setDateFormatOverride(chartFormatter);
		dateAxis.setTickUnit(unit);
		dateAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
		dateAxis.setMaximumDate(date(1947, 0, 1, 23, 59, 59));
		dateAxis.setMinimumDate(date(1946, 11, 31, 23, 59, 59));
		dateAxis.setTickMarksVisible(true);

		BufferedImage ChartBufferedImage = chart.createBufferedImage(900, 195);
		Image ChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);

		chartCell.addElement(ChartImage);

		// Legend
		PdfPCell legend1 = new PdfPCell();
		legend1.setBorder(0);
		Paragraph legend1Text = new Paragraph();
		legend1Text.setAlignment(Element.ALIGN_LEFT);
		legend1Text.add(
				new Phrase("Engine ON", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(39, 39, 42))));
		legend1.addElement(legend1Text);

		PdfPCell legend2 = new PdfPCell();
		legend2.setBorder(0);
		Paragraph legend2Text = new Paragraph();
		legend2Text.setAlignment(Element.ALIGN_LEFT);
		legend2Text.add(new Phrase("Engine OFF",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(39, 39, 42))));
		legend2.addElement(legend2Text);

		Image onicon = Image.getInstance(IMAGELOACATION + "/Rectangle_1.png");
		onicon.setAlignment(Image.ALIGN_RIGHT);
		onicon.scalePercent(130);

		Image officon = Image.getInstance(IMAGELOACATION + "/Rectangle_2.png");
		officon.setAlignment(Image.ALIGN_RIGHT);
		officon.scalePercent(130);

		PdfPCell onCell = new PdfPCell();
		onCell.setBorder(0);
		onCell.setPaddingTop(5f);
		onCell.addElement(onicon);

		PdfPCell offCell = new PdfPCell();
		offCell.setPaddingLeft(8f);
		offCell.setPaddingTop(5f);
		offCell.setBorder(0);
		offCell.addElement(officon);

		PdfPTable footerTable = new PdfPTable(4);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 30f, 8f, 8f, 40f });
		footerTable.addCell(onCell);
		footerTable.addCell(legend1);
		footerTable.addCell(offCell);
		footerTable.addCell(legend2);

		PdfPCell newContent = new PdfPCell();
		newContent.setBorder(0);
		newContent.addElement(footerTable);

		// Cumulative Header

		PdfPCell alertHeader = new PdfPCell();
		alertHeader.setPaddingLeft(20);
		alertHeader.setBorder(0);
		alertHeader.setPaddingBottom(10f);
		alertTitle = new Paragraph();
		alertTitle.setAlignment(Element.ALIGN_LEFT);
		alertTitle.add(new Phrase("Cumulative engine on/off time for the last 7 days",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));
		alertHeader.addElement(alertTitle);

		// Cumulative Details

		PdfPTable culativeContentTable = new PdfPTable(2);
		culativeContentTable.setWidthPercentage(100);
		float[] columnWidths2 = new float[] { 10f, 60f };
		try {
			culativeContentTable.setWidths(columnWidths2);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		DecimalFormat dec = new DecimalFormat("#0.00");

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		headerCell.setFixedHeight(45f);
		headerCell.setBorder(Rectangle.BOTTOM);
		headerCell.setBorderWidthBottom(4f);
		headerCell.setBorderColorBottom(BaseColor.WHITE);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("Engine On Hrs ",
				new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		culativeContentTable.addCell(headerCell);

		PdfPCell engineValue = new PdfPCell();
		engineValue.setBorder(0);
		engineValue.setPaddingLeft(20);
		engineValue.setPaddingTop(10);
		Integer engineOn = 0;
		Integer engineOff = 0;
		if (machineEngineStatus.getEngineOn() != null && machineEngineStatus.getEngineOff() != null) {
			engineOn = (int) Math.round(machineEngineStatus.getEngineOn() / (machineEngineStatus.getEngineOn() + machineEngineStatus.getEngineOff()) * 100);
			engineOff = (int) Math.round(machineEngineStatus.getEngineOff() / (machineEngineStatus.getEngineOn() + machineEngineStatus.getEngineOff()) * 100);
		}
		log.info("data " + engineOn + " - " + engineOff + " - " + machineEngineStatus.getEngineOn() + " - "+ machineEngineStatus.getEngineOff());
		if (engineOn != 0 && engineOn > 80)
			engineOn = engineOn - 20;

		if (engineOff != 0 && engineOff > 80)
			engineOff = engineOff - 20;

		log.info("data " + engineOn + " - " + engineOff + " - " + machineEngineStatus.getEngineOn() + " - "
				+ machineEngineStatus.getEngineOff());
		String on = "<div class=\"container\" style=\"padding-top:6px;\"> <div class=\"skills html \" style=\"position: relative;float:left;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:15px;transform: translateY(10px);padding-top:-10px\">"
				+ dec.format(machineEngineStatus.getEngineOn()) + "</div></div>";
		String off = "<div class=\"container\" style=\"padding-top:6px;\"> <div class=\"skills html \" style=\"position: relative;float:left;height:22px;padding-top:15px;\"></div><div style=\"font-size: 25px;float:left;padding-left:15px;transform: translateY(10px);padding-top:-10px\">"
				+ dec.format(machineEngineStatus.getEngineOff()) + "</div></div>";
		String onCSS = ".html {width: " + engineOn
				+ "%; background-color: #f5bf17;} .container {width: 50%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";
		String offCSS = ".html {width: " + engineOff
				+ "%; background-color: #f5bf17;} .container {width: 50%;} .skills { text-align: right;padding-top: 1px;padding-bottom: 1px;color: white;}";

		for (Element e : XMLWorkerHelper.parseToElementList(on, onCSS)) {
			engineValue.addElement(e);
		}

		culativeContentTable.addCell(engineValue);

		headerCell = new PdfPCell();

		headerCell.setPadding(9);
		headerCell.setPaddingTop(13f);
		headerCell.setFixedHeight(45f);
		headerCell.setBorder(Rectangle.TOP);
		headerCell.setBorderWidthTop(4f);
		headerCell.setBorderColorTop(BaseColor.WHITE);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("Engine Off Hrs ",
				new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		culativeContentTable.addCell(headerCell);

		engineValue = new PdfPCell();
		engineValue.setBorder(0);
		engineValue.setPaddingLeft(20);
		engineValue.setPaddingTop(10f);

		for (Element e : XMLWorkerHelper.parseToElementList(off, offCSS)) {
			engineValue.addElement(e);
		}

		culativeContentTable.addCell(engineValue);

		PdfPCell Content = new PdfPCell();
		Content.setPaddingLeft(23);
		Content.setPaddingBottom(10);
		Content.setBorder(0);
		Content.addElement(culativeContentTable);

		engineOnOffTable.addCell(header);
		engineOnOffTable.addCell(chartHeader);
		engineOnOffTable.addCell(chartCell);
		engineOnOffTable.addCell(newContent);
		engineOnOffTable.addCell(alertHeader);
		engineOnOffTable.addCell(Content);
		engineOnOffTable.setTableEvent(new BorderEvent());
		try {
			document.add(engineOnOffTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
	
	private TaskSeriesCollection getGanttDataset(List<EngineHistoryDataListV2> engineHistoryDayDataList) {
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		final TaskSeries unAvailable = new TaskSeries("2022-01-17");

		if (engineHistoryDayDataList != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < engineHistoryDayDataList.size(); i++) {
				if (engineHistoryDayDataList.get(i).timestamps != null
						&& !engineHistoryDayDataList.get(i).timestamps.isEmpty()) {

					List<DateValue> engineData = engineHistoryDayDataList.get(i).timestamps;
					int dataSize = engineData.size();
					if (dataSize == 1) {
						Date d = engineData.get(0).getKey();
						log.info("d" + d);
						Task t0 = new Task(formatter.format(engineHistoryDayDataList.get(i).date),
								date(new Date(), 0, 0, 0), date(new Date(), 0, 0, 0));
						unAvailable.add(t0);

					} else {
						log.info(" chart start");
						final Task t4 = new Task(formatter.format(engineHistoryDayDataList.get(i).date),
								date(new Date(), 0, 0, 0), date(new Date(), 23, 59, 59));
						for (int j = 0; j < engineData.size() - 1; j++) {

							String index = "st1" + j;
							if (engineHistoryDayDataList.get(i).values.get(j).getVal() == 1) {
								Date d = engineData.get(j).getKey();
								Date d1 = engineData.get(j + 1).getKey();
								if (d1.getHours() == 00 && d1.getMinutes() == 00 && d1.getSeconds() == 00) {
									d1.setHours(23);
									d1.setMinutes(59);
									d1.setSeconds(59);
								}
								final Task stj = new Task(index, timeToDate(d), timeToDate(d1));
								t4.addSubtask(stj);

							} else {
								final Task stj = new Task(index, date(new Date(), 0, 0, 0), date(new Date(), 0, 0, 0));
								t4.addSubtask(stj);
							}
						}
						unAvailable.add(t4);

					}

				} else {
					final Task ti = new Task(formatter.format(engineHistoryDayDataList.get(i).date),
							date(new Date(), 0, 0, 0), date(new Date(), 0, 0, 0));
					unAvailable.add(ti);
				}

			}
			dataset.add(unAvailable);

		} else {
			log.info("No Data");
		}

		return dataset;
	}
	
	private Date date(int year, int month, int day, int hour, int minute, int second) {

		Date date = new Date();

		date.setDate(day);
		date.setMonth(month);
		date.setYear(year);
		date.setHours(hour);
		date.setMinutes(minute);
		date.setSeconds(second);
		final Calendar calendar = Calendar.getInstance();
		calendar.set(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(),
				date.getSeconds());
		log.info("calendar " + calendar.getTime());
		return calendar.getTime();

	}
	
	private Date date(Date date, int hour, int minute, int second) {

		date.setDate(1);
		date.setMonth(0);
		date.setYear(1947);
		date.setHours(hour);
		date.setMinutes(minute);
		date.setSeconds(second);
		final Calendar calendar = Calendar.getInstance();
		calendar.set(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(),
				date.getSeconds());
		return calendar.getTime();
	}

	private Date timeToDate(Date date) {

		date.setDate(1);
		date.setMonth(0);
		date.setYear(1947);
		final Calendar calendar = Calendar.getInstance();
		calendar.set(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), 0);
		return calendar.getTime();
	}
	
	private void fuelSummaryDetails(Document document, PdfWriter writer, MachineFuelHistory machineFuelHistory,
			List<FuelHistoryDataListV2> fuelHistoryDayDataList, MachineEngineStatus machineEngineStatus)
			throws IOException, DocumentException {

		PdfPTable fuelTable = new PdfPTable(1);
		fuelTable.setWidthPercentage(TABLEWIDTH);
		fuelTable.setSpacingBefore(4);

		// Table header
		PdfPCell header = new PdfPCell();
		header.setPadding(15);
		header.setBorder(0);

		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("FUEL SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);

		// Chart Header
		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(98);
		chartTable.setPaddingTop(10);
		chartTable.setWidths(new float[] { 40f, 60f });

		PdfPCell leftHeader = new PdfPCell();
		leftHeader.setPadding(9);
		leftHeader.setBorder(0);
		leftHeader.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph alertTitle = new Paragraph();
		alertTitle.setAlignment(Element.ALIGN_LEFT);
		Integer fuel = machineEngineStatus.getFuel() != null ? machineEngineStatus.getFuel() : 0;
		alertTitle.add(new Phrase("| Last know Fuel level(%)                         " + fuel + "%",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));
		leftHeader.addElement(alertTitle);
		chartTable.addCell(leftHeader);

		PdfPCell rightHeader = new PdfPCell();
		rightHeader.setPaddingRight(30);
		rightHeader.setPaddingTop(10);
		rightHeader.setBorder(0);
		alertTitle = new Paragraph();
		alertTitle.setAlignment(Element.ALIGN_RIGHT);
		
		/*
		 * if(machineFuelHistory!=null && machineFuelHistory.getDateTime()!=null) {
		 * alertTitle.add(new Phrase(lasttime.format(machineFuelHistory.getDateTime()),
		 * new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(39, 39,
		 * 42)))); }else {
		 */
		alertTitle.add("");

		rightHeader.addElement(alertTitle);
		chartTable.addCell(rightHeader);

		PdfPCell cellHeader = new PdfPCell();
		cellHeader.setPaddingTop(10);
		cellHeader.setBorder(0);
		cellHeader.addElement(chartTable);

		PdfPCell chartHeader = new PdfPCell();
		chartHeader.setPaddingLeft(20);
		chartHeader.setPaddingTop(10);
		chartHeader.setBorder(0);
		Paragraph chartTitle = new Paragraph();
		chartTitle.setAlignment(Element.ALIGN_LEFT);
		chartTitle.add(new Phrase("Fuel level % for last 7 days",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));
		chartHeader.addElement(chartTitle);

		PdfPCell fuelChart = new PdfPCell();
		fuelChart.setBorder(0);
		// fuelChart.setFixedHeight(290f);

		XYSeriesCollection dataset = new XYSeriesCollection();
		if (fuelHistoryDayDataList != null) {
			SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < fuelHistoryDayDataList.size(); i++) {

				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
				if (fuelHistoryDayDataList.get(i).values != null) {
					List<DoubleValue> fulData = fuelHistoryDayDataList.get(i).values;
					XYSeries dayi = new XYSeries(datetime.format(fuelHistoryDayDataList.get(i).date));
					for (int j = 0; j < fulData.size(); j++) {
						Double d1 = fulData.get(j).getVal();
						String timehrs = formatter.format(fuelHistoryDayDataList.get(i).timestamps.get(j).getKey());

						Double number = Double.parseDouble(timehrs.replace(":", "."));

						dayi.add(number, d1);

					}
					dataset.addSeries(dayi);
				}

			}
		} else {
			log.info("No data");
		}

		final JFreeChart chart = ChartFactory.createXYLineChart("", "Time (Hrs)", "Fuel %", dataset,
				PlotOrientation.VERTICAL, true, false, false);

		final XYPlot plot = chart.getXYPlot();
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		// renderer.setBaseShapesVisible(true);
		renderer.setDefaultShapesVisible(true);
		renderer.setSeriesShape(0, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(1, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(2, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(3, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(4, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(5, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesShape(6, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
		renderer.setSeriesPaint(0, new Color(154, 219, 61));
		renderer.setSeriesPaint(1, new Color(229, 221, 38));
		renderer.setSeriesPaint(2, new Color(16, 178, 183));
		renderer.setSeriesPaint(3, new Color(112, 16, 183));
		renderer.setSeriesPaint(4, new Color(183, 16, 155));
		renderer.setSeriesPaint(5, new Color(183, 16, 69));
		renderer.setSeriesPaint(6, new Color(23, 212, 168));
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(new Color(252, 252, 252));
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setDomainGridlinePaint(Color.BLACK);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		domain.setRange(0, 23);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setLabel("Time (Hrs)");
		domain.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 10));
		domain.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 10));
		domain.setTickLabelPaint(Color.BLACK);
		domain.setLabelPaint(Color.BLACK);

		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.TOP);
		legend.setWidth(100);
		legend.setFrame(BlockBorder.NONE);
		legend.setItemFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 10));
		legend.setItemPaint(Color.BLACK);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(0, 110);
		rangeAxis.setLabel("Fuel %");
		rangeAxis.setTickUnit(new NumberTickUnit(10));
		rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
		rangeAxis.setTickLabelPaint(Color.BLACK);
		rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
		rangeAxis.setLabelPaint(Color.BLACK);

		String imagePath = IMAGELOACATION + "/fuel_" + Instant.now().toEpochMilli() + ".png";
		File file = new File(imagePath);
		ChartUtils.writeChartAsPNG(new FileOutputStream(file), chart, 900, 250);
		Image image = Image.getInstance(imagePath);

		fuelChart.addElement(image);
		file.delete();

		fuelTable.addCell(header);
		fuelTable.addCell(cellHeader);
		fuelTable.addCell(chartHeader);
		fuelTable.addCell(fuelChart);
		fuelTable.setTableEvent(new BorderEvent());
		try {
			document.add(fuelTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
	
	private void alertAndServiceDetails(Document document, MachineDetails machineDetails,
			List<Alert> alertList,String todayDate, long count, UtilizationDetails utilizationDetails) throws DocumentException, MalformedURLException, IOException {

		PdfPTable customerAndMachineDetailsTable = new PdfPTable(1);
		customerAndMachineDetailsTable.setWidthPercentage(TABLEWIDTH);
		customerAndMachineDetailsTable.setSpacingBefore(4);

		// Table header
		PdfPCell header = new PdfPCell();
		header.setPadding(15);
		header.setBorder(0);
		
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		//header.setFixedHeight(50f);
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("ACTIVE ALERTS",new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		
		//Alert Header 
		PdfPCell alertHeader = new PdfPCell();
		alertHeader.setPaddingTop(10);
		alertHeader.setPaddingLeft(20);
		alertHeader.setBorder(0);
		Paragraph alertTitle = new Paragraph();
		alertTitle.setAlignment(Element.ALIGN_LEFT);
		alertTitle.add(new Phrase("Total "+count+" Active Alerts as on "+todayDate,
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));
		alertTitle.setLeading(15);
		alertHeader.addElement(alertTitle);
		
		// Alert Details
		PdfPTable alertTable = alertDetailsTable(alertList);
		
		PdfPCell alertContent = new PdfPCell();
		alertContent.setPadding(5);
		alertContent.setBorder(0);
		
		alertContent.addElement(alertTable);
		
		//Service Header 
		PdfPCell serviceHeader = new PdfPCell();
		serviceHeader.setPaddingLeft(20);
		serviceHeader.setBorder(0);
		Paragraph serviceTitle = new Paragraph();
		serviceTitle.setAlignment(Element.ALIGN_LEFT);
		serviceTitle.add(new Phrase("Service Status",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 39, 42))));
		serviceHeader.addElement(serviceTitle);
		
		// Table inner content
		PdfPCell content = new PdfPCell();
		content.setPadding(5);
		content.setBorder(0);

		// splitting table to 3 columns
		PdfPTable contentTable = new PdfPTable(2);
		contentTable.setWidthPercentage(100);
		
		SimpleDateFormat service = new SimpleDateFormat("dd/MM/yyyy");
		contentTable.addCell(setContent("Last Service Date", machineDetails.getLastService() !=null ? service.format(machineDetails.getLastService()) : "   -"));
		contentTable.addCell(setContent("Next Service Date", machineDetails.getNextService() !=null ? service.format(machineDetails.getNextService()) : "   -"));
		content.addElement(contentTable);
		
		//Footer
		PdfPCell footerContent = new PdfPCell();
		footerContent.setPadding(7);
		footerContent.setBorder(0);
		footerContent.setBackgroundColor(new BaseColor(255, 249, 229));
		Paragraph footerText = new Paragraph();
		footerText.setAlignment(Element.ALIGN_LEFT);
		footerText.add(new Phrase("Please monitor health alerts to maximize machine uptime.", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(255, 146, 0))));
		footerContent.addElement(footerText);
		
		Image jcbImage = Image.getInstance(IMAGELOACATION+"/message-squareicon.png");
		jcbImage.setAlignment(Image.ALIGN_LEFT);
		jcbImage.scalePercent(50);
		
		PdfPCell leftCell = new PdfPCell();
		leftCell.setPadding(9);
		leftCell.setBackgroundColor(new BaseColor(255, 249, 229));
		leftCell.setBorder(0);
		leftCell.addElement(jcbImage);
		
		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 5f, 95f });
		footerTable.addCell(leftCell);
		footerTable.addCell(footerContent);
		
		PdfPCell newContent = new PdfPCell();
		newContent.setPadding(5);
		newContent.setBorder(0);

		newContent.addElement(footerTable);
		
		customerAndMachineDetailsTable.addCell(header);
		customerAndMachineDetailsTable.addCell(alertHeader);
		customerAndMachineDetailsTable.addCell(alertContent);
		customerAndMachineDetailsTable.addCell(newContent);
		customerAndMachineDetailsTable.setTableEvent(new BorderEvent());
		try {
			document.add(customerAndMachineDetailsTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private PdfPTable alertDetailsTable(List<Alert> alertList) throws DocumentException {
		PdfPTable alertTable = new PdfPTable(1);
		alertTable.getDefaultCell().setBorder(0);
		alertTable.setWidthPercentage(100);

		PdfPCell contentCell = new PdfPCell();
		contentCell.setBorder(0);
		contentCell.setPadding(5);

		PdfPTable contentViewTable = new PdfPTable(5);
		contentViewTable.getDefaultCell().setBorder(0);
		contentViewTable.setWidths(new float[] { 28f, 4f, 28f, 4f, 28f });
		contentViewTable.setWidthPercentage(100);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(9);
		headerCell.setFixedHeight(40f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Alert Description",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell headerCell2 = new PdfPCell();
		headerCell2.setBorder(0);
		headerCell2.setPadding(9);
		headerCell2.setFixedHeight(40f);
		headerCell2.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText2 = new Paragraph();
		headerText2.setAlignment(Element.ALIGN_LEFT);
		headerText2.add(new Phrase("| Severity",
				new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell2.addElement(headerText2);

		PdfPCell headerCell3 = new PdfPCell();
		headerCell3.setBorder(0);
		headerCell3.setPadding(9);
		headerCell3.setFixedHeight(40f);
		headerCell3.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText3 = new Paragraph();
		headerText3.setAlignment(Element.ALIGN_LEFT);
		headerText3.add(new Phrase("| Requested Action", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell3.addElement(headerText3);

		contentViewTable.addCell(headerCell);
		contentViewTable.addCell(new Phrase(" "));
		contentViewTable.addCell(headerCell2);
		contentViewTable.addCell(new Phrase(" "));
		contentViewTable.addCell(headerCell3);

		if (alertList != null && !alertList.isEmpty()) {

			PdfPCell emptyCell = new PdfPCell();
			emptyCell.setBorder(0);
			emptyCell.setPaddingLeft(30);
			emptyCell.setPadding(7);
			emptyCell.addElement(
					new Phrase("-", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));

			for (Alert alertedetails : alertList) {

				PdfPCell valueCell = new PdfPCell();
				valueCell.setBorder(0);
				valueCell.setPaddingLeft(25);
				valueCell.setPadding(7);
				Paragraph valueText = new Paragraph();
				valueText.setAlignment(Element.ALIGN_LEFT);
				valueText.add(new Phrase(alertedetails.getEventName(),
						new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
				valueText.setLeading(20f);
				valueCell.addElement(valueText);

				contentViewTable.addCell(valueCell);
				contentViewTable.addCell(new Phrase(" "));

				valueCell = new PdfPCell();
				valueCell.setBorder(0);
				valueCell.setPaddingLeft(25);
				valueCell.setPadding(7);
				valueText = new Paragraph();
				valueText.setAlignment(Element.ALIGN_LEFT);
				if (alertedetails.getEventLevel().toString().equalsIgnoreCase(("RED"))) {
					valueText.add(new Phrase(alertedetails.getEventType().toString(),
							new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(239, 24, 0))));

				} else {
					valueText.add(new Phrase(alertedetails.getEventType().toString(),
							new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(255, 192, 0))));
				}
				valueText.setLeading(20f);
				valueCell.addElement(valueText);

				contentViewTable.addCell(valueCell);
				contentViewTable.addCell(new Phrase(" "));

				emptyCell = new PdfPCell();
				emptyCell.setBorder(0);
				emptyCell.setPaddingLeft(25);
				emptyCell.setPadding(7);
				valueText = new Paragraph();
				valueText.setAlignment(Element.ALIGN_LEFT);
				if (alertedetails.getEventLevel().toString().equalsIgnoreCase(("RED"))) {
					valueText.add(new Phrase("Contact nearest dealer",
							new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
				} else {
					valueText.add(new Phrase("-",
							new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
				}
				valueText.setLeading(20f);
				emptyCell.addElement(valueText);
				contentViewTable.addCell(emptyCell);

			}

			contentCell.addElement(contentViewTable);
		} else {

			PdfPCell emptyCell = new PdfPCell();
			emptyCell.setBorder(0);
			emptyCell.setPadding(7);
			emptyCell.setColspan(5);
			Paragraph valueText = new Paragraph();
			valueText.setAlignment(Element.ALIGN_CENTER);
			valueText.add(new Phrase("No Active Alerts Found",new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));

			emptyCell.addElement(valueText);
			contentViewTable.addCell(emptyCell);
			contentCell.addElement(contentViewTable);

		}

		alertTable.addCell(contentCell);
		return alertTable;
	}
	
	private void telehandlerReport(Document document, PdfWriter writer, String fromDate, String toDate,
			PdfReportResponse pdfReportResponse, List<AdvanceReportChart> chartList)
			throws DocumentException, IOException {
		PdfPTable outerTable = new PdfPTable(3);
		outerTable.setWidthPercentage(TABLEWIDTH);

		outerTable.setWidths(new float[] { 49f, 2f, 49f });
		outerTable.getDefaultCell().setBorder(0);

		PdfPTable fuelChart = null;
		PdfPTable fuelAverageChart = null;
		PdfPTable powerMode = null;

		log.info("chart " + chartList.size());

		List<PdfPTable> reportList = new ArrayList<>();
		for (AdvanceReportChart chart : chartList) {
			if (chart.getChart().equalsIgnoreCase("TelehandlerFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelChart = wlsFuelChart(pdfReportResponse.getTelehandlerReport().getTelehandlerFuelConsumption(),
						writer);
				reportList.add(fuelChart);
			} else if (chart.getChart().equalsIgnoreCase("AverageFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelAverageChart = wlsFuelAverageChart(
						pdfReportResponse.getTelehandlerReport().getAverageFuelConsumption(), writer);
				reportList.add(fuelAverageChart);
			} else if (chart.getChart().equalsIgnoreCase("FuelPowerBand")) {
				log.info("chart " + chart.getChart());
				powerMode = wlsPowerBandChart(pdfReportResponse.getTelehandlerReport().getFuelPowerBand(), writer);
				reportList.add(powerMode);
			}
		}

		log.info("Report List :" + reportList.size());
		if (chartList.size() == 1) {
			log.info("chart added" + 1);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 2) {
			log.info("chart added" + 2);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
		} else if (chartList.size() == 3) {
			log.info("chart added" + 3);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		document.add(outerTable);

	}
	
	private void compactorReport(Document document, PdfWriter writer, String fromDate, String toDate,
			PdfReportResponse pdfReportResponse, List<AdvanceReportChart> chartList)
			throws DocumentException, MalformedURLException, IOException {
		PdfPTable outerTable = new PdfPTable(3);
		outerTable.setWidthPercentage(TABLEWIDTH);

		outerTable.setWidths(new float[] { 49f, 2f, 49f });
		outerTable.getDefaultCell().setBorder(0);

		PdfPTable fuelChart = null;
		PdfPTable fuelAverageChart = null;
		PdfPTable powerMode = null;

		log.info("chart " + chartList.size());

		List<PdfPTable> reportList = new ArrayList<>();
		for (AdvanceReportChart chart : chartList) {
			if (chart.getChart().equalsIgnoreCase("CompactionFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelChart = wlsFuelChart(pdfReportResponse.getCompactorReport().getCompactionFuelConsumption(), writer);
				reportList.add(fuelChart);
			} else if (chart.getChart().equalsIgnoreCase("AverageFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelAverageChart = wlsFuelAverageChart(
						pdfReportResponse.getCompactorReport().getAverageFuelConsumption(), writer);
				reportList.add(fuelAverageChart);
			} else if (chart.getChart().equalsIgnoreCase("FuelPowerBand")) {
				log.info("chart " + chart.getChart());
				powerMode = wlsPowerBandChart(pdfReportResponse.getCompactorReport().getFuelPowerBand(), writer);
				reportList.add(powerMode);
			}
		}

		log.info("New :" + reportList.size() + "-" + reportList.get(0) + "-" + reportList.get(1));
		if (chartList.size() == 1) {
			log.info("chart added" + 1);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 2) {
			log.info("chart added" + 2);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
		} else if (chartList.size() == 3) {
			log.info("chart added" + 3);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		document.add(outerTable);

	}
	
	private void brakeSpace(Document document) throws DocumentException {
		document.add(new Paragraph("\n"));
	}
	
	private void excavatorreport(Document document, PdfWriter writer, String fromDate, String toDate,
			PdfReportResponse pdfReportResponse, List<AdvanceReportChart> chartList)
			throws DocumentException, MalformedURLException, IOException {

		PdfPTable outerTable = new PdfPTable(3);
		outerTable.setWidthPercentage(TABLEWIDTH);

		outerTable.setWidths(new float[] { 49f, 2f, 49f });
		outerTable.getDefaultCell().setBorder(0);

		PdfPTable powerMode = null;
		PdfPTable travelSwingChart = null;
		PdfPTable fuelChart = null;
		PdfPTable fuelAverageChart = null;
		PdfPTable hammerUserChart = null;
		PdfPTable hammerabuseChart = null;

		log.info("chart " + chartList.size());
		List<PdfPTable> reportList = new ArrayList<>();
		for (AdvanceReportChart chart : chartList) {
			if (chart.getChart().equalsIgnoreCase("PowerModes")) {
				log.info("chart " + chart.getChart());
				powerMode = powerModerChart(pdfReportResponse.getExcavatorReport().getPowerModes(), writer);
				reportList.add(powerMode);
			} else if (chart.getChart().equalsIgnoreCase("TravelAndSwingTime")) {
				log.info("chart " + chart.getChart());
				travelSwingChart = travelSwingChartChart(pdfReportResponse.getExcavatorReport().getTravelAndSwingTime(),
						writer);
				reportList.add(travelSwingChart);
			} else if (chart.getChart().equalsIgnoreCase("ExcavatorFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelChart = fuelChart(pdfReportResponse.getExcavatorReport().getExcavatorFuelConsumption(), writer);
				reportList.add(fuelChart);
			} else if (chart.getChart().equalsIgnoreCase("AverageFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelAverageChart = fuelAverageChart(pdfReportResponse.getExcavatorReport().getAverageFuelConsumption(),
						writer);
				reportList.add(fuelAverageChart);
			} else if (chart.getChart().equalsIgnoreCase("HammerUsedHours")) {
				log.info("chart " + chart.getChart());
				hammerUserChart = hammerUsedHrsChart(pdfReportResponse.getExcavatorReport().getHammerUsedHours(),
						writer);
				reportList.add(hammerUserChart);
			} else if (chart.getChart().equalsIgnoreCase("HammerAbuseEventCount")) {
				log.info("chart " + chart.getChart());
				hammerabuseChart = hammerAbuseEventChart(
						pdfReportResponse.getExcavatorReport().getHammerAbuseEventCount(), writer);
				reportList.add(hammerabuseChart);
			}
		}
		log.info("New :" + reportList.size() + "-" + reportList.get(0) + "-" + reportList.get(1));
		if (chartList.size() == 1) {
			log.info("chart added" + 1);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 2) {
			log.info("chart added" + 2);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
		} else if (chartList.size() == 3) {
			log.info("chart added" + 3);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 4) {
			log.info("chart added" + 4);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
		} else if (chartList.size() == 5) {
			log.info("chart added" + 5);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 6) {
			log.info("chart added" + 6);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
		}

		document.add(outerTable);

	}
	
	private PdfPTable hammerAbuseEventChart(List<HammerAbuseEventCount> hammerAbuseEventCountList, PdfWriter writer)
			throws IOException, DocumentException {
		Image averageChartImage = null;
		String fDate = null;
		String tDate = null;
		if (hammerAbuseEventCountList != null && !hammerAbuseEventCountList.isEmpty()) {
			HammerAbuseEventCount averageMax = hammerAbuseEventCountList.stream()
					.filter(c -> c.getHammerAbuseCount() != null)
					.max(Comparator.comparing(HammerAbuseEventCount::getHammerAbuseCount)).orElse(null);

			// Daily Average Chart

			CategoryDataset hammerDataSet = createHammerAbuseCountDataset(hammerAbuseEventCountList);

			JFreeChart hammerHrsChart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					hammerDataSet, PlotOrientation.VERTICAL, true, true, false);
			hammerHrsChart.setBackgroundPaint(Color.white);
			hammerHrsChart.getPlot().setBackgroundPaint(Color.white);
			hammerHrsChart.getPlot().setOutlinePaint(null);
			hammerHrsChart.getPlot().setOutlineStroke(null);
			hammerHrsChart.setBorderVisible(false);
			hammerHrsChart.removeLegend();

			CategoryPlot averageCPlot = (CategoryPlot) hammerHrsChart.getPlot();

			// set bar chart color
			((BarRenderer) averageCPlot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer averagerRender = (BarRenderer) hammerHrsChart.getCategoryPlot().getRenderer();
			averagerRender.setMaximumBarWidth(0.05);
			averagerRender.setSeriesPaint(0, new Color(241, 159, 156));
			averagerRender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			averagerRender.setDefaultItemLabelsVisible(true);

			final NumberAxis AveragerangeAxis = (NumberAxis) averageCPlot.getRangeAxis();
			Double averageMaxValue = (averageMax != null && averageMax.getHammerAbuseCount() != null)
					? averageMax.getHammerAbuseCount()
					: 10;
			setRangeAndTickUnit(AveragerangeAxis, averageMaxValue);
			AveragerangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setTickLabelPaint(Color.BLACK);
			AveragerangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage averageChartBufferedImage = hammerHrsChart.createBufferedImage(600, 450);
			averageChartImage = Image.getInstance(writer, averageChartBufferedImage, 1.0f);
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
		int size = hammerAbuseEventCountList.size();
		fDate = hammerAbuseEventCountList.get(0).getDate().getDate() + " "
				+ inputFormat.format(hammerAbuseEventCountList.get(0).getDate()) + " "
				+ yearFormat.format(hammerAbuseEventCountList.get(0).getDate());
		tDate = hammerAbuseEventCountList.get(size - 1).getDate().getDate() + " "
				+ inputFormat.format(hammerAbuseEventCountList.get(size - 1).getDate()) + " "
				+ yearFormat.format(hammerAbuseEventCountList.get(size - 1).getDate());

		PdfPTable dutyCycleSummary = generateHammerChart(averageChartImage, fDate, tDate, writer,
				"Hammer Abuse Event Count");
		return dutyCycleSummary;
	}
	
	private CategoryDataset createHammerAbuseCountDataset(List<HammerAbuseEventCount> hammerAbuseEventCountList) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");

		// Population in 2005
		for (HammerAbuseEventCount count : hammerAbuseEventCountList) {
			if (count.getHammerAbuseCount() != null) {
				dataset.addValue(count.getHammerAbuseCount(), "",
						count.getDate().getDate() + " " + inputFormat.format(count.getDate()));
			}

		}
		return dataset;
	}
	
	private PdfPTable hammerUsedHrsChart(List<HammerUsedHours> hammerUsedHours, PdfWriter writer)
			throws IOException, DocumentException {
		Image averageChartImage = null;
		String fDate = null;
		String tDate = null;
		if (hammerUsedHours != null && !hammerUsedHours.isEmpty()) {

			HammerUsedHours averageMax = hammerUsedHours.stream().filter(c -> c.getHammerUsedTimeHrs() != null)
					.max(Comparator.comparing(HammerUsedHours::getHammerUsedTimeHrs)).orElse(null);

			// Daily Average Chart
			CategoryDataset hammerDataSet = createHammerHrsDataset(hammerUsedHours);

			JFreeChart hammerHrsChart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					hammerDataSet, PlotOrientation.VERTICAL, true, true, false);
			hammerHrsChart.setBackgroundPaint(Color.white);
			hammerHrsChart.getPlot().setBackgroundPaint(Color.white);
			hammerHrsChart.getPlot().setOutlinePaint(null);
			hammerHrsChart.getPlot().setOutlineStroke(null);
			hammerHrsChart.setBorderVisible(false);
			hammerHrsChart.removeLegend();

			CategoryPlot averageCPlot = (CategoryPlot) hammerHrsChart.getPlot();
			
			// set bar chart color
			((BarRenderer) averageCPlot.getRenderer()).setBarPainter(new StandardBarPainter());
			BarRenderer averagerRender = (BarRenderer) hammerHrsChart.getCategoryPlot().getRenderer();
			averagerRender.setMaximumBarWidth(0.05);
			averagerRender.setSeriesPaint(0, new Color(145, 219, 133));
			averagerRender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			averagerRender.setDefaultItemLabelsVisible(true);

			final NumberAxis AveragerangeAxis = (NumberAxis) averageCPlot.getRangeAxis();
			Double averageMaxValue = (averageMax != null && averageMax.getHammerUsedTimeHrs() != null)
					? averageMax.getHammerUsedTimeHrs()
					: 10;
			setRangeAndTickUnit(AveragerangeAxis, averageMaxValue);
			AveragerangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setTickLabelPaint(Color.BLACK);
			AveragerangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage averageChartBufferedImage = hammerHrsChart.createBufferedImage(600, 450);
			averageChartImage = Image.getInstance(writer, averageChartBufferedImage, 1.0f);
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
		int size = hammerUsedHours.size();
		fDate = hammerUsedHours.get(0).getDate().getDate() + " " + inputFormat.format(hammerUsedHours.get(0).getDate())
				+ " " + yearFormat.format(hammerUsedHours.get(0).getDate());
		tDate = hammerUsedHours.get(size - 1).getDate().getDate() + " "
				+ inputFormat.format(hammerUsedHours.get(size - 1).getDate()) + " "
				+ yearFormat.format(hammerUsedHours.get(size - 1).getDate());

		PdfPTable dutyCycleSummary = generateHammerChart(averageChartImage, fDate, tDate, writer, "Hammer Used Hours");
		return dutyCycleSummary;
	}
	
	private PdfPTable generateHammerChart(Image chartImage, String fDate, String tDate,
			PdfWriter writer,String chartType) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable dailyAverageSummary = new PdfPTable(1);
		dailyAverageSummary.setWidthPercentage(100);
		dailyAverageSummary.getDefaultCell().setBorder(0);
		dailyAverageSummary.setTableEvent(new BorderEvent());
		
		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase(chartType.toUpperCase(),
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dailyAverageSummary.addCell(header);
	
		
		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		chartImage.scaleAbsolute(400f, 250f);
		chartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(chartImage);
		
		dailyAverageSummary.addCell(imagecell);
		
		return dailyAverageSummary;
		}
	
	private CategoryDataset createHammerHrsDataset(List<HammerUsedHours> hammerUsedHours) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");

		// Population in 2005
		for (HammerUsedHours hrs : hammerUsedHours) {
			if (hrs.getHammerUsedTimeHrs() != null) {
				dataset.addValue(hrs.getHammerUsedTimeHrs(), "",
						hrs.getDate().getDate() + " " + inputFormat.format(hrs.getDate()));
			}

		}
		return dataset;
	}
	
	private PdfPTable fuelAverageChart(List<FuelConsumptionResponse> averageFuelConsumption, PdfWriter writer)
			throws IOException, DocumentException {
		Double averageSum = null;
		Image averageChartImage = null;
		Integer totalList = 0;
		String fDate = null;
		String tDate = null;
		if (averageFuelConsumption != null && !averageFuelConsumption.isEmpty()) {

			averageSum = averageFuelConsumption.stream().filter(c -> c.getTotalFuelUsed() != null)
					.mapToDouble(x -> x.getTotalFuelUsed()).sum();

			for (FuelConsumptionResponse fuel : averageFuelConsumption) {
				if (fuel.getTotalFuelUsed() != null && fuel.getTotalFuelUsed() != 0 && fuel.getTotalFuelUsed() != 0.0) {
					totalList = totalList + 1;
				}
			}

			averageSum = averageSum / totalList;

			FuelConsumptionResponse averageMax = averageFuelConsumption.stream()
					.filter(c -> c.getTotalFuelUsed() != null)
					.max(Comparator.comparing(FuelConsumptionResponse::getTotalFuelUsed)).orElse(null);

			// Daily Average Chart
			CategoryDataset averageDataSet = createDataset(averageFuelConsumption);

			JFreeChart averageChart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					averageDataSet, PlotOrientation.VERTICAL, true, true, false);
			averageChart.setBackgroundPaint(Color.white);
			averageChart.getPlot().setBackgroundPaint(Color.white);
			averageChart.getPlot().setOutlinePaint(null);
			averageChart.getPlot().setOutlineStroke(null);
			averageChart.setBorderVisible(false);
			averageChart.removeLegend();

			// averageChart.getCategoryPlot().getRangeAxis().setLowerBound(0);
			CategoryPlot averageCPlot = (CategoryPlot) averageChart.getPlot();
			
			// set bar chart color
			((BarRenderer) averageCPlot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer averagerRender = (BarRenderer) averageChart.getCategoryPlot().getRenderer();
			averagerRender.setMaximumBarWidth(0.05);
			averagerRender.setSeriesPaint(0, new Color(110, 172, 253));
			averagerRender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			averagerRender.setDefaultItemLabelsVisible(true);

			final NumberAxis AveragerangeAxis = (NumberAxis) averageCPlot.getRangeAxis();
			Double averageMaxValue = (averageMax != null && averageMax.getTotalFuelUsed() != null)
					? averageMax.getTotalFuelUsed()
					: 10;
			setRangeAndTickUnit(AveragerangeAxis, averageMaxValue);
			AveragerangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setTickLabelPaint(Color.BLACK);
			AveragerangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage averageChartBufferedImage = averageChart.createBufferedImage(600, 450);
			averageChartImage = Image.getInstance(writer, averageChartBufferedImage, 1.0f);
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
		int size = averageFuelConsumption.size();
		fDate = averageFuelConsumption.get(0).getDate().getDate() + " "
				+ inputFormat.format(averageFuelConsumption.get(0).getDate()) + " "
				+ yearFormat.format(averageFuelConsumption.get(0).getDate());
		tDate = averageFuelConsumption.get(size - 1).getDate().getDate() + " "
				+ inputFormat.format(averageFuelConsumption.get(size - 1).getDate()) + " "
				+ yearFormat.format(averageFuelConsumption.get(size - 1).getDate());

		PdfPTable dutyCycleSummary = generateDailyAverageChart(averageChartImage, fDate, tDate, averageSum, writer);
		return dutyCycleSummary;
	}
	
	private PdfPTable fuelChart(List<FuelConsumptionResponse> excavatorFuelConsumption, PdfWriter writer)
			throws IOException, DocumentException {
		Image ChartImage = null;

		String fDate = null;
		String tDate = null;
		Double fuelSum = null;
		if (excavatorFuelConsumption != null && !excavatorFuelConsumption.isEmpty()) {

			fuelSum = excavatorFuelConsumption.stream().filter(c -> c.getTotalFuelUsed() != null)
					.mapToDouble(x -> x.getTotalFuelUsed()).sum();

			FuelConsumptionResponse fuelMax = excavatorFuelConsumption.stream()
					.filter(c -> c.getTotalFuelUsed() != null)
					.max(Comparator.comparing(FuelConsumptionResponse::getTotalFuelUsed)).orElse(null);

			CategoryDataset dataset = createDataset(excavatorFuelConsumption);

			JFreeChart chart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);
			chart.getPlot().setBackgroundPaint(Color.white);
			chart.getPlot().setOutlinePaint(null);
			chart.getPlot().setOutlineStroke(null);
			chart.setBorderVisible(false);

			chart.getCategoryPlot().getRangeAxis().setLowerBound(10);

			CategoryPlot cplot = (CategoryPlot) chart.getPlot();

			// set bar chart color
			((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

			renderer.setMaximumBarWidth(0.05);
			renderer.setSeriesPaint(0, new Color(173, 162, 255));
			renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setDefaultItemLabelsVisible(true);
			chart.removeLegend();

			final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
			Double fuelMaxValue = (fuelMax != null && fuelMax.getTotalFuelUsed() != null) ? fuelMax.getTotalFuelUsed(): 10;
			setRangeAndTickUnit(rangeAxis, fuelMaxValue);
			rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setTickLabelPaint(Color.BLACK);
			rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 450);
			ChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);

			SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
			SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
			int size = excavatorFuelConsumption.size();
			fDate = excavatorFuelConsumption.get(0).getDate().getDate() + " "
					+ inputFormat.format(excavatorFuelConsumption.get(0).getDate()) + " "
					+ yearFormat.format(excavatorFuelConsumption.get(0).getDate());
			tDate = excavatorFuelConsumption.get(size - 1).getDate().getDate() + " "
					+ inputFormat.format(excavatorFuelConsumption.get(size - 1).getDate()) + " "
					+ yearFormat.format(excavatorFuelConsumption.get(size - 1).getDate());

		}
		PdfPTable enginePowerSummary = generateFuelConsumptionChart(ChartImage, fDate, tDate, fuelSum, writer);
		return enginePowerSummary;
	}
	
	private PdfPTable travelSwingChartChart(List<ExcavatorTravelAndSwingTime> travelAndSwingTime, PdfWriter writer)
			throws MalformedURLException, DocumentException, IOException {
		Image travelSwingChartImage = null;

		Double totalHrs = 0.0, travelHrs = 0.0, swingHrs = 0.0;
		DecimalFormat dec = new DecimalFormat("#0.00");
		// Time Spent in Gears
		if (travelAndSwingTime != null && !travelAndSwingTime.isEmpty()) {
			totalHrs = travelAndSwingTime.stream().filter(o -> o.getTotalHrs() != null && !o.getTotalHrs().isNaN())
					.mapToDouble(o -> o.getTotalHrs()).sum();
			travelHrs = travelAndSwingTime.stream().filter(o -> o.getTravelHrs() != null && !o.getTravelHrs().isNaN())
					.mapToDouble(o -> o.getTravelHrs()).sum();
			swingHrs = travelAndSwingTime.stream().filter(o -> o.getSwingHrs() != null && !o.getSwingHrs().isNaN())
					.mapToDouble(o -> o.getSwingHrs()).sum();

			List<Double> data = new ArrayList<>();
			data.add(totalHrs);
			data.add(travelHrs);
			data.add(swingHrs);

			CategoryDataset dataset = createTravelSwingChartDataset(totalHrs, travelHrs, swingHrs);

			Double swing = data.stream().max(Double::compare).get();

			JFreeChart chart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);
			chart.getPlot().setBackgroundPaint(Color.white);
			chart.getPlot().setOutlinePaint(null);
			chart.getPlot().setOutlineStroke(null);
			chart.setBorderVisible(false);

			chart.getCategoryPlot().getRangeAxis().setLowerBound(10);

			CategoryPlot cplot = (CategoryPlot) chart.getPlot();
			
			// set bar chart color
			((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

			renderer.setMaximumBarWidth(0.10);
			renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setDefaultItemLabelsVisible(true);
			cplot.setRenderer(renderer);

			renderer.setSeriesPaint(0, new Color(139, 13, 77));
			renderer.setSeriesPaint(1, new Color(116, 94, 166));
			renderer.setSeriesPaint(2, new Color(0, 212, 172));

			chart.removeLegend();

			final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
			Double fuelMaxValue = (swing != null && !swing.isNaN()) ? swing : 10;
			setRangeAndTickUnit(rangeAxis, fuelMaxValue);
			rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setTickLabelPaint(Color.BLACK);
			rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage travelSwingBufferedImage = chart.createBufferedImage(600, 450);
			travelSwingChartImage = Image.getInstance(writer, travelSwingBufferedImage, 1.0f);

		}

		PdfPTable travelSwingSummary = generateTravelSwingChart(travelSwingChartImage, dec.format(totalHrs),
				dec.format(travelHrs), dec.format(swingHrs));
		return travelSwingSummary;

	}
	
	private PdfPTable generateTravelSwingChart(Image travelSwingChartImage, String totalHrs, String travelHrs,
			String swingHrs) throws DocumentException, MalformedURLException, IOException {
		
		PdfPTable fuelConsumptionSummary = new PdfPTable(1);
		//fuelConsumptionSummary.setPaddingTop(15);
		fuelConsumptionSummary.setWidthPercentage(100);
		fuelConsumptionSummary.getDefaultCell().setBorder(0);
		fuelConsumptionSummary.setTableEvent(new BorderEvent());
		

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("TRAVEL AND SWING TIME",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		fuelConsumptionSummary.addCell(header);
		
		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);
		
		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase(
				"Time Spent in Gears is combination of Gear1,Gear2,Gear3 and Gear4 in hrs.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0 ))));
		comments.setLeading(20f);
		content.addElement(comments);
		
		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		travelSwingChartImage.scaleAbsolute(400f, 250f);
		travelSwingChartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(travelSwingChartImage);
		
		fuelConsumptionSummary.addCell(imagecell);
		
		PdfPTable chartTable = new PdfPTable(5);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setPaddingTop(10f);
		chartTable.setWidths(new float[] { 15f,25f,20f,15f,25f });
		
		Image icon1 = Image.getInstance(IMAGELOACATION+"/icon2.png");
		icon1.setAlignment(Image.ALIGN_CENTER);
		icon1.setWidthPercentage(17);
		
		Image icon2 = Image.getInstance(IMAGELOACATION+"/icon3.png");
		icon2.setAlignment(Image.ALIGN_CENTER);
		icon2.setWidthPercentage(17);
		
		Image icon3 = Image.getInstance(IMAGELOACATION+"/icon4.png");
		icon3.setAlignment(Image.ALIGN_CENTER);
		icon3.setWidthPercentage(17);
		
		// Food Row 1
		PdfPCell gearIconCell = new PdfPCell();
		gearIconCell.setPaddingTop(10);
		gearIconCell.setBorder(0);
		gearIconCell.addElement(icon1);

		PdfPCell gear1 = new PdfPCell();
		gear1.setBorder(0);
		gear1.setPadding(7);
		gear1.setPaddingBottom(13f);
		gear1.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear1Text = new Paragraph();
		gear1Text.setAlignment(Element.ALIGN_LEFT);
		gear1Text.add(new Phrase("| Total Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear1.addElement(gear1Text);

		PdfPCell gear2IconCell = new PdfPCell();
		gear2IconCell.setPaddingTop(10);
		gear2IconCell.setBorder(0);
		gear2IconCell.addElement(icon2);

		PdfPCell gear2 = new PdfPCell();
		gear2.setBorder(0);
		gear2.setPadding(7);
		gear2.setPaddingBottom(13f);
		gear2.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear2Text = new Paragraph();
		gear2Text.setAlignment(Element.ALIGN_LEFT);
		gear2Text.add(new Phrase("| Travel Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear2.addElement(gear2Text);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		chartTable.addCell(gearIconCell);
		chartTable.addCell(gear1);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear2IconCell);
		chartTable.addCell(gear2);

		PdfPCell gearvalueCell = new PdfPCell();
		gearvalueCell.setBorder(0);
		gearvalueCell.setPaddingLeft(20);
		Paragraph gearvalueText = new Paragraph();
		gearvalueText.setAlignment(Element.ALIGN_CENTER);
		if (totalHrs != null) {
			gearvalueText.add(new Phrase(totalHrs + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gearvalueText.add("");
		}
		
		PdfPCell gear2valueCell = new PdfPCell();
		gear2valueCell.setBorder(0);
		gear2valueCell.setPaddingLeft(20);
		Paragraph gear2valueText = new Paragraph();
		gear2valueText.setAlignment(Element.ALIGN_CENTER);
		if (travelHrs != null) {
			gear2valueText.add(new Phrase(travelHrs + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gear2valueText.add("");
		}

		chartTable.addCell(emptyValues);
		chartTable.addCell(gearvalueText);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear2valueText);

		// Food Row 2

		PdfPCell gear3IconCell = new PdfPCell();
		gear3IconCell.setPaddingTop(10);
		gear3IconCell.setBorder(0);
		gear3IconCell.addElement(icon3);

		PdfPCell gear3 = new PdfPCell();
		gear3.setBorder(0);
		gear3.setPadding(7);
		gear3.setPaddingBottom(13f);
		gear3.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear3Text = new Paragraph();
		gear3Text.setAlignment(Element.ALIGN_LEFT);
		gear3Text.add(new Phrase("| Swing Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear3.addElement(gear3Text);

		chartTable.addCell(gear3IconCell);
		chartTable.addCell(gear3);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);

		PdfPCell gear3valueCell = new PdfPCell();
		gear3valueCell.setBorder(0);
		// gear3valueCell.setPaddingLeft(20);
		Paragraph gear3valueText = new Paragraph();
		gear3valueText.setAlignment(Element.ALIGN_CENTER);
		if (swingHrs != null) {
			gear3valueText.add(new Phrase(swingHrs + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gear3valueText.add("");
		}

		chartTable.addCell(emptyValues);
		chartTable.addCell(gear3valueText);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);

		// chartTable.addCell(fuelTable);

		fuelConsumptionSummary.addCell(chartTable);
		return fuelConsumptionSummary;
	}
	
	private CategoryDataset createTravelSwingChartDataset(Double totalHrs, Double travelHrs, Double swingHrs) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(totalHrs, "Total Hrs", "Hours");
		dataset.addValue(travelHrs, "Travel Hrs", "Hours");
		dataset.addValue(swingHrs, "Swing Hrs", "Hours");
		return dataset;
	}
	
	private PdfPTable powerModerChart(List<ExcavatorPowerModes> powerModes, PdfWriter writer)
			throws MalformedURLException, DocumentException, IOException {
		Double lBand = 0.0, gBand = 0.0, hBand = 0.0, hplusBand = 0.0;
		DecimalFormat dec = new DecimalFormat("#0.00");
		if (powerModes != null && !powerModes.isEmpty()) {
			lBand = powerModes.stream().filter(o -> o.getLBand() != null && !o.getLBand().isNaN())
					.mapToDouble(o -> o.getLBand()).sum();
			gBand = powerModes.stream().filter(o -> o.getGBand() != null && !o.getGBand().isNaN())
					.mapToDouble(o -> o.getGBand()).sum();
			hBand = powerModes.stream().filter(o -> o.getHBand() != null && !o.getHBand().isNaN())
					.mapToDouble(o -> o.getHBand()).sum();
			hplusBand = powerModes.stream().filter(o -> o.getHPlusBand() != null && !o.getHPlusBand().isNaN())
					.mapToDouble(o -> o.getHPlusBand()).sum();

		}

		DefaultPieDataset<String> dataSet1 = new DefaultPieDataset<String>();
		if (lBand != null)
			dataSet1.setValue(dec.format(lBand), lBand);
		if (gBand != null)
			dataSet1.setValue(dec.format(gBand), gBand);
		if (hBand != null)
			dataSet1.setValue(dec.format(hBand), hBand);
		if (hplusBand != null)
			dataSet1.setValue(dec.format(hplusBand), hplusBand);
		JFreeChart dutychart = ChartFactory.createPieChart("", dataSet1, true, true, false);
		dutychart.setBackgroundPaint(Color.white);
		dutychart.getPlot().setBackgroundPaint(Color.white);
		dutychart.getPlot().setOutlinePaint(null);
		dutychart.getPlot().setOutlineStroke(null);
		dutychart.setBorderVisible(false);
		dutychart.getPlot().setOutlineVisible(false);
		dutychart.removeLegend();
		
		// Display value inside the chart
		PiePlot<?> dutychartPlot = (PiePlot<?>) dutychart.getPlot();
		dutychartPlot.setSimpleLabels(true);
		dutychartPlot.setShadowPaint(null);
		dutychartPlot.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 18));
		dutychartPlot.setLabelPaint(new Color(0, 0, 0));
		dutychartPlot.setLabelShadowPaint(null);
		dutychartPlot.setLabelOutlinePaint(null);
		dutychartPlot.setLabelBackgroundPaint(null);
		dutychartPlot.setLabelPaint(Color.black);

		// Dynamic Color
		Color[] colors = { new Color(236, 129, 65), new Color(0, 185, 240), new Color(253, 192, 45),
				new Color(165, 165, 165) };
		// set each sections inside paint
		int j = 0;
		for (Object key : dataSet1.getKeys()) {
			dutychartPlot.setSectionPaint((Comparable<?>) key, colors[j % colors.length]);
			j++;
		}

		BufferedImage powerModeChartBufferedImage = dutychart.createBufferedImage(400, 330);
		Image PowerModeChartImage = null;
		try {
			PowerModeChartImage = Image.getInstance(writer, powerModeChartBufferedImage, 1.0f);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}

		PdfPTable dutyCycleSummary = generatePowerModeSummary(PowerModeChartImage, lBand, gBand, hBand, hplusBand);
		return dutyCycleSummary;
	}
	
	private PdfPTable generatePowerModeSummary(Image powerModeChartImage, Double lBand, Double gBand, Double hBand,
			Double hplusBand) throws DocumentException, MalformedURLException, IOException {
		DecimalFormat dec = new DecimalFormat("#0.00");

		PdfPTable dutyCycleSummary = new PdfPTable(1);
		dutyCycleSummary.setWidthPercentage(100);
		dutyCycleSummary.getDefaultCell().setBorder(0);
		dutyCycleSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("POWER MODE",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dutyCycleSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);
		// content.setFixedHeight(53f);

		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase("Power modes are combination of L Band,G Band,H Band,H Plus Band.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0))));
		comments.setLeading(20f);
		content.addElement(comments);
		dutyCycleSummary.addCell(content);
		// dutyCycleSummary.setSpacingAfter(2f);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 60f, 40f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.setPaddingTop(50f);
		imagecell.addElement(powerModeChartImage);

		chartTable.addCell(imagecell);

		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		// emptyCell.setPadding(10f);
		emptyCell.setFixedHeight(17.5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon7.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(50);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon5.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(50);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(50);

		Image icon4 = Image.getInstance(IMAGELOACATION + "/icon6.png");
		icon4.setAlignment(Image.ALIGN_LEFT);
		icon4.setWidthPercentage(50);

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setPaddingTop(50f);
		legentContent.setBorder(0);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(
				new Phrase("| L Band ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (lBand != null) {
			valueText.add(new Phrase(dec.format(lBand) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);
		legentContent.addElement(legend);
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(
				new Phrase("| G Band ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (gBand != null) {
			valueText.add(new Phrase(dec.format(gBand) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(
				new Phrase("| H Band ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (hBand != null) {
			valueText.add(new Phrase(dec.format(hBand) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		// 4th
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| H Plus Band ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon4);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (hplusBand != null) {
			valueText.add(new Phrase(dec.format(hplusBand) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		dutyCycleSummary.addCell(chartTable);

		return dutyCycleSummary;
	}
	
	private void customerAndMachineDetails(Document document, MachineDetails machineDetails) {

		PdfPTable customerAndMachineDetailsTable = new PdfPTable(1);
		customerAndMachineDetailsTable.setWidthPercentage(TABLEWIDTH);
		customerAndMachineDetailsTable.setSpacingAfter(4);

		// Table header
		PdfPCell header = new PdfPCell();
		header.setPadding(15);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("CUSTOMER & MACHINE DETAILS", new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);

		// Table inner content
		PdfPCell content = new PdfPCell();
		content.setPadding(10);
		content.setBorder(0);

		// splitting table to 3 columns
		PdfPTable contentTable = new PdfPTable(3);
		contentTable.setWidthPercentage(100);

		SimpleDateFormat status = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat renewalDate = new SimpleDateFormat("dd/MM/yyyy");
		
		contentTable.addCell(setContent("Machine Number", machineDetails.getVin()));
		contentTable.addCell(setContent("Model", machineDetails.getModel() != null ? machineDetails.getModel() : "-"));
		contentTable.addCell(setContent("LiveLink Renewal Date", machineDetails.getRenewalDate() != null ? renewalDate.format(machineDetails.getRenewalDate()) : "-"));
		contentTable.addCell(setContent("Customer Name", machineDetails.getCustomerName() != null ? machineDetails.getCustomerName() : "-"));
		contentTable.addCell(setContent("Customer Contact", machineDetails.getMobileNumber() != null ? machineDetails.getMobileNumber() : "-"));
		contentTable.addCell(setContent("Status as on", machineDetails.getStatusOn() != null ? status.format(machineDetails.getStatusOn()) : "-"));

		content.addElement(contentTable);
		customerAndMachineDetailsTable.addCell(header);
		customerAndMachineDetailsTable.addCell(content);
		customerAndMachineDetailsTable.setTableEvent(new BorderEvent());
		try {
			document.add(customerAndMachineDetailsTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
	
	private PdfPCell setContent(String header, String value) {

		PdfPCell contentCell = new PdfPCell();
		contentCell.setPadding(5);
		contentCell.setBorder(0);

		PdfPTable contentViewTable = new PdfPTable(1);
		contentViewTable.setWidthPercentage(100);

		PdfPCell headerCell = new PdfPCell();
		
		headerCell.setPadding(9);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerCell.setBorder(Rectangle.NO_BORDER);
		headerCell.setFixedHeight(40f);
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| " + header, new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueCell.setPadding(9);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		
		if(value !=null && value.contains("%"))
		{
			valueText.add(
					new Phrase(value, new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(255, 192, 0))));
		}else {
			if(header.equalsIgnoreCase("Status as on")) {
				valueText.add(
						new Phrase(value, new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(0, 212, 172))));
			}else {
				valueText.add(
						new Phrase(value, new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
			}
			
		}
		
		valueCell.addElement(valueText);

		contentViewTable.addCell(headerCell);
		contentViewTable.addCell(valueCell);
		contentCell.addElement(contentViewTable);
		return contentCell;
		
	}
	
	private void bhlReport(Document document, PdfWriter writer, String fromDate, String toDate,
			PdfReportResponse pdfReportResponse, List<AdvanceReportChart> chartList) throws IOException, DocumentException {
		
		PdfPTable outerTable = new PdfPTable(3);
		outerTable.setWidthPercentage(TABLEWIDTH);
		
		outerTable.setWidths(new float[] { 49f, 2f, 49f });
		outerTable.getDefaultCell().setBorder(0);

		PdfPTable fuelChart = null;
		PdfPTable fuelAverageChart = null;
		PdfPTable fuelConsumptionDutyChart = null;
		PdfPTable fuelConsumptionExcavationChart = null;
		PdfPTable distanceRoadingChart = null;
		PdfPTable averageSpeedChart = null;
		PdfPTable dutyCyleChart = null;
		PdfPTable mcCompassChart = null;
		PdfPTable gearChart = null;
		PdfPTable powerbandChart = null;
		PdfPTable excavationChart =  null;
		
		
		
		log.info("chart "+chartList.size());
		
		List<PdfPTable> reportList = new ArrayList<>();
		for(AdvanceReportChart chart : chartList)
		{	log.info("chart List data "+chart.getChart()  );
			if(chart.getChart().equalsIgnoreCase("WlsFuelConsumption"))
			{	
				log.info("chart "+chart.getChart());
				fuelChart = wlsFuelChart(pdfReportResponse.getBhlReport().getFuelConsumption(),writer);
				 reportList.add(fuelChart);
			}else if(chart.getChart().equalsIgnoreCase("AverageFuelConsumption")) {
				log.info("chart "+chart.getChart());
				fuelAverageChart = wlsFuelAverageChart(pdfReportResponse.getBhlReport().getDailyFuelAverage(),writer);
				 reportList.add(fuelAverageChart);
			}else if(chart.getChart().equalsIgnoreCase("FuelConsumptionDuty")) {
				log.info("chart "+chart.getChart());
				fuelConsumptionDutyChart = bhlFuelConsumptionDutyChart(pdfReportResponse.getBhlReport().getFuelConsumptionDuty(),writer);
				reportList.add(fuelConsumptionDutyChart);
			}else if(chart.getChart().equalsIgnoreCase("FuelConsumptionExcavation")) {
				log.info("chart "+chart.getChart());
				fuelConsumptionExcavationChart = bhlFuelConsumptionExcavationChart(pdfReportResponse.getBhlReport().getFuelConsumptionExcavation(),writer);
				reportList.add(fuelConsumptionExcavationChart);
			}else if(chart.getChart().equalsIgnoreCase("DistanceRoading")) {
				log.info("chart "+chart.getChart());
				distanceRoadingChart = bhlDistanceRoadingChart(pdfReportResponse.getBhlReport().getDistanceRoading(),writer);
				reportList.add(distanceRoadingChart);
			}else if(chart.getChart().equalsIgnoreCase("AverageRoading")) {
				log.info("chart "+chart.getChart());
				averageSpeedChart = bhlAverageRoadingChart(pdfReportResponse.getBhlReport().getAverageRoading(),writer);
				reportList.add(averageSpeedChart);
			}else if(chart.getChart().equalsIgnoreCase("ExcavationModesList")) {
				log.info("chart "+chart.getChart());
				fuelConsumptionDutyChart = bhlExcavationModesListChart(pdfReportResponse.getBhlReport().getMachineExcavationMode(),writer);
				reportList.add(fuelConsumptionDutyChart);
			}else if(chart.getChart().equalsIgnoreCase("MachinePowerBand")) {
				log.info("chart "+chart.getChart());
				powerbandChart = bhlEnginePowerBandChart(pdfReportResponse.getMachinePowerBand(),writer);
				reportList.add(powerbandChart);
			}else if(chart.getChart().equalsIgnoreCase("DutyCycleBHLList")) {
				log.info("chart "+chart.getChart());
				dutyCyleChart = bhlDutyCycleBHLListChart(pdfReportResponse.getBhlReport().getMachineDutyCycle(),writer);
				reportList.add(dutyCyleChart);
			}else if(chart.getChart().equalsIgnoreCase("MachineCompassBHLList")) {
				log.info("chart "+chart.getChart());
				mcCompassChart = bhlMachineCompassBHLListChart(pdfReportResponse.getBhlReport().getMachineCompassBHLList(),writer);
				reportList.add(mcCompassChart);
			}else if(chart.getChart().equalsIgnoreCase("GearTimeSpentBHLList")) {
				log.info("chart "+chart.getChart());
				gearChart = bhlGearTimeSpentBHLListChart(pdfReportResponse.getBhlReport().getGearTimeSpentBHLList(),writer);
				reportList.add(gearChart);
			}
		}
		log.info("Chart List Size "+reportList.size());
		if(chartList.size()==1)
		{
			log.info("chart added" +1);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}else if(chartList.size()==2) {
			log.info("chart added" +2);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));	
		}else if(chartList.size()==3) {
			log.info("chart added" +3);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}else if(chartList.size()==4) {
			log.info("chart added" +4);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
		}else if(chartList.size()==5) {
			log.info("chart added" +5);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		else if(chartList.size()==6) {
			log.info("chart added" +6);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
		}else if(chartList.size()==7) {
			log.info("chart added" +7);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
			outerTable.addCell(reportList.get(6));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		else if(chartList.size()==8) {
			log.info("chart added" +8);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
			outerTable.addCell(reportList.get(6));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(7));
		}
		else if(chartList.size()==9) {
			log.info("chart added" +9);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
			outerTable.addCell(reportList.get(6));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(7));
			outerTable.addCell(reportList.get(8));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		else if(chartList.size()==10) {
			log.info("chart added" +10);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
			outerTable.addCell(reportList.get(6));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(7));
			outerTable.addCell(reportList.get(8));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(9));
		}else if(chartList.size()==11) {
			log.info("chart added" +11);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
			outerTable.addCell(reportList.get(4));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(5));
			outerTable.addCell(reportList.get(6));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(7));
			outerTable.addCell(reportList.get(8));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(9));
			outerTable.addCell(reportList.get(10));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		}
		
		document.add(outerTable);
		
		log.info("Report List :"+reportList.size());
		
	}
	
	private PdfPTable bhlGearTimeSpentBHLListChart(List<GearTimeSpentBHL> gearTimeSpentBHLList, PdfWriter writer) throws IOException, DocumentException {
		DecimalFormat dec = new DecimalFormat("#0.00");

		Image gearChartImage =null;
		
		Double firstGear = 0.0;
	    Double secondGear = 0.0;
	    Double thirdGear = 0.0;
	    Double fourGear = 0.0;
		//Time Spent in Gears
		if(gearTimeSpentBHLList!=null && !gearTimeSpentBHLList.isEmpty())
		{		    
			 firstGear = gearTimeSpentBHLList.stream().filter(o->o.getFirstGear()!=null).mapToDouble(o -> o.getFirstGear()).sum();
		     secondGear = gearTimeSpentBHLList.stream().filter(o->o.getSecoundGear()!=null).mapToDouble(o -> o.getSecoundGear()).sum();
		     thirdGear = gearTimeSpentBHLList.stream().filter(o->o.getThirdGear()!=null).mapToDouble(o -> o.getThirdGear()).sum();
		     fourGear = gearTimeSpentBHLList.stream().filter(o->o.getForthGear()!=null).mapToDouble(o -> o.getForthGear()).sum();
		     
		     CategoryDataset dataset = createGearChartDataset(gearTimeSpentBHLList,firstGear,secondGear,thirdGear,fourGear);
		     
		     List<Double> data = new ArrayList<>();
				data.add(firstGear);
				data.add(secondGear);
				data.add(thirdGear);
				data.add(fourGear);
				
			   Double gear= data.stream().max(Double::compare).get();
		    
		     
		     JFreeChart chart=ChartFactory.createBarChart(  
			            "", //Chart Title  
			            "", // Category axis  
			            "", // Value axis  
			            dataset,  
			            PlotOrientation.VERTICAL,  
			            true,true,false  
			           );  
				chart.setBackgroundPaint(Color.white);
				chart.getPlot().setBackgroundPaint(Color.white);
				chart.getPlot().setOutlinePaint(null);
				chart.getPlot().setOutlineStroke(null);
				chart.setBorderVisible(false);
				
				chart.getCategoryPlot().getRangeAxis().setLowerBound(10);
				
				CategoryPlot cplot = (CategoryPlot)chart.getPlot();
			   	 //set  bar chart color

			    ((BarRenderer)cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			    BarRenderer renderer = (BarRenderer)chart.getCategoryPlot().getRenderer();
			    
			    renderer.setMaximumBarWidth(0.10);
			      
			    renderer.setSeriesPaint(0, new Color(139, 13, 77));
			    renderer.setSeriesPaint(1, new Color(116, 94, 166));
			    renderer.setSeriesPaint(2, new Color(0, 212, 172));
			    renderer.setSeriesPaint(3, new Color(0, 185, 240));
			    
			    renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			    renderer.setDefaultItemLabelsVisible(true); 
			        
				chart.removeLegend();
			    
				final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
				Double fuelMaxValue = (gear!=null && gear !=null) ? gear : 10;
				setRangeAndTickUnit(rangeAxis, fuelMaxValue);
		       
		        rangeAxis.setLabelFont(new java.awt.Font( "HELVETICA", java.awt.Font.PLAIN, 11 ));
		        rangeAxis.setTickLabelPaint(Color.BLACK);
		        rangeAxis.setTickLabelFont(new java.awt.Font( "HELVETICA", java.awt.Font.PLAIN, 11 ));
		        rangeAxis.setLabelPaint(Color.BLACK);
		        
				BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 450);
				gearChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);
		
		}
		PdfPTable dutyCycleSummary = generateGearChart(gearChartImage,dec.format(firstGear),dec.format(secondGear),dec.format(thirdGear),dec.format(fourGear));

		return dutyCycleSummary;
	}
	
	private PdfPTable generateGearChart(Image gearChartImage, String firstGear, String secondGear, String thirdGear,
			String fourGear) throws DocumentException, MalformedURLException, IOException {

		PdfPTable fuelConsumptionSummary = new PdfPTable(1);
		fuelConsumptionSummary.setWidthPercentage(100);
		fuelConsumptionSummary.getDefaultCell().setBorder(0);
		fuelConsumptionSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("TIME SPENT IN GEARS",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		fuelConsumptionSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);

		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase("Time Spent in Gears is combination of Gear1,Gear2,Gear3 and Gear4 in hrs.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0))));
		comments.setLeading(20f);
		content.addElement(comments);
		fuelConsumptionSummary.addCell(content);

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		gearChartImage.scaleAbsolute(400f, 250f);
		gearChartImage.setAlignment(Image.ALIGN_CENTER);
		imagecell.addElement(gearChartImage);

		fuelConsumptionSummary.addCell(imagecell);

		PdfPTable chartTable = new PdfPTable(5);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setPaddingTop(10f);
		chartTable.setWidths(new float[] { 15f, 25f, 20f, 15f, 25f });

		Image gear1Icon = Image.getInstance(IMAGELOACATION + "/icon2.png");
		gear1Icon.setAlignment(Image.ALIGN_CENTER);
		gear1Icon.setWidthPercentage(18);

		Image gear2Icon = Image.getInstance(IMAGELOACATION + "/icon3.png");
		gear2Icon.setAlignment(Image.ALIGN_CENTER);
		gear2Icon.setWidthPercentage(18);

		Image gear3Icon = Image.getInstance(IMAGELOACATION + "/icon4.png");
		gear3Icon.setAlignment(Image.ALIGN_CENTER);
		gear3Icon.setWidthPercentage(18);

		Image gear4Icon = Image.getInstance(IMAGELOACATION + "/icon5.png");
		gear4Icon.setAlignment(Image.ALIGN_CENTER);
		gear4Icon.setWidthPercentage(18);

		// Food Row 1

		PdfPCell gearIconCell = new PdfPCell();
		gearIconCell.setPaddingTop(10);
		gearIconCell.setBorder(0);
		gearIconCell.addElement(gear1Icon);

		PdfPCell gear1 = new PdfPCell();
		gear1.setBorder(0);
		gear1.setPadding(7);
		gear1.setPaddingBottom(13f);
		gear1.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear1Text = new Paragraph();
		gear1Text.setAlignment(Element.ALIGN_LEFT);
		gear1Text.add(
				new Phrase("| Gear 1", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear1.addElement(gear1Text);

		PdfPCell gear2IconCell = new PdfPCell();
		gear2IconCell.setPaddingTop(10);
		gear2IconCell.setBorder(0);
		gear2IconCell.addElement(gear2Icon);

		PdfPCell gear2 = new PdfPCell();
		gear2.setBorder(0);
		gear2.setPadding(7);
		gear2.setPaddingBottom(13f);
		gear2.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear2Text = new Paragraph();
		gear2Text.setAlignment(Element.ALIGN_LEFT);
		gear2Text.add(
				new Phrase("| Gear 2", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear2.addElement(gear2Text);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		chartTable.addCell(gearIconCell);
		chartTable.addCell(gear1);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear2IconCell);
		chartTable.addCell(gear2);

		PdfPCell gearvalueCell = new PdfPCell();
		gearvalueCell.setBorder(0);
		gearvalueCell.setPaddingLeft(20);
		Paragraph gearvalueText = new Paragraph();
		gearvalueText.setAlignment(Element.ALIGN_CENTER);
		if (firstGear != null) {
			gearvalueText.add(new Phrase(firstGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gearvalueText.add("");
		}

		PdfPCell gear2valueCell = new PdfPCell();
		gear2valueCell.setBorder(0);
		gear2valueCell.setPaddingLeft(20);
		Paragraph gear2valueText = new Paragraph();
		gear2valueText.setAlignment(Element.ALIGN_CENTER);
		if (secondGear != null) {
			gear2valueText.add(new Phrase(secondGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gear2valueText.add("");
		}

		chartTable.addCell(emptyValues);
		chartTable.addCell(gearvalueText);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear2valueText);

		// Food Row 2
		PdfPCell gear3IconCell = new PdfPCell();
		gear3IconCell.setPaddingTop(10);
		gear3IconCell.setBorder(0);
		gear3IconCell.addElement(gear3Icon);

		PdfPCell gear3 = new PdfPCell();
		gear3.setBorder(0);
		gear3.setPadding(7);
		gear3.setPaddingBottom(13f);
		gear3.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear3Text = new Paragraph();
		gear3Text.setAlignment(Element.ALIGN_LEFT);
		gear3Text.add(
				new Phrase("| Gear 3", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear3.addElement(gear3Text);

		PdfPCell gear4IconCell = new PdfPCell();
		gear4IconCell.setPaddingTop(10);
		gear4IconCell.setBorder(0);
		gear4IconCell.addElement(gear4Icon);

		PdfPCell gear4 = new PdfPCell();
		gear4.setBorder(0);
		gear4.setPadding(7);
		gear4.setPaddingBottom(13f);
		gear4.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph gear4Text = new Paragraph();
		gear4Text.setAlignment(Element.ALIGN_LEFT);
		gear4Text.add(
				new Phrase("| Gear 4", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		gear4.addElement(gear4Text);

		chartTable.addCell(gear3IconCell);
		chartTable.addCell(gear3);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear4IconCell);
		chartTable.addCell(gear4);

		PdfPCell gear3valueCell = new PdfPCell();
		gear3valueCell.setBorder(0);
		// gear3valueCell.setPaddingLeft(20);
		Paragraph gear3valueText = new Paragraph();
		gear3valueText.setAlignment(Element.ALIGN_CENTER);
		if (thirdGear != null) {
			gear3valueText.add(new Phrase(thirdGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gear3valueText.add("");
		}

		PdfPCell gear4valueCell = new PdfPCell();
		gear4valueCell.setBorder(0);
		gear4valueCell.setPaddingLeft(20);
		Paragraph gear4valueText = new Paragraph();
		gear4valueText.setAlignment(Element.ALIGN_CENTER);
		if (fourGear != null) {
			gear4valueText.add(new Phrase(fourGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			gear4valueText.add("");
		}

		chartTable.addCell(emptyValues);
		chartTable.addCell(gear3valueText);
		chartTable.addCell(emptyValues);
		chartTable.addCell(emptyValues);
		chartTable.addCell(gear4valueText);

		// chartTable.addCell(fuelTable);
		fuelConsumptionSummary.addCell(chartTable);
		return fuelConsumptionSummary;
	}
	
	private CategoryDataset createGearChartDataset(List<GearTimeSpentBHL> gearTimeSpentBHLList, Double firstGear,
			Double secondGear, Double thirdGear, Double fourGear) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(firstGear, "Gear-1", "Gear");
		dataset.addValue(secondGear, "Gear-2", "Gear");
		dataset.addValue(thirdGear, "Gear-3", "Gear");
		dataset.addValue(fourGear, "Gear-4", "Gear");

		return dataset;

	}
	
	private PdfPTable bhlMachineCompassBHLListChart(List<MachineCompassBHL> machineCompassBHLList, PdfWriter writer)
			throws IOException, DocumentException {
		DecimalFormat dec = new DecimalFormat("#0.00");

		Double forwardDirection = 0.0, reverseDirection = 0.0, neutralDirection = 0.0;
		if (machineCompassBHLList != null && !machineCompassBHLList.isEmpty()) {
			forwardDirection = machineCompassBHLList.stream().filter(o -> o.getForwardDirection() != null)
					.mapToDouble(o -> o.getForwardDirection()).sum();
			reverseDirection = machineCompassBHLList.stream().filter(o -> o.getReverseDirection() != null)
					.mapToDouble(o -> o.getReverseDirection()).sum();
			neutralDirection = machineCompassBHLList.stream().filter(o -> o.getNeutralDirection() != null)
					.mapToDouble(o -> o.getNeutralDirection()).sum();
		}
		log.info("Direction " + forwardDirection + "-" + reverseDirection + "-" + neutralDirection);

		DefaultPieDataset<String> dataSet1 = new DefaultPieDataset<String>();
		if (forwardDirection != null)
			dataSet1.setValue(dec.format(forwardDirection), forwardDirection);
		if (reverseDirection != null)
			dataSet1.setValue(dec.format(reverseDirection), reverseDirection);
		if (neutralDirection != null)
			dataSet1.setValue(dec.format(neutralDirection), neutralDirection);

		JFreeChart dutychart = ChartFactory.createPieChart("", dataSet1, true, true, false);
		dutychart.setBackgroundPaint(Color.white);
		dutychart.getPlot().setBackgroundPaint(Color.white);
		dutychart.getPlot().setOutlinePaint(null);
		dutychart.getPlot().setOutlineStroke(null);
		dutychart.setBorderVisible(false);
		dutychart.removeLegend();

		// Display value inside the chart
		PiePlot<?> dutychartPlot = (PiePlot<?>) dutychart.getPlot();
		dutychartPlot.setSimpleLabels(true);
		dutychartPlot.setShadowPaint(null);
		dutychartPlot.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 17));
		dutychartPlot.setLabelLinkPaint(new Color(255, 0, 0));
		dutychartPlot.setLabelShadowPaint(null);
		dutychartPlot.setLabelOutlinePaint(null);
		dutychartPlot.setLabelBackgroundPaint(null);
		dutychartPlot.setLabelPaint(Color.black);

		// Dynamic Color
		Color[] colors = { new Color(0, 212, 172), new Color(253, 192, 45), new Color(116, 94, 166) };
		// set each sections inside paint
		int j = 0;
		for (Object key : dataSet1.getKeys()) {
			dutychartPlot.setSectionPaint((Comparable<?>) key, colors[j % colors.length]);
			j++;
		}

		BufferedImage dutyChartBufferedImage = dutychart.createBufferedImage(400, 330);
		Image dutyChartImage = Image.getInstance(writer, dutyChartBufferedImage, 1.0f);
		PdfPTable enginePowerSummary = generateMCCompassSummary(dutyChartImage, dec.format(forwardDirection),
				dec.format(reverseDirection), dec.format(neutralDirection));
		return enginePowerSummary;
	}
	
	private PdfPTable generateMCCompassSummary(Image chartImage, String forward, String reverse, String netural)
			throws DocumentException, MalformedURLException, IOException {

		PdfPTable dutyCycleSummary = new PdfPTable(1);
		dutyCycleSummary.setWidthPercentage(100);
		dutyCycleSummary.getDefaultCell().setBorder(0);
		dutyCycleSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("M/C COMPASS",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dutyCycleSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);

		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase(
				"M/C Compass is combination of Forward Direction,Reverse Direction and Netural Direction in hrs.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0))));
		comments.setLeading(20f);
		content.addElement(comments);
		dutyCycleSummary.addCell(content);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 60f, 40f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.setPaddingTop(50f);
		imagecell.addElement(chartImage);

		chartTable.addCell(imagecell);

		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		emptyCell.setFixedHeight(17.5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon4.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(50);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(50);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon3.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(50);

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setBorder(0);
		legentContent.setPaddingTop(60f);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Forward Direction ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (forward != null) {
			valueText.add(new Phrase(forward + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);
		
		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Reverse Direction ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (reverse != null) {
			valueText.add(new Phrase(reverse + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Netural Direction ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (netural != null) {
			valueText.add(new Phrase(netural + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		dutyCycleSummary.addCell(chartTable);

		Image lightIcon = Image.getInstance(IMAGELOACATION + "/lightbulbicon.png");
		lightIcon.setAlignment(Image.ALIGN_LEFT);
		lightIcon.scalePercent(50);

		PdfPCell leftCell = new PdfPCell();
		leftCell.setPaddingLeft(17);
		leftCell.addElement(lightIcon);

		PdfPTable footerTable = new PdfPTable(1);
		footerTable.setWidthPercentage(100);
		footerTable.addCell(leftCell);

		PdfPCell newContent = new PdfPCell();
		newContent.setPadding(5);
		newContent.setBorder(0);

		return dutyCycleSummary;
	}
	
	private PdfPTable bhlDutyCycleBHLListChart(MachineDutyCycle machineDutyCycle, PdfWriter writer) throws MalformedURLException, DocumentException, IOException {
		DecimalFormat dec = new DecimalFormat("#0.00");
		DefaultPieDataset<String> dataSet1 = new DefaultPieDataset<String>();
		if(machineDutyCycle!=null) {
			if(machineDutyCycle.getAttachment()!=null)
				dataSet1.setValue(dec.format(machineDutyCycle.getAttachment()), machineDutyCycle.getAttachment());
			if(machineDutyCycle.getRoading()!=null)
				dataSet1.setValue(dec.format(machineDutyCycle.getRoading()), machineDutyCycle.getRoading());
			if(machineDutyCycle.getLoading()!=null)
				dataSet1.setValue(dec.format(machineDutyCycle.getLoading()), machineDutyCycle.getLoading());
			if(machineDutyCycle.getIdling()!=null)
				dataSet1.setValue(dec.format(machineDutyCycle.getIdling()), machineDutyCycle.getIdling());
			if(machineDutyCycle.getExcavation()!=null)
				dataSet1.setValue(dec.format(machineDutyCycle.getExcavation()), machineDutyCycle.getExcavation());
		}
		
		JFreeChart dutychart = ChartFactory.createPieChart("", dataSet1, true, true, false);
		dutychart.setBackgroundPaint(Color.white);
		dutychart.getPlot().setBackgroundPaint(Color.white);
		dutychart.getPlot().setOutlinePaint(null);
		dutychart.getPlot().setOutlineStroke(null);
		dutychart.setBorderVisible(false);
		dutychart.getPlot().setOutlineVisible(false);
		dutychart.removeLegend();
		
		// Display value inside the chart
		PiePlot<?> dutychartPlot = (PiePlot<?>) dutychart.getPlot();
		dutychartPlot.setSimpleLabels(true);
		dutychartPlot.setShadowPaint(null);
		dutychartPlot.setLabelFont(new java.awt.Font( "HELVETICA", java.awt.Font.PLAIN, 17 ));
		dutychartPlot.setLabelPaint(new Color(0,0,0));
		dutychartPlot.setLabelShadowPaint(null);
		dutychartPlot.setLabelOutlinePaint(null);
		dutychartPlot.setLabelBackgroundPaint(null);
		dutychartPlot.setLabelPaint(Color.black);
		
		// Dynamic Color
		Color[] colors = {new Color(236,129,65),new Color(0, 185, 240), new Color(0, 212, 172), new Color(116, 94, 166), new Color(251, 175, 37)}; 
        // set each sections inside paint
		int j = 0;
		for (Object key : dataSet1.getKeys()) {
			dutychartPlot.setSectionPaint((Comparable<?>) key, colors[j % colors.length]);
		    j++;
		}
		
		BufferedImage dutyChartBufferedImage = dutychart.createBufferedImage(400, 330);
		Image dutyChartImage = Image.getInstance(writer, dutyChartBufferedImage, 1.0f);

		PdfPTable dutyCycleSummary = generateDutyCycleSummary(dutyChartImage,machineDutyCycle);

		return dutyCycleSummary;
	}
	
	private PdfPTable generateDutyCycleSummary(Image chartImage, MachineDutyCycle machineDutyCycle) throws DocumentException, MalformedURLException, IOException {

		DecimalFormat dec = new DecimalFormat("#0.00");
		
		PdfPTable dutyCycleSummary = new PdfPTable(1);
		dutyCycleSummary.setWidthPercentage(100);
		dutyCycleSummary.getDefaultCell().setBorder(0);
		dutyCycleSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("DUTY CYCLE SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dutyCycleSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);
		//content.setFixedHeight(53f);
		
		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase(
				"Duty Cycle is combination of Attachments hrs,Excavation hrs, Loading hrs, Roading hrs and Idle hrs.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0 ))));
		comments.setLeading(20f);
		content.addElement(comments);
		dutyCycleSummary.addCell(content);
		//dutyCycleSummary.setSpacingAfter(2f);
		
		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 60f,40f });
		
		
		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.addElement(chartImage);
		chartTable.addCell(imagecell);
		
		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		emptyCell.setFixedHeight(5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(
				new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);
		
		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));
		
		
		Image icon5 = Image.getInstance(IMAGELOACATION+"/icon7.png");
		icon5.setAlignment(Image.ALIGN_LEFT);
		icon5.setWidthPercentage(50);
		
		Image icon4 = Image.getInstance(IMAGELOACATION+"/icon3.png");
		icon4.setAlignment(Image.ALIGN_LEFT);
		icon4.setWidthPercentage(50);
		
		Image icon2 = Image.getInstance(IMAGELOACATION+"/icon5.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(50);
		
		Image icon3 = Image.getInstance(IMAGELOACATION+"/icon4.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(50);
		
		Image icon1 = Image.getInstance(IMAGELOACATION+"/icon1.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(50);
		
		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setBorder(0);
		
		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Attachments Hrs ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		
		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon5);
		
		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);
		
		legentContent.addElement(legend);
		
		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if(machineDutyCycle!=null && machineDutyCycle.getAttachment()!=null)
		{
			valueText.add(
					new Phrase(dec.format(machineDutyCycle.getAttachment())+" Hrs", new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL,new BaseColor(29, 36, 38))));	
		}else {
			valueText.add("");
		}
		
		valueCell.addElement(valueText);
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);
		legentContent.addElement(legend);
		
		//Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		
		legend.addCell(emptyCell);
		
		legentContent.addElement(legend);
		
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Excavation Hrs ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (machineDutyCycle != null && machineDutyCycle.getExcavation() != null) {
			valueText.add(new Phrase(dec.format(machineDutyCycle.getExcavation()) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);
		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);
		
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Roading Hrs ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		
		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);
		
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);
		
		legentContent.addElement(legend);
		
		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if(machineDutyCycle!=null && machineDutyCycle.getRoading()!=null)
		{
			valueText.add(
					new Phrase(dec.format(machineDutyCycle.getRoading())+" Hrs", new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL,new BaseColor(29, 36, 38))));	
		}else {
			valueText.add("");
		}
		
		
		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Loading Hrs ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		
		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (machineDutyCycle != null && machineDutyCycle.getLoading() != null) {
			valueText.add(new Phrase(dec.format(machineDutyCycle.getLoading()) + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}
		valueCell.addElement(valueText);
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);
		
		legentContent.addElement(legend);
		
		//Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		
		legend.addCell(emptyCell);
		legentContent.addElement(legend);
		
		// 4th
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Idle Hrs ", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		
		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon4);
		
		
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);
		
		legentContent.addElement(legend);
		
		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if(machineDutyCycle!=null && machineDutyCycle.getIdling()!=null)
		{
			valueText.add(
					new Phrase(dec.format(machineDutyCycle.getIdling())+" Hrs", new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));	
		}else {
			valueText.add("");
		}
		
		
		valueCell.addElement(valueText);

		
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		dutyCycleSummary.addCell(chartTable);

		PdfPCell footerContent = new PdfPCell();
		footerContent.setBorder(0);
		footerContent.setPaddingLeft(30f);
		Paragraph footerText = new Paragraph();
		footerText.setAlignment(Element.ALIGN_LEFT);
		footerText.add(new Phrase("i) Fuel consumption is higher in Roading>Loading>Excavation>Idle hours\n" + 
				"ii) Fuel consumption can be reduced by switching off the engine during idle hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(255, 146, 0))));
		footerContent.addElement(footerText);
		
		Image lightIcon = Image.getInstance(IMAGELOACATION+"/lightbulbicon.png");
		lightIcon.setAlignment(Image.ALIGN_LEFT);
		lightIcon.scalePercent(50);
		
		PdfPCell leftCell = new PdfPCell();
		leftCell.setBorder(0);
		leftCell.setPaddingTop(10f);
		leftCell.addElement(lightIcon);
		
		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 5f, 95f });
		footerTable.addCell(leftCell);
		footerTable.addCell(footerContent);
		
		PdfPCell newContent = new PdfPCell();
		newContent.setPadding(5);
		newContent.setBorder(0);

		newContent.addElement(footerTable);
		
		dutyCycleSummary.addCell(newContent);
		
		return dutyCycleSummary;
		}
	
	private PdfPTable bhlEnginePowerBandChart(MachinePowerBand machinePowerBand, PdfWriter writer) throws IOException, DocumentException {

		DefaultPieDataset<String> dataSet = new DefaultPieDataset<String>();
		dataSet.setValue(machinePowerBand.getHighSpeed()+"%", machinePowerBand.getHighSpeed());
		dataSet.setValue(machinePowerBand.getLowSpeed()+"%", machinePowerBand.getLowSpeed());
		dataSet.setValue(machinePowerBand.getMediumSpeed()+"%", machinePowerBand.getMediumSpeed());
		JFreeChart chart = ChartFactory.createPieChart("", dataSet, true, true, false);
		chart.setBackgroundPaint(Color.white);
		chart.getPlot().setBackgroundPaint(Color.white);
		chart.getPlot().setOutlinePaint(null);
		chart.getPlot().setOutlineStroke(null);
		chart.setBorderVisible(false);
		
		
		chart.removeLegend();
		PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
		
		plot.setSimpleLabels(true);
		plot.setShadowPaint(null);
		plot.setLabelFont(new java.awt.Font( "HELVETICA", java.awt.Font.PLAIN, 35 ));
		plot.setLabelLinkPaint(new Color(255,0,0));
		plot.setLabelShadowPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelPaint(Color.black);
		
		// Dynamic Color
		Color[] chartcolors = {new Color(0, 212, 172), new Color(251, 175, 37), new Color(116, 94, 166)}; 
		// set each sections inside paint
		int i = 0;
		for (Object key : dataSet.getKeys()) {
			plot.setSectionPaint((Comparable<?>) key, chartcolors[i % chartcolors.length]);
			i++;
		}

		BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 620);
		Image ChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);
		PdfPTable enginePowerSummary = generateEnginePowerSummary(ChartImage);
		return enginePowerSummary;
	}
	
	private PdfPTable generateEnginePowerSummary(Image chartImage) throws DocumentException, MalformedURLException, IOException {
		PdfPTable enginePowerSummary = new PdfPTable(1);
		enginePowerSummary.setWidthPercentage(100);
		enginePowerSummary.getDefaultCell().setBorder(0);
		enginePowerSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("ENGINE POWER BAND SUMMARY",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		enginePowerSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);
		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase(
				"Machine generally operates in the three rpm bands. These are Low Speed Band(<1100 RPM), Medium Speed Band (1100-1600 RPM) and High Speed Band (>1600 RPM). Charts shows band wise utilisation of machine.",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(0, 0, 0))));
		comments.setLeading(20f);
		content.addElement(comments);
		
		enginePowerSummary.addCell(content);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 52f, 48f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.addElement(chartImage);

		chartTable.addCell(imagecell);

		PdfPCell circleCell = new PdfPCell();
		circleCell.setBackgroundColor(new BaseColor(29, 36, 38));
		circleCell.setBorder(0);
		circleCell.setCellEvent(new CellBorderEvent());

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPaddingTop(100f);
		legentContent.setBorder(0);

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		// icon1.scalePercent(0);
		icon1.setWidthPercentage(50);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon3.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(50);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon4.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(50);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(8);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		valueText.add(new Phrase("Low Speed Band",
				new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(29, 36, 38))));
		valueCell.addElement(valueText);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 12f, 88f });

		legend.addCell(rightCell);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// 2nd Title
		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		valueText.add(new Phrase("Medium Speed Band",
				new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(29, 36, 38))));
		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 12f, 88f });

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(8);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend.addCell(rightCell);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// 3rd Title
		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		valueText.add(new Phrase("High Speed Band",
				new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(29, 36, 38))));
		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 12f, 88f });

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(8);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend.addCell(rightCell);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		enginePowerSummary.addCell(chartTable);

		// Footer
		PdfPCell footerContent = new PdfPCell();
		footerContent.setBorder(0);
		footerContent.setPaddingLeft(20f);
		Paragraph footerText = new Paragraph();
		footerText.setAlignment(Element.ALIGN_LEFT);
		footerText.add(new Phrase(
				"High-Speed Band gives more productivity but consumes more fuel, use Low-speed Band (<1100  RPM) and Medium speed band (1100-1600RPM) for better fuel consumption.",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(255, 146, 0))));
		footerContent.addElement(footerText);

		Image lightIcon = Image.getInstance(IMAGELOACATION + "/lightbulbicon.png");
		lightIcon.setAlignment(Image.ALIGN_LEFT);
		lightIcon.scalePercent(50);

		PdfPCell leftCell = new PdfPCell();
		leftCell.setBorder(0);
		leftCell.setPaddingTop(10f);
		leftCell.addElement(lightIcon);

		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100);
		footerTable.setWidths(new float[] { 5f, 95f });
		footerTable.addCell(leftCell);
		footerTable.addCell(footerContent);

		PdfPCell newContent = new PdfPCell();
		newContent.setPadding(5);
		newContent.setBorder(0);

		newContent.addElement(footerTable);

		enginePowerSummary.addCell(newContent);

		return enginePowerSummary;

	}
	
	private PdfPTable bhlExcavationModesListChart(MachineExcavationMode machineExcavationMode, PdfWriter writer) {
		return null;
	}
	
	private PdfPTable bhlAverageRoadingChart(List<AverageSpeedRoading> averageRoading, PdfWriter writer)
			throws BadElementException, IOException {
		Image averageChartImage = null;
		if (averageRoading != null && !averageRoading.isEmpty()) {

			AverageSpeedRoading averageMax = averageRoading.stream().filter(c -> c.getAverageSpeedRoading() != null)
					.max(Comparator.comparing(AverageSpeedRoading::getAverageSpeedRoading)).orElse(null);

			// Daily Average Chart
			CategoryDataset averageDataSet = createAverageRoadingDataset(averageRoading);

			JFreeChart averageChart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					averageDataSet, PlotOrientation.VERTICAL, true, true, false);
			averageChart.setBackgroundPaint(Color.white);
			averageChart.getPlot().setBackgroundPaint(Color.white);
			averageChart.getPlot().setOutlinePaint(null);
			averageChart.getPlot().setOutlineStroke(null);
			averageChart.setBorderVisible(false);
			averageChart.removeLegend();

			CategoryPlot averageCPlot = (CategoryPlot) averageChart.getPlot();
			
			// set bar chart color
			((BarRenderer) averageCPlot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer averagerRender = (BarRenderer) averageChart.getCategoryPlot().getRenderer();
			averagerRender.setMaximumBarWidth(0.05);
			averagerRender.setSeriesPaint(0, new Color(110, 172, 253));
			averagerRender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			averagerRender.setDefaultItemLabelsVisible(true);

			final NumberAxis AveragerangeAxis = (NumberAxis) averageCPlot.getRangeAxis();
			Double averageMaxValue = (averageMax != null && averageMax.getAverageSpeedRoading() != null)
					? averageMax.getAverageSpeedRoading()
					: 10;
			log.info("Maximum Fuel :" + averageMaxValue);
			setRangeAndTickUnit(AveragerangeAxis, averageMaxValue);
			AveragerangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setTickLabelPaint(Color.BLACK);
			AveragerangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage averageChartBufferedImage = averageChart.createBufferedImage(600, 450);
			averageChartImage = Image.getInstance(writer, averageChartBufferedImage, 1.0f);
		}
		PdfPTable averageSpeedTable = generateAverageSpeedChart(averageChartImage, writer);
		return averageSpeedTable;

	}
	
	private PdfPTable generateAverageSpeedChart(Image averageChartImage, PdfWriter writer) {

		PdfPTable averageSpeedSummary = new PdfPTable(1);
		averageSpeedSummary.setWidthPercentage(100);
		averageSpeedSummary.getDefaultCell().setBorder(0);
		averageSpeedSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("AVERAGE SPEED(KM/Hr)",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		averageSpeedSummary.addCell(header);

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		averageChartImage.scaleAbsolute(400f, 250f);
		averageChartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(averageChartImage);

		averageSpeedSummary.addCell(imagecell);

		return averageSpeedSummary;
	}
	
	private CategoryDataset createAverageRoadingDataset(List<AverageSpeedRoading> averageRoading) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
		for (AverageSpeedRoading average : averageRoading) {
			if (average.getAverageSpeedRoading() != null) {
				dataset.addValue(average.getAverageSpeedRoading(), "",
						average.getDay().getDate() + " " + inputFormat.format(average.getDay()));
			}

		}
		return dataset;

	}
	
	private PdfPTable bhlDistanceRoadingChart(List<DistanceTraveledRoading> distanceRoading, PdfWriter writer)
			throws BadElementException, IOException {
		Image ChartImage = null;

		if (distanceRoading != null && !distanceRoading.isEmpty()) {
			DistanceTraveledRoading roadingMax = distanceRoading.stream()
					.filter(c -> c.getDistanceTraveledRoading() != null)
					.max(Comparator.comparing(DistanceTraveledRoading::getDistanceTraveledRoading)).orElse(null);

			CategoryDataset dataset = createRoadingDistanceDataset(distanceRoading);

			JFreeChart chart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);
			chart.getPlot().setBackgroundPaint(Color.white);
			chart.getPlot().setOutlinePaint(null);
			chart.getPlot().setOutlineStroke(null);
			chart.setBorderVisible(false);

			chart.getCategoryPlot().getRangeAxis().setLowerBound(10);

			CategoryPlot cplot = (CategoryPlot) chart.getPlot();
			
			// set bar chart color
			((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

			renderer.setMaximumBarWidth(0.05);
			renderer.setSeriesPaint(0, new Color(173, 162, 255));
			renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setDefaultItemLabelsVisible(true);
			chart.removeLegend();

			final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
			Double fuelMaxValue = (roadingMax != null && roadingMax.getDistanceTraveledRoading() != null)
					? roadingMax.getDistanceTraveledRoading()
					: 100;
			rangeAxis.setRange(0, fuelMaxValue);
			setRangeAndTickUnit(rangeAxis, fuelMaxValue);
			rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setTickLabelPaint(Color.BLACK);
			rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 450);
			ChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);

		}
		PdfPTable roadingDistanceTable = generateRoadindDistanceChart(ChartImage, writer);

		return roadingDistanceTable;

	}
	
	private PdfPTable generateRoadindDistanceChart(Image chartImage, PdfWriter writer) {

		PdfPTable roadindDistanceSummary = new PdfPTable(1);
		roadindDistanceSummary.setWidthPercentage(100);
		roadindDistanceSummary.getDefaultCell().setBorder(0);
		roadindDistanceSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("ROADING DISTANCE IN KM",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		roadindDistanceSummary.addCell(header);

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		chartImage.scaleAbsolute(400f, 250f);
		chartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(chartImage);

		roadindDistanceSummary.addCell(imagecell);

		return roadindDistanceSummary;
	}
	
	private CategoryDataset createRoadingDistanceDataset(List<DistanceTraveledRoading> distanceRoading) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");

		for (DistanceTraveledRoading roading : distanceRoading) {
			if (roading.getDistanceTraveledRoading() != null) {
				dataset.addValue(roading.getDistanceTraveledRoading(), "",
						roading.getDay().getDate() + " " + inputFormat.format(roading.getDay()));
			}

		}
		return dataset;

	}
	
	private PdfPTable bhlFuelConsumptionExcavationChart(List<FuelConsumptionExcavation> excavationList,
			PdfWriter writer) throws IOException, DocumentException {

		// Fuel Consumption By Excavation Mode
		DecimalFormat dec = new DecimalFormat("#0.00");
		DefaultPieDataset<String> excavationDataSet = new DefaultPieDataset<String>();
		Double excavationEco = 0.0, excavationStd = 0.0, excavationPlus = 0.0;
		for (int j = 0; j < excavationList.size(); j++) {
			excavationEco = excavationEco
					+ (excavationList.get(j).getEconomyMode() != null ? excavationList.get(j).getEconomyMode() : 0);
			excavationStd = excavationStd
					+ (excavationList.get(j).getStandardMode() != null ? excavationList.get(j).getStandardMode() : 0);
			excavationPlus = excavationPlus
					+ (excavationList.get(j).getPlusMode() != null ? excavationList.get(j).getPlusMode() : 0);
		}
		if (excavationList != null) {

			excavationDataSet.setValue(dec.format(excavationEco), excavationEco);

			excavationDataSet.setValue(dec.format(excavationStd), excavationStd);

			excavationDataSet.setValue(dec.format(excavationPlus), excavationPlus);

		}

		JFreeChart excavationchart = ChartFactory.createPieChart("", excavationDataSet, true, true, false);
		excavationchart.setBackgroundPaint(Color.white);
		excavationchart.getPlot().setBackgroundPaint(Color.white);
		excavationchart.getPlot().setOutlinePaint(null);
		excavationchart.getPlot().setOutlineStroke(null);
		excavationchart.setBorderVisible(false);
		excavationchart.getPlot().setOutlineVisible(false);
		excavationchart.removeLegend();

		// Display value inside the chart
		PiePlot<?> excavationchartPlot = (PiePlot<?>) excavationchart.getPlot();
		excavationchartPlot.setSimpleLabels(true);
		excavationchartPlot.setShadowPaint(null);
		excavationchartPlot.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 30));
		excavationchartPlot.setLabelPaint(new Color(0, 0, 0));
		excavationchartPlot.setLabelShadowPaint(null);
		excavationchartPlot.setLabelOutlinePaint(null);
		excavationchartPlot.setLabelBackgroundPaint(null);
		excavationchartPlot.setLabelPaint(Color.black);

		// Dynamic Color
		Color[] colors = { new Color(236, 129, 65), new Color(0, 185, 240), new Color(253, 192, 45) };
		// set each sections inside paint
		int j = 0;
		for (Object key : excavationDataSet.getKeys()) {
			excavationchartPlot.setSectionPaint((Comparable<?>) key, colors[j % colors.length]);
			j++;
		}

		BufferedImage excavationBufferedImage = excavationchart.createBufferedImage(600, 620);
		Image excavationChartImage = Image.getInstance(writer, excavationBufferedImage, 1.0f);

		PdfPTable fuelConsumptionByExcavation = generateFuelConsumptionByExcavation(excavationChartImage, excavationEco,
				excavationStd, excavationPlus);
		return fuelConsumptionByExcavation;

	}
	
	private PdfPTable generateFuelConsumptionByExcavation(Image excavationChartImage, Double excavationEco,
			Double excavationStd, Double excavationPlus) throws DocumentException, MalformedURLException, IOException {

		PdfPTable fuelExcavationSummary = new PdfPTable(1);
		fuelExcavationSummary.setWidthPercentage(100);
		fuelExcavationSummary.getDefaultCell().setBorder(0);
		fuelExcavationSummary.setTableEvent(new BorderEvent());

		DecimalFormat dec = new DecimalFormat("#0.00");

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("FUEL CONSUMPTION BY EXCAVATION MODES",
				new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		fuelExcavationSummary.addCell(header);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 52f, 48f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.addElement(excavationChartImage);

		chartTable.addCell(imagecell);

		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		emptyCell.setFixedHeight(17.5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon7.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(40);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon5.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(40);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(40);

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setBorder(0);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Excavation Eco Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (excavationEco != null) {
			valueText.add(new Phrase(dec.format(excavationEco) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Excavation Std Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (excavationStd != null) {
			valueText.add(new Phrase(dec.format(excavationStd) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Excavation Plus Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (excavationPlus != null) {
			valueText.add(new Phrase(dec.format(excavationPlus) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		fuelExcavationSummary.addCell(chartTable);

		return fuelExcavationSummary;

	}
	
	private PdfPTable bhlFuelConsumptionDutyChart(List<FuelConsumptionDuty> fuelConsumptionDuty, PdfWriter writer)
			throws IOException, DocumentException {

		Double excavationMode = 0.0, loadingMode = 0.0, roadingMode = 0.0, idingMode = 0.0;
		DecimalFormat dec = new DecimalFormat("#0.00");
		for (int i = 0; i < fuelConsumptionDuty.size(); i++) {
			excavationMode = excavationMode + (fuelConsumptionDuty.get(i).getExcavationMode() != null
					? fuelConsumptionDuty.get(i).getExcavationMode()
					: 0);
			loadingMode = loadingMode
					+ (fuelConsumptionDuty.get(i).getLoadingMode() != null ? fuelConsumptionDuty.get(i).getLoadingMode()
							: 0);
			roadingMode = roadingMode
					+ (fuelConsumptionDuty.get(i).getRoadindMode() != null ? fuelConsumptionDuty.get(i).getRoadindMode()
							: 0);
			idingMode = idingMode
					+ (fuelConsumptionDuty.get(i).getIdleMode() != null ? fuelConsumptionDuty.get(i).getIdleMode() : 0);
		}

		DefaultPieDataset<String> dataSet = new DefaultPieDataset<String>();
		dataSet.setValue(dec.format(excavationMode), excavationMode);
		dataSet.setValue(dec.format(loadingMode), loadingMode);
		dataSet.setValue(dec.format(roadingMode), roadingMode);
		dataSet.setValue(dec.format(idingMode), idingMode);

		JFreeChart dutychart = ChartFactory.createPieChart("", dataSet, true, true, false);
		dutychart.setBackgroundPaint(Color.white);
		dutychart.getPlot().setBackgroundPaint(Color.white);
		dutychart.getPlot().setOutlinePaint(null);
		dutychart.getPlot().setOutlineStroke(null);
		dutychart.setBorderVisible(false);
		dutychart.removeLegend();

		PiePlot<?> plot = (PiePlot<?>) dutychart.getPlot();

		plot.setSimpleLabels(true);
		plot.setShadowPaint(null);
		plot.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 30));
		plot.setLabelLinkPaint(new Color(255, 0, 0));
		plot.setLabelShadowPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelPaint(Color.black);

		// Dynamic Color
		Color[] chartcolors = { new Color(236, 129, 65), new Color(0, 185, 240), new Color(253, 192, 45),
				new Color(165, 165, 165) };
		// set each sections inside paint
		int i = 0;
		for (Object key : dataSet.getKeys()) {
			plot.setSectionPaint((Comparable<?>) key, chartcolors[i % chartcolors.length]);
			i++;
		}

		BufferedImage dutyBufferedImage = dutychart.createBufferedImage(600, 620);
		Image dutyChartImage = Image.getInstance(writer, dutyBufferedImage, 1.0f);
		PdfPTable fuelConsumptionByDuty = generateFuelConsumptionByDuty(dutyChartImage, excavationMode, loadingMode,
				roadingMode, idingMode);
		return fuelConsumptionByDuty;

	}
	
	private PdfPTable wlsFuelChart(List<FuelConsumptionResponse> fuelConsumption, PdfWriter writer)
			throws IOException, DocumentException {

		Image ChartImage = null;
		String fDate = null;
		String tDate = null;
		Double fuelSum = null;
		if (fuelConsumption != null && !fuelConsumption.isEmpty()) {

			fuelSum = fuelConsumption.stream().filter(c -> c.getTotalFuelUsed() != null)
					.mapToDouble(x -> x.getTotalFuelUsed()).sum();

			FuelConsumptionResponse fuelMax = fuelConsumption.stream().filter(c -> c.getTotalFuelUsed() != null)
					.max(Comparator.comparing(FuelConsumptionResponse::getTotalFuelUsed)).orElse(null);

			CategoryDataset dataset = createDataset(fuelConsumption);

			JFreeChart chart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);
			chart.getPlot().setBackgroundPaint(Color.white);
			chart.getPlot().setOutlinePaint(null);
			chart.getPlot().setOutlineStroke(null);
			chart.setBorderVisible(false);

			chart.getCategoryPlot().getRangeAxis().setLowerBound(10);

			CategoryPlot cplot = (CategoryPlot) chart.getPlot();
			
			// set bar chart color
			((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

			renderer.setMaximumBarWidth(0.05);
			renderer.setSeriesPaint(0, new Color(173, 162, 255));
			renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setDefaultItemLabelsVisible(true);
			chart.removeLegend();

			final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
			Double fuelMaxValue = (fuelMax != null && fuelMax.getTotalFuelUsed() != null) ? fuelMax.getTotalFuelUsed(): 10;
			setRangeAndTickUnit(rangeAxis, fuelMaxValue);

			rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setTickLabelPaint(Color.BLACK);
			rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 450);
			ChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);

			SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
			SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
			int size = fuelConsumption.size();
			fDate = fuelConsumption.get(0).getDate().getDate() + " "+ inputFormat.format(fuelConsumption.get(0).getDate()) + " "
					+ yearFormat.format(fuelConsumption.get(0).getDate());
			tDate = fuelConsumption.get(size - 1).getDate().getDate() + " "+ inputFormat.format(fuelConsumption.get(size - 1).getDate()) + " "
					+ yearFormat.format(fuelConsumption.get(size - 1).getDate());

		}
		PdfPTable fuelchart = generateFuelConsumptionChart(ChartImage, fDate, tDate, fuelSum, writer);
		return fuelchart;
	}
	
	private CategoryDataset createDataset(List<FuelConsumptionResponse> fuelList) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");

		for (FuelConsumptionResponse fuel : fuelList) {
			if (fuel.getTotalFuelUsed() != null) {
				dataset.addValue(fuel.getTotalFuelUsed(), "",
						fuel.getDate().getDate() + " " + inputFormat.format(fuel.getDate()));
			}
		}
		return dataset;
	}
	
	private PdfPTable generateFuelConsumptionByDuty(Image dutyChartImage, Double excavationMode, Double loadingMode,
			Double roadingMode, Double idingMode) throws DocumentException, MalformedURLException, IOException {

		PdfPTable fuelDutySummary = new PdfPTable(1);
		fuelDutySummary.setWidthPercentage(100);
		fuelDutySummary.getDefaultCell().setBorder(0);
		fuelDutySummary.setTableEvent(new BorderEvent());

		DecimalFormat dec = new DecimalFormat("#0.00");

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("FUEL CONSUMPTION BY DUTY CYCLE",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		fuelDutySummary.addCell(header);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 52f, 48f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.addElement(dutyChartImage);

		chartTable.addCell(imagecell);

		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		emptyCell.setFixedHeight(17.5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon7.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(40);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon5.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(40);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(40);

		Image icon4 = Image.getInstance(IMAGELOACATION + "/icon6.png");
		icon4.setAlignment(Image.ALIGN_LEFT);
		icon4.setWidthPercentage(40);

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setBorder(0);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Excavation Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (excavationMode != null) {
			valueText.add(new Phrase(dec.format(excavationMode) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Loading Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (loadingMode != null) {
			valueText.add(new Phrase(dec.format(loadingMode) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Roading Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (roadingMode != null) {
			valueText.add(new Phrase(dec.format(roadingMode) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		// 4th
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Idle Mode ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon4);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (idingMode != null) {
			valueText.add(new Phrase(dec.format(idingMode) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		fuelDutySummary.addCell(chartTable);

		return fuelDutySummary;

	}
	
	private PdfPTable generateFuelConsumptionChart(Image chartImage,String fdate,String tdate, Double fuelSum, PdfWriter writer) throws DocumentException, MalformedURLException, IOException {PdfPTable fuelConsumptionSummary = new PdfPTable(1);
	fuelConsumptionSummary.setWidthPercentage(100);
	fuelConsumptionSummary.getDefaultCell().setBorder(0);
	fuelConsumptionSummary.setTableEvent(new BorderEvent());
	
	PdfPCell header = new PdfPCell();
	header.setBorder(0);
	header.setPadding(20);
	header.setBackgroundColor(new BaseColor(29, 36, 38));
	Paragraph title = new Paragraph();
	title.setAlignment(Element.ALIGN_LEFT);
	title.add(new Phrase("FUEL USED IN LITERS",
			new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
	header.addElement(title);
	fuelConsumptionSummary.addCell(header);
	
	PdfPCell content = new PdfPCell();
	content.setBorder(0);
	content.setPaddingRight(10f);
	
	Image fuelLevel = Image.getInstance(IMAGELOACATION+"/fuelLevel.png");
	fuelLevel.setAlignment(Image.ALIGN_RIGHT);
	fuelLevel.setWidthPercentage(5);
	
	content.setPaddingTop(2);
	content.setBorder(0);
	content.addElement(fuelLevel);
	
	fuelConsumptionSummary.addCell(content);

	PdfPCell imagecell = new PdfPCell();
	imagecell.setBorder(0);
	chartImage.scaleAbsolute(400f, 250f);
	chartImage.setAlignment(Element.ALIGN_CENTER);
	imagecell.addElement(chartImage);
	
	fuelConsumptionSummary.addCell(imagecell);
	
	PdfPTable chartTable = new PdfPTable(5);
	chartTable.setWidthPercentage(100);
	chartTable.getDefaultCell().setBorder(0);
	chartTable.setPaddingTop(10f);
	chartTable.setWidths(new float[] { 15f,45f,20f,8f,12f });
	
	PdfPCell cell = new PdfPCell();
	cell.setBorder(0);
	cell.setBackgroundColor(new BaseColor(242, 241, 250));
	cell.setPaddingLeft(70f);
	Paragraph celltitle = new Paragraph();
	cell.setPaddingTop(10f);
	celltitle.add(new Phrase(new Phrase("Fuel Consumption in Liters", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,new BaseColor(29, 36, 38)))));
	cell.addElement(celltitle);
	
	PdfPCell cellValue = new PdfPCell();
	cellValue.setBorder(0);
	cellValue.setBackgroundColor(new BaseColor(242, 241, 250));
	cellValue.setPaddingTop(10f);
	Paragraph values = new Paragraph();
	values.add(new Phrase(new Phrase(""+decfor.format(fuelSum).toString(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,new BaseColor(29, 36, 38)))));
	values.setAlignment(Element.ALIGN_CENTER);
	cellValue.addElement(values);
	
	PdfPCell cell2 = new PdfPCell();
	cell2.setBorder(0);
	cell2.setBackgroundColor(new BaseColor(242, 241, 250));
	cell2.setPaddingLeft(85f);
	Paragraph cell2title = new Paragraph();
	cell2title.add(new Phrase(new Phrase(fdate+" - "+tdate, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL,new BaseColor(29, 36, 38)))));
	cell2.addElement(cell2title);
	
	PdfPCell cell2Value = new PdfPCell();
	cell2Value.setBorder(0);
	cell2Value.setBackgroundColor(new BaseColor(242, 241, 250));

	Paragraph values2 = new Paragraph();
	values2.setAlignment(Element.ALIGN_CENTER);
	values2.add(new Phrase(new Phrase("Liters", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL,new BaseColor(29, 36, 38)))));
	cell2Value.addElement(values2);
	
	PdfPCell emptyCell = new PdfPCell();
	emptyCell.setBorder(0);
	emptyCell.setBackgroundColor(new BaseColor(242, 241, 250));
	Paragraph emptyText = new Paragraph();
	emptyText.add(new Phrase(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,new BaseColor(29, 36, 38)))));
	emptyText.setAlignment(Element.ALIGN_LEFT);
	emptyCell.addElement(emptyText);
	
	PdfPCell emptyCell2 = new PdfPCell();
	emptyCell2.setBorder(0);
	Paragraph emptyText2 = new Paragraph();
	emptyText2.add(new Phrase(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,new BaseColor(29, 36, 38)))));
	emptyText2.setAlignment(Element.ALIGN_LEFT);
	emptyCell2.addElement(emptyText2);
	
	chartTable.addCell("");
	chartTable.addCell(cell);
	chartTable.addCell(cellValue);
	chartTable.addCell(emptyCell);
	chartTable.addCell("");
	
	chartTable.addCell("");
	chartTable.addCell(cell2);
	chartTable.addCell(cell2Value);
	chartTable.addCell(emptyCell);
	chartTable.addCell("");
	
	chartTable.addCell("");
	chartTable.addCell(emptyCell);
	chartTable.addCell(emptyCell);
	chartTable.addCell(emptyCell);
	chartTable.addCell("");
	
	fuelConsumptionSummary.addCell(chartTable);
	fuelConsumptionSummary.addCell(emptyCell2);
	return fuelConsumptionSummary;
	}
	
	private void setRangeAndTickUnit(final NumberAxis AveragerangeAxis, Double averageMaxValue) {
		if(averageMaxValue.intValue()<=2) {
			AveragerangeAxis.setRange(0, 3);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(1));
		}else if(averageMaxValue.intValue()>2 && averageMaxValue.intValue()<=5) {
			AveragerangeAxis.setRange(0, 8);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(1));
		}else if(averageMaxValue.intValue()>5 && averageMaxValue.intValue()<=8) {
			AveragerangeAxis.setRange(0, 10);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(2));
		}else if(averageMaxValue.intValue()>8 && averageMaxValue.intValue()<=10) {
			AveragerangeAxis.setRange(0, 12);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(2));
		}
		else if(averageMaxValue.intValue()>10 && averageMaxValue.intValue()<=15) {
			AveragerangeAxis.setRange(0, 20);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(2));
		}
		else if(averageMaxValue.intValue()>15 && averageMaxValue.intValue()<=20) {
			AveragerangeAxis.setRange(0, 24);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(2));
		}else if(averageMaxValue.intValue()>20 && averageMaxValue.intValue()<=30) {
			AveragerangeAxis.setRange(0, 32);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}else if(averageMaxValue.intValue()>30 && averageMaxValue.intValue()<=40) {
			AveragerangeAxis.setRange(0, 41);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}
		else if(averageMaxValue.intValue()>40 && averageMaxValue.intValue()<=50) {
			AveragerangeAxis.setRange(0, 51);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}else if(averageMaxValue.intValue()>50 && averageMaxValue.intValue()<=60) {
			AveragerangeAxis.setRange(0, 61);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}else if(averageMaxValue.intValue()>60 && averageMaxValue.intValue()<=70) {
			AveragerangeAxis.setRange(0, 71);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}
		else if(averageMaxValue.intValue()>70 && averageMaxValue.intValue()<=80) {
			AveragerangeAxis.setRange(0, 81);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}
		else if(averageMaxValue.intValue()>80 && averageMaxValue.intValue()<=90) {
			AveragerangeAxis.setRange(0, 91);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(5));
		}
		else if(averageMaxValue.intValue()>90 && averageMaxValue.intValue()<=100) {
			AveragerangeAxis.setRange(0, 102);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(10));
		}else if(averageMaxValue.intValue()>100 && averageMaxValue.intValue()<=200) {
			AveragerangeAxis.setRange(0, 202);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(20));
		}else if(averageMaxValue.intValue()>200 && averageMaxValue.intValue()<=300) {
			AveragerangeAxis.setRange(0, 302);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(30));
		}else if(averageMaxValue.intValue()>300 && averageMaxValue.intValue()<=400) {
			AveragerangeAxis.setRange(0, 402);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(40));
		}else if(averageMaxValue.intValue()>400 && averageMaxValue.intValue()<=500) {
			AveragerangeAxis.setRange(0, 502);
			AveragerangeAxis.setTickUnit(new NumberTickUnit(50));
		}else if(averageMaxValue.intValue()>500) {
			AveragerangeAxis.setRange(0, averageMaxValue.intValue()+10);
			Integer tickUnit = averageMaxValue.intValue()/10;
			AveragerangeAxis.setTickUnit(new NumberTickUnit(tickUnit));
		}
	}
	
	private void wlsReport(Document document, PdfWriter writer, String fromDate, String toDate,
			PdfReportResponse pdfReportResponse, List<AdvanceReportChart> chartList)
			throws DocumentException, MalformedURLException, IOException {
		PdfPTable outerTable = new PdfPTable(3);
		outerTable.setWidthPercentage(TABLEWIDTH);
		outerTable.setWidths(new float[] { 49f, 2f, 49f });
		outerTable.getDefaultCell().setBorder(0);

		PdfPTable fuelConsumption = null;
		PdfPTable fuelAverage = null;
		PdfPTable gearUtilization = null;
		PdfPTable powerBands = null;

		log.info("chart " + chartList.size());
		List<PdfPTable> reportList = new ArrayList<>();
		for (AdvanceReportChart chart : chartList) {
			if (chart.getChart().equalsIgnoreCase("WlsFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelConsumption = wlsFuelChart(pdfReportResponse.getWLSReport().getFuelConsumption(), writer);
				reportList.add(fuelConsumption);
			} else if (chart.getChart().equalsIgnoreCase("AverageFuelConsumption")) {
				log.info("chart " + chart.getChart());
				fuelAverage = wlsFuelAverageChart(pdfReportResponse.getWLSReport().getDailyFuelAverage(), writer);
				reportList.add(fuelAverage);
			} else if (chart.getChart().equalsIgnoreCase("WlsGearUtilization")) {
				log.info("chart " + chart.getChart());
				gearUtilization = wlsGearUtilizationChart(pdfReportResponse.getWLSReport().getWlsGearUtilization(),
						writer);
				reportList.add(gearUtilization);
			} else if (chart.getChart().equalsIgnoreCase("FuelPowerBand")) {
				log.info("chart " + chart.getChart());
				powerBands = wlsPowerBandChart(pdfReportResponse.getWLSReport().getFuelPowerBand(), writer);
				reportList.add(powerBands);
			}
		}
		log.info("New :" + reportList.size() + "-" + reportList.get(0) + "-" + reportList.get(1));
		if (chartList.size() == 1) {
			log.info("chart added" + 1);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 2) {
			log.info("chart added" + 2);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
		} else if (chartList.size() == 3) {
			log.info("chart added" + 3);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(new Phrase(" "));
		} else if (chartList.size() == 4) {
			log.info("chart added" + 4);
			outerTable.addCell(reportList.get(0));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(1));
			outerTable.addCell(reportList.get(2));
			outerTable.addCell(new Phrase(" "));
			outerTable.addCell(reportList.get(3));
		}
		document.add(outerTable);
	}
	
	private PdfPTable wlsFuelAverageChart(List<FuelConsumptionResponse> dailyFuelAverage, PdfWriter writer)
			throws IOException, DocumentException {

		Image averageChartImage = null;
		String fDate = null;
		String tDate = null;
		Double averageSum = null;
		Integer totalList = 0;

		if (dailyFuelAverage != null && !dailyFuelAverage.isEmpty()) {

			averageSum = dailyFuelAverage.stream().filter(c -> c.getTotalFuelUsed() != null)
					.mapToDouble(x -> x.getTotalFuelUsed()).sum();

			for (FuelConsumptionResponse fuel : dailyFuelAverage) {
				if (fuel.getTotalFuelUsed() != null && fuel.getTotalFuelUsed() != 0 && fuel.getTotalFuelUsed() != 0.0) {
					totalList = totalList + 1;
				}
			}

			averageSum = averageSum / totalList;

			FuelConsumptionResponse averageMax = dailyFuelAverage.stream().filter(c -> c.getTotalFuelUsed() != null)
					.max(Comparator.comparing(FuelConsumptionResponse::getTotalFuelUsed)).orElse(null);

			// Daily Average Chart
			CategoryDataset averageDataSet = createDataset(dailyFuelAverage);

			JFreeChart averageChart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					averageDataSet, PlotOrientation.VERTICAL, true, true, false);
			averageChart.setBackgroundPaint(Color.white);
			averageChart.getPlot().setBackgroundPaint(Color.white);
			averageChart.getPlot().setOutlinePaint(null);
			averageChart.getPlot().setOutlineStroke(null);
			averageChart.setBorderVisible(false);
			averageChart.removeLegend();

			CategoryPlot averageCPlot = (CategoryPlot) averageChart.getPlot();

			((BarRenderer) averageCPlot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer averagerRender = (BarRenderer) averageChart.getCategoryPlot().getRenderer();
			averagerRender.setMaximumBarWidth(0.05);
			averagerRender.setSeriesPaint(0, new Color(110, 172, 253));
			averagerRender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			averagerRender.setDefaultItemLabelsVisible(true);

			final NumberAxis AveragerangeAxis = (NumberAxis) averageCPlot.getRangeAxis();
			Double averageMaxValue = (averageMax != null && averageMax.getTotalFuelUsed() != null)? averageMax.getTotalFuelUsed(): 10;
			setRangeAndTickUnit(AveragerangeAxis, averageMaxValue);
			AveragerangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setTickLabelPaint(Color.BLACK);
			AveragerangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			AveragerangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage averageChartBufferedImage = averageChart.createBufferedImage(600, 450);
			averageChartImage = Image.getInstance(writer, averageChartBufferedImage, 1.0f);
		}
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
		int size = dailyFuelAverage.size();
		fDate = dailyFuelAverage.get(0).getDate().getDate() + " "
				+ inputFormat.format(dailyFuelAverage.get(0).getDate()) + " "
				+ yearFormat.format(dailyFuelAverage.get(0).getDate());
		tDate = dailyFuelAverage.get(size - 1).getDate().getDate() + " "
				+ inputFormat.format(dailyFuelAverage.get(size - 1).getDate()) + " "
				+ yearFormat.format(dailyFuelAverage.get(size - 1).getDate());
		log.info("average " + averageSum);
		PdfPTable dutyCycleSummary = generateDailyAverageChart(averageChartImage, fDate, tDate, averageSum, writer);
		return dutyCycleSummary;
	}
	
	private PdfPTable generateDailyAverageChart(Image chartImage, String fDate, String tDate, Double averageSum,
			PdfWriter writer) throws DocumentException, FileNotFoundException, IOException {

		PdfPTable dailyAverageSummary = new PdfPTable(1);
		dailyAverageSummary.setWidthPercentage(100);
		dailyAverageSummary.getDefaultCell().setBorder(0);
		dailyAverageSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("FUEL AVERAGE IN LITERS/HRS",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dailyAverageSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingRight(10f);

		Image fuelLevel = Image.getInstance(IMAGELOACATION + "/avgFuel.png");
		fuelLevel.setAlignment(Image.ALIGN_RIGHT);
		fuelLevel.setWidthPercentage(5);

		content.setPaddingTop(2);
		content.setBorder(0);
		content.addElement(fuelLevel);

		dailyAverageSummary.addCell(content);

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		chartImage.scaleAbsolute(400f, 250f);
		chartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(chartImage);

		dailyAverageSummary.addCell(imagecell);

		PdfPTable chartTable = new PdfPTable(5);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setPaddingTop(10f);
		chartTable.setWidths(new float[] { 15f, 45f, 20f, 8f, 12f });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setBackgroundColor(new BaseColor(240, 243, 250));
		cell.setPaddingLeft(70f);
		Paragraph celltitle = new Paragraph();
		cell.setPaddingTop(10f);
		celltitle.add(new Phrase(new Phrase("Average Fuel Consumption",
				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(29, 36, 38)))));
		cell.addElement(celltitle);

		PdfPCell cellValue = new PdfPCell();
		cellValue.setBorder(0);
		cellValue.setBackgroundColor(new BaseColor(240, 243, 250));
		cellValue.setPaddingTop(10f);
		Paragraph values = new Paragraph();
		if (!averageSum.isNaN()) {
			values.add(new Phrase(new Phrase("" + decfor.format(averageSum).toString(),
					new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(29, 36, 38)))));
		} else {
			values.add(new Phrase(new Phrase("" + decfor.format(0).toString(),
					new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(29, 36, 38)))));
		}

		values.setAlignment(Element.ALIGN_CENTER);
		cellValue.addElement(values);

		PdfPCell cell2 = new PdfPCell();
		cell2.setBorder(0);
		cell2.setBackgroundColor(new BaseColor(240, 243, 250));
		cell2.setPaddingLeft(85f);
		Paragraph cell2title = new Paragraph();
		cell2title.add(new Phrase(new Phrase(fDate + " - " + tDate,
				new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(29, 36, 38)))));
		cell2.addElement(cell2title);

		PdfPCell cell2Value = new PdfPCell();
		cell2Value.setBorder(0);
		cell2Value.setBackgroundColor(new BaseColor(240, 243, 250));
		// cell2Value.setPaddingTop(10f);
		Paragraph values2 = new Paragraph();
		values2.setAlignment(Element.ALIGN_CENTER);
		values2.add(new Phrase(new Phrase("Liters/Hrs",
				new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(29, 36, 38)))));
		cell2Value.addElement(values2);

		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setBorder(0);
		emptyCell.setBackgroundColor(new BaseColor(240, 243, 250));
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(
				new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(29, 36, 38)))));
		emptyText.setAlignment(Element.ALIGN_LEFT);
		emptyCell.addElement(emptyText);

		PdfPCell emptyCell2 = new PdfPCell();
		emptyCell2.setBorder(0);
		Paragraph emptyText2 = new Paragraph();
		emptyText2.add(new Phrase(
				new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(29, 36, 38)))));
		emptyText2.setAlignment(Element.ALIGN_LEFT);
		emptyCell2.addElement(emptyText2);

		chartTable.addCell("");
		chartTable.addCell(cell);
		chartTable.addCell(cellValue);
		chartTable.addCell(emptyCell);
		chartTable.addCell("");

		chartTable.addCell("");
		chartTable.addCell(cell2);
		chartTable.addCell(cell2Value);
		chartTable.addCell(emptyCell);
		chartTable.addCell("");

		chartTable.addCell("");
		chartTable.addCell(emptyCell);
		chartTable.addCell(emptyCell);
		chartTable.addCell(emptyCell);
		chartTable.addCell("");

		dailyAverageSummary.addCell(chartTable);
		dailyAverageSummary.addCell(emptyCell2);

		return dailyAverageSummary;
	}
	
	private PdfPTable wlsGearUtilizationChart(List<WheelLoaderGearUtilization> wlsGearUtilization, PdfWriter writer)
			throws IOException, DocumentException {
		DecimalFormat dec = new DecimalFormat("#0.00");

		Image gearChartImage = null;
		List<Double> maxValules = new ArrayList<>();
		Double forwardFirstGear = 0.0;
		Double forwardSecondGear = 0.0;
		Double forwardThirdGear = 0.0;
		Double forwardFourGear = 0.0;

		Double backwardFirstGear = 0.0;
		Double backwardSecondGear = 0.0;
		Double backwardThirdGear = 0.0;
		Double backwardFourGear = 0.0;

		// Time Spent in Gears
		if (wlsGearUtilization != null && !wlsGearUtilization.isEmpty()) {
			forwardFirstGear = wlsGearUtilization.stream()
					.filter(o -> o.getForward() != null && o.getForward().get("gear1") != null)
					.mapToDouble(o -> o.getForward().get("gear1")).sum();
			forwardSecondGear = wlsGearUtilization.stream()
					.filter(o -> o.getForward() != null && o.getForward().get("gear2") != null)
					.mapToDouble(o -> o.getForward().get("gear2")).sum();
			forwardThirdGear = wlsGearUtilization.stream()
					.filter(o -> o.getForward() != null && o.getForward().get("gear3") != null)
					.mapToDouble(o -> o.getForward().get("gear3")).sum();
			forwardFourGear = wlsGearUtilization.stream()
					.filter(o -> o.getForward() != null && o.getForward().get("gear4") != null)
					.mapToDouble(o -> o.getForward().get("gear4")).sum();

			backwardFirstGear = wlsGearUtilization.stream()
					.filter(o -> o.getBackward() != null && o.getBackward().get("gear1") != null)
					.mapToDouble(o -> o.getBackward().get("gear1")).sum();
			backwardSecondGear = wlsGearUtilization.stream()
					.filter(o -> o.getBackward() != null && o.getBackward().get("gear2") != null)
					.mapToDouble(o -> o.getBackward().get("gear2")).sum();
			backwardThirdGear = wlsGearUtilization.stream()
					.filter(o -> o.getBackward() != null && o.getBackward().get("gear3") != null)
					.mapToDouble(o -> o.getBackward().get("gear3")).sum();
			backwardFourGear = wlsGearUtilization.stream()
					.filter(o -> o.getBackward() != null && o.getBackward().get("gear4") != null)
					.mapToDouble(o -> o.getBackward().get("gear4")).sum();

			CategoryDataset dataset = createGearUtilizationChartDataset(forwardFirstGear, forwardSecondGear,
					forwardThirdGear, forwardFourGear, backwardFirstGear, backwardSecondGear, backwardThirdGear,
					backwardFourGear);

			maxValules.add(backwardFourGear);
			maxValules.add(backwardThirdGear);
			maxValules.add(backwardSecondGear);
			maxValules.add(backwardFirstGear);
			maxValules.add(forwardFourGear);
			maxValules.add(forwardThirdGear);
			maxValules.add(forwardSecondGear);
			maxValules.add(forwardFirstGear);

			Double maxGear = Collections.max(maxValules);

			JFreeChart chart = ChartFactory.createBarChart("", // Chart Title
					"", // Category axis
					"", // Value axis
					dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);
			chart.getPlot().setBackgroundPaint(Color.white);
			chart.getPlot().setOutlinePaint(null);
			chart.getPlot().setOutlineStroke(null);
			chart.setBorderVisible(false);

			chart.getCategoryPlot().getRangeAxis().setLowerBound(10);

			CategoryPlot cplot = (CategoryPlot) chart.getPlot();

			((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

			renderer.setMaximumBarWidth(0.05);

			// set up gradient paints for series...
			GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, new Color(255, 192, 0), 0.0f, 0.0f,
					new Color(255, 192, 0));
			GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, new Color(0, 212, 172), 0.0f, 0.0f,
					new Color(0, 212, 172));

			renderer.setSeriesPaint(0, gp0);
			renderer.setSeriesPaint(1, gp1);
			renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setDefaultItemLabelsVisible(true);
			chart.removeLegend();

			final NumberAxis rangeAxis = (NumberAxis) cplot.getRangeAxis();
			Double fuelMaxValue = (maxGear != null && maxGear != null) ? maxGear : 10;
			log.info("maxGear " + fuelMaxValue);
			setRangeAndTickUnit(rangeAxis, fuelMaxValue);
			rangeAxis.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setTickLabelPaint(Color.BLACK);
			rangeAxis.setTickLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 11));
			rangeAxis.setLabelPaint(Color.BLACK);

			BufferedImage ChartBufferedImage = chart.createBufferedImage(600, 450);
			gearChartImage = Image.getInstance(writer, ChartBufferedImage, 1.0f);
		}
		PdfPTable enginePowerSummary = generateGearUtilizationSummary(gearChartImage, dec.format(forwardFirstGear),
				dec.format(forwardSecondGear), dec.format(forwardThirdGear), dec.format(forwardFourGear),
				dec.format(backwardFirstGear), dec.format(backwardSecondGear), dec.format(backwardThirdGear),
				dec.format(backwardFourGear), wlsGearUtilization);
		return enginePowerSummary;

	}
	
	private CategoryDataset createGearUtilizationChartDataset(Double forwardFirstGear, Double forwardSecondGear,
			Double forwardThirdGear, Double forwardFourGear, Double backwardFirstGear, Double backwardSecondGear,
			Double backwardThirdGear, Double backwardFourGear) {

		String series1 = "Forward";
		String series2 = "Backward";

		String category1 = "Gear 1";
		String category2 = "Gear 2";
		String category3 = "Gear 3";
		String category4 = "Gear 4";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(forwardFirstGear, series1, category1);
		dataset.addValue(forwardSecondGear, series1, category2);
		dataset.addValue(forwardThirdGear, series1, category3);
		dataset.addValue(forwardFourGear, series1, category4);

		dataset.addValue(backwardFirstGear, series2, category1);
		dataset.addValue(backwardSecondGear, series2, category2);
		dataset.addValue(backwardThirdGear, series2, category3);
		dataset.addValue(backwardFourGear, series2, category4);

		return dataset;
	}
	
	private PdfPTable generateGearUtilizationSummary(Image gearChartImage, String forwardFirstGear,
			String forwardSecondGear, String forwardThirdGear, String forwardFourGear, String backwardFirstGear,
			String backwardSecondGear, String backwardThirdGear, String backwardFourGear,
			List<WheelLoaderGearUtilization> wlsGearUtilization)
			throws DocumentException, MalformedURLException, IOException {

		PdfPTable dutyCycleSummary = new PdfPTable(1);
		dutyCycleSummary.setWidthPercentage(100);
		dutyCycleSummary.getDefaultCell().setBorder(0);
		dutyCycleSummary.setTableEvent(new BorderEvent());

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("GEAR UTILIZATION",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		dutyCycleSummary.addCell(header);

		PdfPCell content = new PdfPCell();
		content.setBorder(0);
		content.setPaddingLeft(20);
		// content.setFixedHeight(53f);

		Paragraph comments = new Paragraph();
		comments.setAlignment(Element.ALIGN_LEFT);
		comments.add(new Phrase("Time Spent in Gears is a combination of Gear 1,Gear 2,Gear 3 and Gear 4 in hrs.",
				new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0))));
		comments.setLeading(20f);
		content.addElement(comments);
		dutyCycleSummary.addCell(content);

		PdfPTable chartTable = new PdfPTable(1);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setWidths(new float[] { 100f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		gearChartImage.scaleAbsolute(400f, 250f);
		gearChartImage.setAlignment(Element.ALIGN_CENTER);
		imagecell.addElement(gearChartImage);

		dutyCycleSummary.addCell(imagecell);

		// Legend Details
		PdfPCell wayContent = new PdfPCell();
		wayContent.setPadding(10);
		wayContent.setBorder(0);

		// splitting table to 8 columns
		PdfPTable GearUtilizationContentTable = new PdfPTable(7);
		GearUtilizationContentTable.setWidths(new float[] { 10f, 14f, 10f, 10f, 10f, 10f, 10f });
		GearUtilizationContentTable.setWidthPercentage(100);

		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setBorder(0);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(
				new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(29, 36, 38)))));
		emptyCell.addElement(emptyText);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setBorder(Rectangle.BOTTOM);
		headerCell.setBorderWidthBottom(4f);
		headerCell.setBorderColorBottom(BaseColor.WHITE);
		headerCell.setFixedHeight(35f);
		headerCell.setBackgroundColor(new BaseColor(255, 192, 0));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_CENTER);
		headerText.add(
				new Phrase("Forward", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		GearUtilizationContentTable.addCell(emptyCell);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorBottom(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));

		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);

		valueText.add(new Phrase(forwardFirstGear + " Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorBottom(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		valueText.add(new Phrase(forwardSecondGear + " Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorBottom(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		valueText.add(new Phrase(forwardThirdGear + " Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorBottom(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		valueText.add(new Phrase(forwardFourGear + " Hrs",
				new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);
		GearUtilizationContentTable.addCell(emptyCell);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setBorder(Rectangle.TOP);
		headerCell.setBorderWidthTop(4f);
		headerCell.setBorderColorTop(BaseColor.WHITE);
		headerCell.setFixedHeight(35f);
		headerCell.setBackgroundColor(new BaseColor(0, 212, 172));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_CENTER);
		headerText.add(
				new Phrase("Backward", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);
		GearUtilizationContentTable.addCell(emptyCell);

		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorBottom(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);

		if (backwardFirstGear != null) {
			valueText.add(new Phrase(backwardFirstGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorBottom(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		if (backwardSecondGear != null) {
			valueText.add(new Phrase(backwardSecondGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorBottom(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		if (backwardThirdGear != null) {
			valueText.add(new Phrase(backwardThirdGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);

		headerCell = new PdfPCell();
		headerCell.setPaddingTop(7);
		headerCell.setBorderWidthLeft(1f);
		headerCell.setBorderWidthBottom(1f);
		headerCell.setBorderWidthTop(1f);
		headerCell.setBorderWidthRight(1f);
		headerCell.setBorderColorRight(new BaseColor(231, 231, 231));
		headerCell.setBorderColorLeft(new BaseColor(255, 255, 255));
		headerCell.setBorderColorBottom(new BaseColor(255, 255, 255));
		headerCell.setBorderColorTop(new BaseColor(255, 255, 255));
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_CENTER);
		if (backwardFourGear != null) {
			valueText.add(new Phrase(backwardFourGear + " Hrs",
					new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}
		headerCell.addElement(valueText);
		GearUtilizationContentTable.addCell(headerCell);
		GearUtilizationContentTable.addCell(emptyCell);

		dutyCycleSummary.addCell(GearUtilizationContentTable);

		return dutyCycleSummary;

	}
	
	private PdfPTable wlsPowerBandChart(List<MachinePowerBands> powerBandList, PdfWriter writer)
			throws IOException, DocumentException {

		Double idlePowerBand = 0.0, lowPowerBand = 0.0, mediumPowerBand = 0.0, highPowerBand = 0.0;
		DecimalFormat dec = new DecimalFormat("#0.00");
		for (int i = 0; i < powerBandList.size(); i++) {
			idlePowerBand = idlePowerBand
					+ (powerBandList.get(i).getIdlePowerBand() != null ? powerBandList.get(i).getIdlePowerBand() : 0);
			lowPowerBand = lowPowerBand
					+ (powerBandList.get(i).getLowPowerBand() != null ? powerBandList.get(i).getLowPowerBand() : 0);
			mediumPowerBand = mediumPowerBand
					+ (powerBandList.get(i).getMediumPowerBand() != null ? powerBandList.get(i).getMediumPowerBand()
							: 0);
			highPowerBand = highPowerBand
					+ (powerBandList.get(i).getHighPowerBand() != null ? powerBandList.get(i).getHighPowerBand() : 0);
		}
		
		DefaultPieDataset<String> dataSet = new DefaultPieDataset<String>();
		dataSet.setValue(dec.format(idlePowerBand), idlePowerBand);
		dataSet.setValue(dec.format(lowPowerBand), lowPowerBand);
		dataSet.setValue(dec.format(mediumPowerBand), mediumPowerBand);
		dataSet.setValue(dec.format(highPowerBand), highPowerBand);

		JFreeChart powerbandChart = ChartFactory.createPieChart("", dataSet, true, true, false);
		powerbandChart.setBackgroundPaint(Color.white);
		powerbandChart.getPlot().setBackgroundPaint(Color.white);
		powerbandChart.getPlot().setOutlinePaint(null);
		powerbandChart.getPlot().setOutlineStroke(null);
		powerbandChart.setBorderVisible(false);

		powerbandChart.removeLegend();
		PiePlot<?> plot = (PiePlot<?>) powerbandChart.getPlot();

		plot.setSimpleLabels(true);
		plot.setShadowPaint(null);
		plot.setLabelFont(new java.awt.Font("HELVETICA", java.awt.Font.PLAIN, 30));
		plot.setLabelLinkPaint(new Color(255, 0, 0));
		plot.setLabelShadowPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelBackgroundPaint(null);
		plot.setLabelPaint(Color.black);

		// Dynamic Color
		Color[] chartcolors = { new Color(236, 129, 65), new Color(0, 185, 240), new Color(253, 192, 45),
				new Color(165, 165, 165) };
		// set each sections inside paint
		int i = 0;
		for (Object key : dataSet.getKeys()) {
			plot.setSectionPaint((Comparable<?>) key, chartcolors[i % chartcolors.length]);
			i++;
		}

		BufferedImage powerbandImage = powerbandChart.createBufferedImage(600, 620);
		Image powerbandChartImage = Image.getInstance(writer, powerbandImage, 1.0f);
		PdfPTable powerBands = generatepowerBandsChart(powerbandChartImage, idlePowerBand, lowPowerBand,
				mediumPowerBand, highPowerBand);
		return powerBands;
	}

	private PdfPTable generatepowerBandsChart(Image powerbandChartImage, Double idlePowerBand, Double lowPowerBand,
			Double mediumPowerBand, Double highPowerBand) throws MalformedURLException, IOException, DocumentException {

		PdfPTable fuelDutySummary = new PdfPTable(1);
		fuelDutySummary.setWidthPercentage(100);
		fuelDutySummary.getDefaultCell().setBorder(0);
		fuelDutySummary.setTableEvent(new BorderEvent());

		DecimalFormat dec = new DecimalFormat("#0.00");

		PdfPCell header = new PdfPCell();
		header.setBorder(0);
		header.setPadding(20);
		header.setBackgroundColor(new BaseColor(29, 36, 38));
		Paragraph title = new Paragraph();
		title.setAlignment(Element.ALIGN_LEFT);
		title.add(new Phrase("POWER BANDS",
				new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, new BaseColor(255, 192, 0))));
		header.addElement(title);
		fuelDutySummary.addCell(header);

		PdfPTable chartTable = new PdfPTable(2);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		chartTable.setPaddingTop(50f);
		chartTable.setWidths(new float[] { 52f, 48f });

		PdfPCell imagecell = new PdfPCell();
		imagecell.setBorder(0);
		imagecell.setPaddingTop(60f);
		imagecell.addElement(powerbandChartImage);

		chartTable.addCell(imagecell);

		// Legend Details
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setColspan(2);
		emptyCell.setBorder(0);
		emptyCell.setFixedHeight(17.5f);
		Paragraph emptyText = new Paragraph();
		emptyText.add(new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(29, 36, 38))));
		emptyCell.addElement(emptyText);

		PdfPCell emptyValues = new PdfPCell();
		emptyValues.setBorder(0);
		emptyValues.addElement(new Phrase(" "));

		Image icon1 = Image.getInstance(IMAGELOACATION + "/icon7.png");
		icon1.setAlignment(Image.ALIGN_LEFT);
		icon1.setWidthPercentage(50);

		Image icon2 = Image.getInstance(IMAGELOACATION + "/icon5.png");
		icon2.setAlignment(Image.ALIGN_LEFT);
		icon2.setWidthPercentage(50);

		Image icon3 = Image.getInstance(IMAGELOACATION + "/icon1.png");
		icon3.setAlignment(Image.ALIGN_LEFT);
		icon3.setWidthPercentage(50);

		Image icon4 = Image.getInstance(IMAGELOACATION + "/icon6.png");
		icon4.setAlignment(Image.ALIGN_LEFT);
		icon4.setWidthPercentage(50);

		PdfPCell legentContent = new PdfPCell();
		legentContent.setPadding(5);
		legentContent.setBorder(0);

		PdfPCell headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		Paragraph headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Idle Power Band ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		PdfPCell rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon1);

		PdfPTable legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		PdfPCell valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		Paragraph valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (idlePowerBand != null) {
			valueText.add(new Phrase(dec.format(idlePowerBand) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);
		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Low Power Band ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon2);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (lowPowerBand != null) {
			valueText.add(new Phrase(dec.format(lowPowerBand) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);

		legentContent.addElement(legend);

		// 3rd
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| Medium Power Band ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon3);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (mediumPowerBand != null) {
			valueText.add(new Phrase(dec.format(mediumPowerBand) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		// Empty Space
		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);

		legend.addCell(emptyCell);
		legentContent.addElement(legend);

		// 4th
		headerCell = new PdfPCell();
		headerCell.setBorder(0);
		headerCell.setPadding(7);
		headerCell.setPaddingBottom(13f);
		headerCell.setBackgroundColor(new BaseColor(231, 231, 231));
		headerText = new Paragraph();
		headerText.setAlignment(Element.ALIGN_LEFT);
		headerText.add(new Phrase("| High Power Band ",
				new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(29, 36, 38))));
		headerCell.addElement(headerText);

		rightCell = new PdfPCell();
		rightCell.setPaddingTop(10);
		rightCell.setBorder(0);
		rightCell.addElement(icon4);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(rightCell);
		legend.addCell(headerCell);

		legentContent.addElement(legend);

		valueCell = new PdfPCell();
		valueCell.setBorder(0);
		valueCell.setPaddingLeft(20);
		valueText = new Paragraph();
		valueText.setAlignment(Element.ALIGN_LEFT);
		if (highPowerBand != null) {
			valueText.add(new Phrase(dec.format(highPowerBand) + " Ltrs",
					new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(29, 36, 38))));
		} else {
			valueText.add("");
		}

		valueCell.addElement(valueText);

		legend = new PdfPTable(2);
		legend.setWidthPercentage(100);
		legend.setWidths(new float[] { 15f, 85f });
		legend.addCell(emptyValues);
		legend.addCell(valueCell);

		legentContent.addElement(legend);

		chartTable.addCell(legentContent);

		fuelDutySummary.addCell(chartTable);

		return fuelDutySummary;

	}

}
