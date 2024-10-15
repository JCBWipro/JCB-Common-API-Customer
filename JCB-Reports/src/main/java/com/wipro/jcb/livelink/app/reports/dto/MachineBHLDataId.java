package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * This Class is to Handle Response related to MachineBHLDataId
 */
public class MachineBHLDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6037626787004212021L;
    Date day;
    String vinId;
}
