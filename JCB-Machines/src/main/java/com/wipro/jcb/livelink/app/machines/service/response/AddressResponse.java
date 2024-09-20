package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
* Author: Rituraj Azad
* User: RI20474447
* Date:17-09-2024
* project: JCB-Common-API-Customer
*/

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    @ApiModelProperty(value = "Chennai", required = true)
    private String location;

    @Override
    public String toString() {
        return "AddressResponse [location=" + location + "]";
    }

}
