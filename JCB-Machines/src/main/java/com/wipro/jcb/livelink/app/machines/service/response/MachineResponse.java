package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.dto.*;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.enums.NormalAlertSeverity;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class MachineResponse {
    @ApiModelProperty(value = "Unique identifier for machine", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @ApiModelProperty(value = "Status as on time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;
    @ApiModelProperty(value="Server Timezone", example = "UTC", required = true)
    private String serverTimezone = AppServerConstants.timezone;
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
    @ApiModelProperty(value = "List of machine utilization of provided machine", required = true)
    private List<MachineUtilizationData> utilization;
    @ApiModelProperty(value = "List of machine fuel consumption of provided machine", required = true)
    private List<MachineFuelConsumptionData> fuel;
    @ApiModelProperty(value = "List of machine performance of provided machine", required = true)
    private List<MachinePerformanceData> performance;
    @ApiModelProperty(value = "Machine service status", required = true)
    private String serviceStatus;
    @ApiModelProperty(value = "Machine service reason ", required = true)
    private String serviceReason;
    private FuelHistoryDataList fuelHistoryDataList;
    private EngineHistoryDataList engineHistoryDataList;

    private boolean supportFeatures;

    private boolean activeFlag;
	/*public MachineResponse(String vin, Date statusAsOnTime, String machineHours, String fuelPercentage, Double latitude,
			Double longitude, String location, String zone, String model, String tag, String dateRange, String hours,
			String image, Boolean normalAlert, Boolean serviceAlert, NormalAlertSeverity normalAlertSeverity,
			String fuelLevel, String platform, String thumbnail, MachineDetailData machineStatus, List<AlertData> alert,
			List<MachineUtilizationData> utilization, List<MachineFuelConsumptionData> fuel,
			List<MachinePerformanceData> performance, String serviceStatus, String serviceReason,
			FuelHistoryDataList fuelHistoryDataList, EngineHistoryDataList engineHistoryDataList) {
		super();
		this.vin = vin;
		this.statusAsOnTime = statusAsOnTime;
		this.machineHours = machineHours;
		this.fuelPercentage = fuelPercentage;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
		this.zone = zone;
		this.model = model;
		this.tag = tag;
		this.dateRange = dateRange;
		this.hours = hours;
		this.image = image;
		this.normalAlert = normalAlert;
		this.serviceAlert = serviceAlert;
		this.normalAlertSeverity = normalAlertSeverity;
		this.fuelLevel = fuelLevel;
		this.platform = platform;
		this.thumbnail = thumbnail;
		this.machineStatus = machineStatus;
		this.alert = alert;
		this.utilization = utilization;
		this.fuel = fuel;
		this.performance = performance;
		this.serviceStatus = serviceStatus;
		this.serviceReason = serviceReason;
		this.fuelHistoryDataList = fuelHistoryDataList;
		this.engineHistoryDataList = engineHistoryDataList;

	}*/



    public MachineResponse(Builder builder) {
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
        this.utilization = builder.utilization;
        this.fuel = builder.fuel;
        this.performance = builder.performance;
        this.serviceStatus = builder.serviceStatus;
        this.serviceReason = builder.serviceReason;
        this.fuelHistoryDataList = builder.fuelHistoryDataList;
        this.engineHistoryDataList = builder.engineHistoryDataList;
        this.supportFeatures=builder.supportFeatures;
        this.activeFlag = builder.activeFlag;
    }

    public MachineResponse() {
    }

    public String getVin() {
        return vin;
    }
    public Date getStatusAsOnTime() {
        return statusAsOnTime;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public String getMachineHours() {
        return machineHours;
    }


    public String getFuelPercentage() {
        return fuelPercentage;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public String getZone() {
        return zone;
    }

    public String getModel() {
        return model;
    }
    public String getTag() {
        return tag;
    }


    public String getDateRange() {
        return dateRange;
    }

    public String getHours() {
        return hours;
    }

    public String getImage() {
        return image;
    }

    public Boolean getNormalAlert() {
        return normalAlert;
    }

    public Boolean getServiceAlert() {
        return serviceAlert;
    }

    public NormalAlertSeverity getNormalAlertSeverity() {
        return normalAlertSeverity;
    }

    public String getFuelLevel() {
        return fuelLevel;
    }


    public String getPlatform() {
        return platform;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public MachineDetailData getMachineStatus() {
        return machineStatus;
    }

    public List<AlertData> getAlert() {
        return alert;
    }


    public List<MachineUtilizationData> getUtilization() {
        return utilization;
    }


    public List<MachineFuelConsumptionData> getFuel() {
        return fuel;
    }

    public List<MachinePerformanceData> getPerformance() {
        return performance;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public String getServiceReason() {
        return serviceReason;
    }

    public FuelHistoryDataList getFuelHistoryDataList() {
        return fuelHistoryDataList;
    }
    public EngineHistoryDataList getEngineHistoryDataList() {
        return engineHistoryDataList;
    }

    public boolean getSupportFeatures() {
        return supportFeatures;
    }

    public void setSupportedFeatures(final boolean supportFeatures) {
        this.supportFeatures = supportFeatures;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
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
        private List<MachineUtilizationData> utilization;
        private List<MachineFuelConsumptionData> fuel;
        private List<MachinePerformanceData> performance;
        private String serviceStatus;
        private String serviceReason;
        private FuelHistoryDataList fuelHistoryDataList;
        private EngineHistoryDataList engineHistoryDataList;
        private boolean supportFeatures;
        private boolean activeFlag;
        public Builder(final Machine machine, final MachineDetailResponse machinedetails, final ServiceStatus machineServiceStatus) {
            this.vin= machine.getVin();
            this.statusAsOnTime = machine.getStatusAsOnTime();
            this.machineHours = DOUBLE_FORMAT.format(machine.getTotalMachineHours()) + "Hrs";
            this.fuelPercentage = machine.getFuelLevel() + "%";
            this.latitude = machine.getLatitude();
            this.longitude = machine.getLongitude();
            this.location = machine.getLocation();
            this.zone = machine.getZone();
            this.model = machine.getModel();
            this.tag = machine.getTag();
            this.hours = "8 AM - 8 PM";
            this.image = machine.getImage();
            this.normalAlert = machinedetails.getNormalAlertCount() > 0;
            this.serviceAlert = machinedetails.getServiceAlertCount() > 0;
            this.normalAlertSeverity = machinedetails.getNormalAlertCount() > 0 ?
                    (machinedetails.getAlertWithRedEventLevel() > 0 ? NormalAlertSeverity.RED: NormalAlertSeverity.YELLOW):
                    NormalAlertSeverity.GREEN;

            this.platform = machine.getPlatform();
            this.thumbnail = machine.getThumbnail();
            this.machineStatus = machinedetails.getMachine();
            this.alert = machinedetails.getAlert();
            this.utilization = machinedetails.getUtilization();
            this.fuel = machinedetails.getFuel();
            this.performance = machinedetails.getPerformance();
            this.serviceStatus = machineServiceStatus.getServiceStatusName();
            this.serviceReason = machineServiceStatus.getServiceStatusReason();
            this.activeFlag = machine.isRenewalFlag();
        }

        public Builder setFeedData(MachineFeedParserData machineFeedparserdata, MachineFeedLocation machineFeedLocation) {
            this.location = (null!= machineFeedLocation.getMachineAddress().getLocation())?machineFeedLocation.getMachineAddress().getLocation() : this.location ;
            //this.location =  machineFeedLocation.getMachineAddress().getLocation();
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
        //this.dateRange = dateRange;
        public Builder dateRange(String dateRange) {
            this.dateRange= dateRange;
            return this;
        }


        //FuelHistoryDataList fuelHistoryDataList
        public Builder fuelHistoryDataList(final FuelHistoryDataList fuelHistoryDataList) {
            this.fuelHistoryDataList= fuelHistoryDataList;
            return this;
        }


        //EngineHistoryDataList engineHistoryDataList

        public Builder engineHistoryDataList(final EngineHistoryDataList engineHistoryDataList) {
            this.engineHistoryDataList= engineHistoryDataList;
            return this;
        }

        public Builder supportedFeatures(final boolean supportFeatures) {
            this.supportFeatures = supportFeatures;
            return this;
        }

        public MachineResponse build() {
            return new MachineResponse(this);
        }
    }

}

