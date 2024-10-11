package com.wipro.jcb.livelink.app.machines.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to MachineFeautureInfoId
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineFeautureInfoId implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String vin;
	private String type;

}
