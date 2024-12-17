package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LiveLocationData implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String link;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String vin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double latitude;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double longitude;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusAsOnTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expiryTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    public LiveLocationData(String link, String vin) {

        this.link = link;
        this.vin= vin;
    }

}
