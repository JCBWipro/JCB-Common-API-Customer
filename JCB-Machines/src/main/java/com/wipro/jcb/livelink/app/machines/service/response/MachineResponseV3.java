package com.wipro.jcb.livelink.app.machines.service.response;

import com.wipro.jcb.livelink.app.machines.dto.*;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineFuelConsumptionData;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-09-2024
 * project: JCB-Common-API-Customer
 */
@Getter
@Data
public class MachineResponseV3 {
    @ApiModelProperty(value = "Unique identifier for machine", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    private Boolean pdfView;
    private String machineType;
    private String type;
    private String premiumFlag;
    private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
    private List<EngineHistoryDataListV2> engineHistoryDayDataList;
    private List<MachineFuelConsumptionData> updatedFuelList;
    private List<MachineUtilizationData> updatedUtilizationList;
    private List<MachinePerformanceData> updatedPerformanceList;

    public MachineResponseV3(Builder builder) {
        super();
        this.vin = builder.vin;
        this.pdfView = builder.pdfView;
        this.machineType = builder.machineType;
        this.type = builder.type;
        this.premiumFlag = builder.premiumFlag;
        this.fuelHistoryDayDataList = builder.fuelHistoryDayDataList;
        this.engineHistoryDayDataList = builder.engineHistoryDayDataList;
        this.updatedFuelList=builder.updatedFuelList;
        this.updatedPerformanceList=builder.updatedPerformanceList;
        this.updatedUtilizationList=builder.updatedUtilizationList;
    }

    public MachineResponseV3() {
    }


    public static class Builder{
        private final String vin;
        private final Boolean pdfView;
        private final String machineType;
        private final String type;
        private final String premiumFlag;
        private List<FuelHistoryDataListV2> fuelHistoryDayDataList;
        private List<EngineHistoryDataListV2> engineHistoryDayDataList;
        private final List<MachineFuelConsumptionData> updatedFuelList;
        private final List<MachineUtilizationData> updatedUtilizationList;
        private final List<MachinePerformanceData> updatedPerformanceList;
        public Builder(final Machine machine, final MachineDetailResponse machinedetails, String machineType) {
            this.vin= machine.getVin();
            //this.pdfView = machine.getPlatform().equals("Excavators") ? true : false;
            this.pdfView = true ;
            this.machineType = machineType;
            this.type = (machine.getMachineType()!=null && !Objects.equals(machine.getMachineType(), "") && !machine.getMachineType().isEmpty()) ? machine.getMachineType() : "Standard";
            this.premiumFlag = (machine.getPremiumFlag()!=null && !Objects.equals(machine.getPremiumFlag(), "") && !machine.getPremiumFlag().isEmpty()) ? machine.getPremiumFlag() : "Standard";
            this.updatedFuelList=machinedetails.getUpdatedFuelList();
            this.updatedPerformanceList=machinedetails.getUpdatedPerformanceList();
            this.updatedUtilizationList=machinedetails.getUpdatedUtilizationList();
        }

        public Builder engineHistoryDayDataList(final List<EngineHistoryDataListV2> engineHistoryDayDataList) {
            this.engineHistoryDayDataList = engineHistoryDayDataList;
            return this;
        }

        public Builder fuelHistoryDayDataList(final List<FuelHistoryDataListV2> fuelHistoryDayDataList) {
            this.fuelHistoryDayDataList = fuelHistoryDayDataList;
            return this;
        }

        public MachineResponseV3 build() {
            return new MachineResponseV3(this);
        }
    }

}
