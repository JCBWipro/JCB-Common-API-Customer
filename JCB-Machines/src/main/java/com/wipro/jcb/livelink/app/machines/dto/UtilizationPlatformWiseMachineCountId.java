package com.wipro.jcb.livelink.app.machines.dto;

import java.io.Serial;
import java.io.Serializable;

import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilizationPlatformWiseMachineCountId implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private MachineUtilizationCategory utilizationCategory;
	private String platform;

}
