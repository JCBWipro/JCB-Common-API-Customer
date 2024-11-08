package com.wipro.jcb.livelink.app.machines.service.response;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.dto.AlertData;
import com.wipro.jcb.livelink.app.machines.dto.EngineHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.FuelHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.MachineDetailData;
import com.wipro.jcb.livelink.app.machines.dto.MachineDetailResponse;
import com.wipro.jcb.livelink.app.machines.dto.MachinePerformanceData;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedLocation;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.machines.entity.MachineFuelConsumptionData;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import com.wipro.jcb.livelink.app.machines.enums.NormalAlertSeverity;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MachineResponseListV3 {

	@ApiModelProperty(value = "Unique identifier for machine", example = "PUNJD22CV0000IIII", required = true)
	private String vin;
	@ApiModelProperty(value = "Status as on time", example = "2017-07-13 12:44:20", required = true)
	@JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
	private Date statusAsOnTime;
	@ApiModelProperty(value="Server Timezone", example = "UTC", required = true)
	private final String serverTimezone = AppServerConstants.timezone;
	@ApiModelProperty(value = "Total machine hours", example = "280Hrs", required = true)
	private String machineHours;
	@ApiModelProperty(value = "Fuel percentage", example = "55%", required = true)
	private String fuelPercentage;
	@ApiModelProperty(value = "Latitude", example = "18.5204", required = true)
	private Double latitude;
	@ApiModelProperty(value = "Longitude", example = "73.8567", required = true)
	private Double longitude;
	@ApiModelProperty(value = "Location", example = "Pune, Maharashtra, India", required = true)
	private String location;
	@ApiModelProperty(value = "Zone", example = "North Regional Off", required = true)
	private String zone;
	@ApiModelProperty(value = "Model", example = "3DX", required = true)
	private String model;
	@ApiModelProperty(value = "Reg. No/Location/Nickname", example = "-", required = true)
	private String tag;
	@ApiModelProperty(value = "Requested time range", example = "26 June 17 - 2 july 17", required = true)
	private String dateRange;
	@ApiModelProperty(value = "Staic value", example = "8 AM - 8 PM", required = true)
	private String hours;
	@JsonIgnore
	@ApiModelProperty(value = "Machine image", example = "Image URL/Path", required = true)
	private String image;
	@ApiModelProperty(value = "Flag representing normal alert status", example = "true", allowableValues = "true,false", required = true)
	private Boolean normalAlert;
	@ApiModelProperty(value = "Flag representing service alert status", example = "true", allowableValues = "true,false", required = true)
	private Boolean serviceAlert;
	@ApiModelProperty(value = "Flag representing service alert status", example = "RED", allowableValues = "RED,GREEN,YELLOW", required = true)
	private NormalAlertSeverity normalAlertSeverity;
	@ApiModelProperty(value = "Fuel level color indicator", example = "high", allowableValues = "high,low,normal", required = true)
	private String fuelLevel;
	@ApiModelProperty(value = "Type of platform", example = "Backhoe Loader", required = true)
	private String platform;
	@ApiModelProperty(value = "Machine thumbnail image", example = "Image URL/Path", required = true)
	private String thumbnail;
	@ApiModelProperty(value = "Machine details", required = true)
	private MachineDetailData machineStatus;
	@ApiModelProperty(value = "List of alerts of provided machine", required = true)
	private List<AlertData> alert;
	@ApiModelProperty(value = "Machine service status", required = true)
	private String serviceStatus;
	@ApiModelProperty(value = "Machine service reason ", required = true)
	private String serviceReason;
	private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
	private List<EngineHistoryDataListV2> engineHistoryDayDataList;
	private List<MachineFuelConsumptionData> updatedFuelList;
	private List<MachineUtilizationData> updatedUtilizationList;
	private List<MachinePerformanceData> updatedPerformanceList;
	@ApiModelProperty(value = "Requested time range", example = "08 May 20 - 14 May 20", required = true)
	private String utilizationDateRange;
	
	private boolean supportFeatures;
	
	private boolean activeFlag;
	
	private String premiumFlag;
	
	public MachineResponseListV3(Builder builder) {
		super();
		this.vin = builder.vin;
		this.statusAsOnTime = builder.statusAsOnTime;
		this.machineHours = builder.machineHours;
		if((!FuelLevelNAConstant.getExceptionMachines().contains(this.vin)) && FuelLevelNAConstant.getFuellevelnaconfig().containsKey(builder.platform)
				&& FuelLevelNAConstant.getFuellevelnaconfig().get(builder.platform).contains(this.vin.substring(3, 8))) {
			this.fuelPercentage = "NA";
			this.fuelLevel = "normal";
		} else {
			this.fuelPercentage = builder.fuelPercentage;
			this.fuelLevel = builder.fuelLevel;
		}
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
		this.location = builder.location;
		this.zone = builder.zone;
		this.model = builder.model;
		this.tag = builder.tag;
		this.dateRange = builder.dateRange;
		this.hours = builder.hours;
		this.image = builder.image;
		this.normalAlert = builder.normalAlert;
		this.serviceAlert = builder.serviceAlert;
		this.normalAlertSeverity = builder.normalAlertSeverity;
		this.platform = builder.platform;
		this.thumbnail = builder.thumbnail;
		this.machineStatus = builder.machineStatus;
		this.alert = builder.alert;
		this.serviceStatus = builder.serviceStatus;
		this.serviceReason = builder.serviceReason;
		this.fuelHistoryDayDataList = builder.fuelHistoryDayDataList;
		this.engineHistoryDayDataList = builder.engineHistoryDayDataList;
		this.supportFeatures=builder.supportFeatures;
		this.updatedFuelList=builder.updatedFuelList;
		this.updatedPerformanceList=builder.updatedPerformanceList;
		this.updatedUtilizationList=builder.updatedUtilizationList;
		this.utilizationDateRange=builder.utilizationDateRange;
		this.activeFlag = builder.activeFlag;
		this.premiumFlag = builder.premiumFlag;
	}
	
	public static class Builder{
		private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat(".##");
		private String vin;
		private Date statusAsOnTime;
		private String machineHours;
		private String fuelPercentage;
		private Double latitude;
		private Double longitude;
		private String location;
		private String zone;
		private String model;
		private String tag;
		private String dateRange;
		private String hours;
		private String image;
		private Boolean normalAlert;
		private Boolean serviceAlert;
		private NormalAlertSeverity normalAlertSeverity;
		private String fuelLevel;
		private String platform;
		private String thumbnail;
		private MachineDetailData machineStatus;
		private List<AlertData> alert;
		private String serviceStatus;
		private String serviceReason;
		private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
		private List<EngineHistoryDataListV2> engineHistoryDayDataList;
		private boolean supportFeatures;
		private List<MachineFuelConsumptionData> updatedFuelList;
		private List<MachineUtilizationData> updatedUtilizationList;
		private List<MachinePerformanceData> updatedPerformanceList;
		private String utilizationDateRange;
		private boolean activeFlag;
		private String premiumFlag;
		
		public Builder(final Machine machine,final MachineDetailResponse machinedetails,final ServiceStatus machineServiceStatus) {
			this.vin= machine.getVin();			
			this.statusAsOnTime = machine.getStatusAsOnTime();
			this.machineHours = DOUBLE_FORMAT.format(machine.getTotalMachineHours()) + "Hrs";
			this.fuelPercentage = machine.getFuelLevel() + "%";
			this.latitude = machine.getLatitude();
			this.longitude = machine.getLongitude();
			this.location = "-";
			this.zone = machine.getZone();
			this.model = machine.getModel();
			this.tag = "";
			this.hours = "8 AM - 8 PM";
			this.image = machine.getImage();
			this.normalAlert = machinedetails.getNormalAlertCount() > 0? true: false;
			this.serviceAlert = machinedetails.getServiceAlertCount() > 0? true: false;
			this.normalAlertSeverity = machinedetails.getNormalAlertCount() > 0 ? 
					(machinedetails.getAlertWithRedEventLevel() > 0 ? NormalAlertSeverity.RED: NormalAlertSeverity.YELLOW):
						NormalAlertSeverity.GREEN;
			
			this.platform = machine.getPlatform();
			this.thumbnail = machine.getThumbnail();
			this.machineStatus = machinedetails.getMachine();
			this.alert = machinedetails.getAlert();
			this.serviceStatus = machineServiceStatus.getServiceStatusName();
			this.serviceReason = machineServiceStatus.getServiceStatusReason();
			this.updatedFuelList=machinedetails.getUpdatedFuelList();;
			this.updatedPerformanceList=machinedetails.getUpdatedPerformanceList();
			this.updatedUtilizationList=machinedetails.getUpdatedUtilizationList();
			this.activeFlag = machine.getRenewalFlag();
			this.premiumFlag = (machine.getPremiumFlag()!=null &&  machine.getPremiumFlag()!="" && !machine.getPremiumFlag().isEmpty()) ? machine.getPremiumFlag() : "Standard";
		}

		public Builder setFeedData(MachineFeedParserData machineFeedparserdata,MachineFeedLocation machineFeedLocation, String location) {
			this.location = location ;
			this.latitude = machineFeedLocation.getLatitude();
			this.longitude = machineFeedLocation.getLongitude();
			this.statusAsOnTime = machineFeedparserdata.getStatusAsOnTime();
			this.fuelPercentage = null!= machineFeedparserdata.getFuelLevel()?machineFeedparserdata.getFuelLevel()+ "%": this.fuelPercentage;
			this.machineHours = DOUBLE_FORMAT.format(machineFeedparserdata.getTotalMachineHours()) + "Hrs";
		return this;	
		}
		
		public Builder setFeedData(MachineFeedParserData machineFeedparserdata,MachineFeedLocation machineFeedLocation) {
			this.location = "-";
			this.latitude = machineFeedLocation.getLatitude();
			this.longitude = machineFeedLocation.getLongitude();
			this.statusAsOnTime = machineFeedparserdata.getStatusAsOnTime();
			this.fuelPercentage = null!= machineFeedparserdata.getFuelLevel()?machineFeedparserdata.getFuelLevel()+ "%": this.fuelPercentage;
			this.machineHours = DOUBLE_FORMAT.format(machineFeedparserdata.getTotalMachineHours()) + "Hrs";
		return this;	
		}
		
		public Builder fuelLevel (String fuelLevel) {
			this.fuelLevel = fuelLevel;
			return this;
		}
		
		public Builder feedFuelLevel(MachineFeedParserData feed , Machine machine) {
			if (null!= feed.getFuelLevel()) {
				this.fuelLevel = feed.getFuelLevel() >= 0 && feed.getFuelLevel() < 6 ? "low": feed.getFuelLevel() >= 6 && feed.getFuelLevel() <= 15 ? "normal": "high";
				}
			else {
				this.fuelLevel = machine.getFuelLevel() >= 0 && machine.getFuelLevel() < 6 ? "low": machine.getFuelLevel() >= 6 && machine.getFuelLevel() <= 15 ? "normal": "high";
			}
			return this;
		}
		
		public Builder dateRange(String dateRange) {
			this.dateRange= dateRange;
			return this;
		}
		
		public Builder engineHistoryDayDataList(final List<EngineHistoryDataListV2> engineHistoryDayDataList) {
			this.engineHistoryDayDataList = engineHistoryDayDataList;
			return this;
		}
		
		public Builder fuelHistoryDayDataList(final List<FuelHistoryDataListV2> fuelHistoryDayDataList) {
			this.fuelHistoryDayDataList = fuelHistoryDayDataList;
			return this;
		}
		
		public Builder supportedFeatures(final boolean supportFeatures) {
			this.supportFeatures = supportFeatures;
			return this;
		}
		
		public Builder utilizationDateRange(String utilizationDateRange) {
			this.utilizationDateRange = utilizationDateRange;
			return this;
		}
		
		public MachineResponseListV3 build() {
			return new MachineResponseListV3(this);
		}
	}
	
}
