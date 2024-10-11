package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.dto.MachineFeautureInfoId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MachineFeautureInfoId.class)
@Table(name = "machine_feature_info", indexes = {
		@Index(name = "machine_feature_info_vin_type", columnList = "vin,type", unique = true) })

public class MachineFeatureInfo implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String vin;

	@Id
	private String type;

	private boolean flag = true;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
	private Date createdAt;

}
