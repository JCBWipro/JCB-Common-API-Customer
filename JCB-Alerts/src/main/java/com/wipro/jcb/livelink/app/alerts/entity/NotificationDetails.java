package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */

@Setter
@Getter
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_details")
public class NotificationDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1293838L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Schema(description = "Day", example = "2017-07-13")
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "day")
    private Date day;
    @Schema(description = "type", example = "Breakfast,Aler,SSL")
    @JsonProperty("type")
    private String type;
    @Schema(description = "userId", example = "1.1")
    @JsonProperty("user_id")
    private String userId;
    @Schema(description = "flag", example = "1.1")
    @JsonProperty("flag")
    private Boolean flag;
    @Schema(description = "vin", example = "3XHARD232993")
    @JsonProperty("vin")
    private String vin;
    @Schema(description = "alertId", example = "3XHARD232993")
    @JsonProperty("alert_id")
    private String alertId;
    @Schema(description = "vin", example = "3XHARD232993")
    @JsonProperty("alert_title")
    private String alertTitle;
    @Schema(description = "vin", example = "3XHARD232993")
    @JsonProperty("alert_description")
    private String alertDesc;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alert_date_time")
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date alertTime;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date updatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date deletedAt;
}