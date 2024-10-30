package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.dto.FuelHistoryDataId;

import io.swagger.v3.oas.annotations.media.Schema;
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
@DynamicUpdate
@IdClass(FuelHistoryDataId.class)
@Table(name = "machinefuelhistorydata", indexes = {
		@Index(name = "machinefuelhistorydata_vin_dateTime", columnList = "vin,dateTime", unique = false) })
public class MachineFuelHistory implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name = "vin", updatable = false, insertable = false)
	private String vin;

	private Double fuelLevel;
	@Id
	@JsonIgnore
	private String vinId;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Schema(description = "Generated Time", example = "2017-07-13 12:44:20", required = true)
	@JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
	private Date dateTime;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
	private Date createdAt;

}
