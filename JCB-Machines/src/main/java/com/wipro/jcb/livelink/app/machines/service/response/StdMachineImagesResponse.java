package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StdMachineImagesResponse {

    Integer id;

    String imageName;

    String imageUrl;

    String imagePath;

}
