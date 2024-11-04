package com.wipro.jcb.livelink.app.machines.dealer.response;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionParams {
	
	@Schema(description = "Machine count on perticular given key", example = "30", required = true)
	private Long machineCount = 0l;
	@Schema(description = "category for separation", example = "Backhoe", required = true)
	private String category;
	@Schema(description = "Information about customer", example = "Backhoe", required = true)
	private List<CustomerInfo> customerInfo = new ArrayList<>();

}
