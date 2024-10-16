package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/*
 * This Class is to Handle Response related to MachinePerformanceDataId
 */
@Data
public class MachinePerformanceDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8564378808953107809L;
    private Date day;
    private String vinId;
}
