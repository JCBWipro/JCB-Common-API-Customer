package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.dto.UtilizationPlatformWiseCustomerMachineCountId;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

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
@IdClass(UtilizationPlatformWiseCustomerMachineCountId.class)
@Table(name="utilization_platform_wise_customer_machine_count")
public class UtilizationPlatformWiseCustomerMachineCount {

	@Id
	@Column(name="legend")
	@Enumerated(EnumType.STRING)
	private MachineUtilizationCategory utilizationCategory;
	
	@Id
	@Column(name="customer_id")
	private String customerId;
	
	@Id
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "machine_count")
	private Long machineCount;
	
	@ManyToOne
	@JoinColumn(name = "cust_id", referencedColumnName = "id")
	@JsonIgnore
	private Customer customer;
	
	@JsonIgnore
	@Column(name = "cust_id", updatable = false, insertable = false)
	private String cust_id;

}
