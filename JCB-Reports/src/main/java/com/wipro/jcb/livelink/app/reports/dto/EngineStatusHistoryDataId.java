package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineStatusHistoryDataId  implements Serializable {
    @Serial
    private static final long serialVersionUID = 3725304868871038840L;
    String vinId;
    Date dateTime;
}
