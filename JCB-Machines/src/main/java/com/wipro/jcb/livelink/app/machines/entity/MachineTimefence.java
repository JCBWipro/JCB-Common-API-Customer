package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "machine_timefence")
public class MachineTimefence implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	private String mobileNumber;
	private String operatingStartTime;
	private String operatingEndTime ;
	private String notificationPattern ;
	private String notificationDate;
	private String recurrenceType;
	private String recurrence ;
	private String recurrenceStartDate;
	private String recurrenceEndDate;
	
	@Id
	@Column(name = "vin", unique = true, nullable = false)
	private String vin ;
	
	private String voicecall;
	private String sms;
	private String whatsapp;
	private String push;
	
}
