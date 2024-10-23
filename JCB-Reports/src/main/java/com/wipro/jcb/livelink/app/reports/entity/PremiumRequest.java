package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "premium_request")
public class PremiumRequest implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	
	@Schema(description = "vin", example = "3XHARD232993", required = true)
	@JsonProperty("vin")
	private String vin;
	
	@Column(name = "count")
	private long count= 0;
	
	@Schema(description = "userId", example = "test5455678", required = true)
	@JsonProperty("user_id")
	private String userId;
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at",columnDefinition = "timestamp default current_timestamp", insertable = false)
	private Date createdAt;

}
