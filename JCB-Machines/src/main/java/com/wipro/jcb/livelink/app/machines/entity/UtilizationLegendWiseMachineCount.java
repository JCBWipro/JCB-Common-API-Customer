package com.wipro.jcb.livelink.app.machines.entity;

import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utilization_legend_wise_machine_count")
public class UtilizationLegendWiseMachineCount {

	@Id
	@Column(name = "legend")
	@Enumerated(EnumType.STRING)
	private MachineUtilizationCategory utilizationCategory;
	
	@Column(name = "machine_count")
	private Integer machineCount;

}
