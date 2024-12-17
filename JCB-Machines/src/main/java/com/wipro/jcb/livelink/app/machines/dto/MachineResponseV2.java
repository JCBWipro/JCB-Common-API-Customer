package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.enums.NormalAlertSeverity;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class MachineResponseV2 {
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat(".##");
    @Schema(description = "Unique identifier for machine", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @Schema(description = "Status as on time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;
    @Schema(description ="Server Timezone", example = "UTC", required = true)
    private String serverTimezone = AppServerConstants.timezone;
    @Schema(description = "Total machine hours", example = "280Hrs", required = true)
    private String machineHours;
    @Schema(description = "Fuel percentage", example = "55%", required = true)
    private String fuelPercentage;
    @Schema(description = "Latitude", example = "18.5204", required = true)
    private Double latitude;
    @Schema(description = "Longitude", example = "73.8567", required = true)
    private Double longitude;
    @Schema(description = "Location", example = "Pune, Maharashtra, India", required = true)
    private String location;
    @Schema(description = "Zone", example = "North Regional Off", required = true)
    private String zone;
    @Schema(description = "Model", example = "3DX", required = true)
    private String model;
    @Schema(description = "Reg. No/Location/Nickname", example = "-", required = true)
    private String tag;
    @Schema(description = "Requested time range", example = "26 June 17 - 2 july 17", required = true)
    private String dateRange;
    @Schema(description = "Static value", example = "8 AM - 8 PM", required = true)
    private String hours;
    @JsonIgnore
    @Schema(description = "Machine image", example = "Image URL/Path", required = true)
    private String image;
    @Schema(description = "Flag representing normal alert status", example = "true", allowableValues = "true,false", required = true)
    private Boolean normalAlert;
    @Schema(description = "Flag representing service alert status", example = "true", allowableValues = "true,false", required = true)
    private Boolean serviceAlert;
    @Schema(description = "Flag representing service alert status", example = "RED", allowableValues = "RED,GREEN,YELLOW", required = true)
    private NormalAlertSeverity normalAlertSeverity;
    @Schema(description = "Fuel level color indicator", example = "high", allowableValues = "high,low,normal", required = true)
    private String fuelLevel;
    @Schema(description = "Type of platform", example = "Backhoe Loader", required = true)
    private String platform;
    @Schema(description = "Machine thumbnail image", example = "Image URL/Path", required = true)
    private String thumbnail;
    @Schema(description = "Machine details", required = true)
    private MachineDetailData machineStatus;
    @Schema(description = "List of alerts of provided machine", required = true)
    private List<AlertData> alert;
    @Schema(description = "Machine service status", required = true)
    private String serviceStatus;
    @Schema(description = "Machine service reason ", required = true)
    private String serviceReason;
    private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
    private List<EngineHistoryDataListV2> engineHistoryDayDataList;
    private List<MachineFuelConsumptionData> updatedFuelList;
    private List<MachineUtilizationData> updatedUtilizationList;
    private List<MachinePerformanceData> updatedPerformanceList;
    @Schema(description = "Requested time range", example = "08 May 20 - 14 May 20", required = true)
    private String utilizationDateRange;

    private boolean supportFeatures;

    private boolean activeFlag;

    public MachineResponseV2(Builder builder) {
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
        this.supportFeatures = builder.supportFeatures;
        this.updatedFuelList = builder.updatedFuelList;
        this.updatedPerformanceList = builder.updatedPerformanceList;
        this.updatedUtilizationList = builder.updatedUtilizationList;
        this.utilizationDateRange = builder.utilizationDateRange;
        this.activeFlag = builder.activeFlag;
    }

    public MachineResponseV2() {
    }

    // Getters and Setters

    public static class Builder{
        private final String vin;
        private Date statusAsOnTime;
        private String machineHours;
        private String fuelPercentage;
        private Double latitude;
        private Double longitude;
        private String location;
        private final String zone;
        private final String model;
        private final String tag;
        private String dateRange;
        private final String hours;
        private final String image;
        private final Boolean normalAlert;
        private final Boolean serviceAlert;
        private final NormalAlertSeverity normalAlertSeverity;
        private String fuelLevel;
        private final String platform;
        private final String thumbnail;
        private final MachineDetailData machineStatus;
        private final List<AlertData> alert;
        private final String serviceStatus;
        private final String serviceReason;
        private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
        private List<EngineHistoryDataListV2> engineHistoryDayDataList;
        private boolean supportFeatures;
        private final List<MachineFuelConsumptionData> updatedFuelList;
        private final List<MachineUtilizationData> updatedUtilizationList;
        private final List<MachinePerformanceData> updatedPerformanceList;
        private String utilizationDateRange;
        private final boolean activeFlag;

        public Builder(final Machine machine, final MachineDetailResponse machineDetails, final ServiceStatus machineServiceStatus) {
            this.vin = machine.getVin();
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
            this.normalAlert = machineDetails.getNormalAlertCount() > 0;
            this.serviceAlert = machineDetails.getServiceAlertCount() > 0;
            this.normalAlertSeverity = machineDetails.getNormalAlertCount() > 0 ?
                    (machineDetails.getAlertWithRedEventLevel() > 0 ? NormalAlertSeverity.RED : NormalAlertSeverity.YELLOW) :
                    NormalAlertSeverity.GREEN;
            this.platform = machine.getPlatform();
            this.thumbnail = machine.getThumbnail();
            this.machineStatus = machineDetails.getMachine();
            this.alert = machineDetails.getAlert();
            this.serviceStatus = machineServiceStatus.getServiceStatusName();
            this.serviceReason = machineServiceStatus.getServiceStatusReason();
            this.updatedFuelList = machineDetails.getUpdatedFuelList();
            this.updatedPerformanceList = machineDetails.getUpdatedPerformanceList();
            this.updatedUtilizationList = machineDetails.getUpdatedUtilizationList();
            this.activeFlag = machine.getRenewalFlag();
        }

        public Builder setFeedData(MachineFeedParserData machineFeedparserdata, MachineFeedLocation machineFeedLocation, String location) {
            this.location = location;
            this.latitude = machineFeedLocation.getLatitude();
            this.longitude = machineFeedLocation.getLongitude();
            this.statusAsOnTime = machineFeedparserdata.getStatusAsOnTime();
            this.fuelPercentage = (machineFeedparserdata.getFuelLevel() != null) ? machineFeedparserdata.getFuelLevel() + "%" : this.fuelPercentage;
            this.machineHours = DOUBLE_FORMAT.format(machineFeedparserdata.getTotalMachineHours()) + "Hrs";
            return this;
        }

        public Builder setFeedData(MachineFeedParserData machineFeedparserdata, MachineFeedLocation machineFeedLocation) {
            return setFeedData(machineFeedparserdata, machineFeedLocation, "-");
        }

        public Builder fuelLevel(String fuelLevel) {
            this.fuelLevel = fuelLevel;
            return this;
        }

        public Builder feedFuelLevel(MachineFeedParserData feed, Machine machine) {
            if (feed.getFuelLevel() != null) {
                this.fuelLevel = (feed.getFuelLevel() >= 0 && feed.getFuelLevel() < 6) ? "low" :
                        (feed.getFuelLevel() >= 6 && feed.getFuelLevel() <= 15) ? "normal" : "high";
            } else {
                this.fuelLevel = (machine.getFuelLevel() >= 0 && machine.getFuelLevel() < 6) ? "low" :
                        (machine.getFuelLevel() >= 6 && machine.getFuelLevel() <= 15) ? "normal" : "high";
            }
            return this;
        }

        public Builder dateRange(String dateRange) {
            this.dateRange = dateRange;
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

        public MachineResponseV2 build() {
            return new MachineResponseV2(this);
        }
    }
}