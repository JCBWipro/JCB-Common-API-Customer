package com.wipro.jcb.livelink.app.reports.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This Class is to Handle Response related to StandardMachineBaseResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardMachineBaseResponse {

	List<StdMachineImagesResponse> data;

}
