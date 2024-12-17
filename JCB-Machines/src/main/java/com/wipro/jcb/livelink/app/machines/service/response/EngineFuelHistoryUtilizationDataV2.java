package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.*;

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
@ToString
public class EngineFuelHistoryUtilizationDataV2 {
    private String vin;
    private String dateRange;
    private List<EngineHistoryDataListV3> engineHistoryDayDataList;
    private List<FuelHistoryDataListV3> fuelHistoryDayDataList;
}
