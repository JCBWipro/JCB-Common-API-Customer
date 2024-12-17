package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.dto.UtilizationLegendWiseCustomerMachineCountId;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UtilizationLegendWiseCustomerMachineCountId.class)
@Table(name = "utilization_legend_wise_customer_machine_count")
public class UtilizationLegendWiseCustomerMachineCount {

	@Id
	@Schema(description = "keyParam", example = "HEAVILY_USED", required = true)
	@JsonProperty("category")
	@Column(name = "legend")
	@Enumerated(EnumType.STRING)
	private MachineUtilizationCategory utilizationCategory;

	@Id
	@Column(name = "customer_id")
	private String customerId;

	@JsonProperty("machineCount")
	@Column(name = "machine_count")
	private Long machineCount;

	@ManyToOne
	@JoinColumn(name = "cust_id", referencedColumnName = "id")
	@JsonIgnore
	private Customer customer;

	@JsonIgnore
	@Column(name = "cust_id", insertable = false, updatable = false)
	private String custId;

}
