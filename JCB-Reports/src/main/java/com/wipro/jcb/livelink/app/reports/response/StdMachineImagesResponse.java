package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This Class is to Handle Response related to StdMachineImagesResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StdMachineImagesResponse {

	private Integer id;

	private String imageName;

	private String imageUrl;

	private String imagePath;

}
