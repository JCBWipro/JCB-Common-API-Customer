package com.wipro.jcb.livelink.app.machines.entity;

import com.wipro.jcb.livelink.app.machines.dto.UtilizationPlatformWiseMachineCountId;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UtilizationPlatformWiseMachineCountId.class)
@Table(name = "utilization_platform_wise_machine_count")
public class UtilizationPlatformWiseMachineCount {
	
	@Id
	@Column(name = "legend")
	@Enumerated(EnumType.STRING)
	private MachineUtilizationCategory utilizationCategory;
	
	@Id
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "machine_count")
	private Long machineCount;
	
}
