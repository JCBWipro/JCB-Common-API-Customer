package com.wipro.jcb.livelink.app.machines.request;

import com.wipro.jcb.livelink.app.machines.entity.Operator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This MachineProfileRequest is to Handle MachineProfile related Data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineProfileRequest {

	@Schema(description = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
	private String vin;
	@Schema(description = "Operator details", required = true)
	private Operator operator;
	@Schema(description = "Reg. No/Location/Nickname", example = "-", required = true)
	private String tag;
	@Schema(description = "taluka place", example = "-", required = true)
	private String site;

}