package com.wipro.jcb.livelink.app.machines.dealer.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDistribution {
	
	@Schema(description = "Machine count on perticular given key", example = "30", required = true)
	private Long machineCount;
	@Schema(description = "Contains customer info depend on machine category", example = "", required = true)
	private List<DistributionParams> distributinParams;

}
