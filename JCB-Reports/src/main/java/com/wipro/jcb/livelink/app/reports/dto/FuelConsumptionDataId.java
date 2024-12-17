package com.wipro.jcb.livelink.app.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/*
 * This Class is to Handle Response related to FuelConsumptionData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelConsumptionDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3844277147297566079L;
    private Date day;
    private String vinId;
}
