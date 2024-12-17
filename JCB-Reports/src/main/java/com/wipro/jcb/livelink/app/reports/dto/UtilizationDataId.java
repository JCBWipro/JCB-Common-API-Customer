package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/*
 * This Class is to Handle Response related to UtilizationDataId
 */
@Data
public class UtilizationDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1949937694808396055L;
    private Date day;
    private String vinId;
}