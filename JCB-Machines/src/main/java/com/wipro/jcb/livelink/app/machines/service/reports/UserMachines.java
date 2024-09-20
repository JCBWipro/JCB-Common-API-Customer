package com.wipro.jcb.livelink.app.machines.service.reports;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMachines {
    @ApiModelProperty(value = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @ApiModelProperty(value = "Model", example = "3DX Super ecoXcellence", required = true)
    private String model;
    @ApiModelProperty(value = "platform", example = "Backhoe Loader", required = true)
    private String platform;
    @ApiModelProperty(value = "Reg. No/Location/Nickname", example = "-", required = true)
    private String tag;
    @ApiModelProperty(value = "Machine thumbnail image", example = "Image URL/Path", required = true)
    private String thumbnail;
    @ApiModelProperty(value = "mahcine location", example = "Pune", required = true)
    private String location;


}
