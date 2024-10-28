package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDutyCycle {

    private Double attachment;
    private Double idling;
    private Double excavation;
    private Double loading;
    private Double roading;

}
