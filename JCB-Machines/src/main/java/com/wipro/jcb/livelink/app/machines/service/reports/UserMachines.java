package com.wipro.jcb.livelink.app.machines.service.reports;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @Schema(description = "Model", example = "3DX Super ecoXcellence", required = true)
    private String model;
    @Schema(description = "platform", example = "Backhoe Loader", required = true)
    private String platform;
    @Schema(description = "Reg. No/Location/Nickname", example = "-", required = true)
    private String tag;
    @Schema(description = "Machine thumbnail image", example = "Image URL/Path", required = true)
    private String thumbnail;
    @Schema(description = "mahcine location", example = "Pune", required = true)
    private String location;


}
