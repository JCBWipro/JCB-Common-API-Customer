package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/17/2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class EngineFuelHistoryUtilizationDataV2 {


    private String vin;
    private String dateRange;
    private List<EngineHistoryDataListV3> engineHistoryDayDataList;
    private List<FuelHistoryDataListV3> fuelHistoryDayDataList;

    @Override
    public String toString() {
        return "EngineFuelHistoryUtilizationData [vin=" + vin + ", dateRange=" + dateRange
                + ", engineHistoryDayDataList=" + engineHistoryDayDataList + ", fuelHistoryDayDataList="
                + fuelHistoryDayDataList + "]";
    }


}
