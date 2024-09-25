package com.wipro.jcb.livelink.app.user.web.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "asset_control_unit")
public class AssetControlUnitEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Serial_Number")
	private String serialNumber;
	
	@OneToOne(targetEntity = ControlUnitEntity.class)
    @JoinColumn(name = "Control_Unit_ID")
	private ControlUnitEntity controlUnitId;
	
	@Column(name = "SIM_No")
	private String simNo;
	
	@Column(name = "ICCID")
	private String iccidNo;
	
	@Column(name = "IMEI_Number")
	private String imeiNo;
	
	@Column(name = "Registration_Date")
	private Timestamp registrationDate;

}
