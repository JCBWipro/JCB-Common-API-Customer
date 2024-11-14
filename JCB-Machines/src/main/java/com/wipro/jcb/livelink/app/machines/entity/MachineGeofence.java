package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "machine_geofence")
public class MachineGeofence implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "vin", unique = true, nullable = false)
	private String vin ;
	
	private String mobileNumber;
	private String landmarkName;
	private Integer landmarkId;
	private Double radius ;
	private String address;
	private String latitude ;
	private String longitude ;
	private String isArrival;
	private String isDepature;
	private String sms;
	private String push;
	private String machineType;
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="timestamp default current_timestamp", insertable=false, updatable=false)
	private Date createdAt;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", updatable = false)
	private Date updatedAt;
	
}
