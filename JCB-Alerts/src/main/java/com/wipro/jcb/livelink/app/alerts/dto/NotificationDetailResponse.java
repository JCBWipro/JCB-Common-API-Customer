package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetailResponse {


    private Integer id;

    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "notification type", example = "1.1", required = true)
    @JsonProperty("type")
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String vin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alertId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alertTitle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alertDesc;

    @Schema(description = "alertTime", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormatWithTime, timezone = AppServerConstants.timezone)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date alertTime;

    @Schema(description = "createdAt", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormatWithTime, timezone = AppServerConstants.timezone)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createdAt;

    @Schema(description = "updatedAt", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updatedAt;

    @Schema(description = "deletedAt", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date deletedAt;

    @Schema(description = "userId", example = "1.1", required = true)
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "flag", example = "1.1", required = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("flag")
    private Boolean flag;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String breakfastHeader;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String breakfastDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salesforceHeader;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salesforceDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationHeader;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationDescription;

    public NotificationDetailResponse(Integer id, Date day, String type, String userId,
                                      String vin, String alertId, String alertTitle, String alertDesc,
                                      Date alertTime, Date createdAt, Date updatedAt, Date deletedAt) {
        super();
        this.id = id;
        this.day = day;
        this.type = type;
        this.userId = userId;
        this.vin = vin;
        this.alertId = alertId;
        this.alertTitle = alertTitle;
        this.alertDesc = alertDesc;
        this.alertTime = alertTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public NotificationDetailResponse(Date day, String type) {
        super();
        this.day = day;
        this.type = type;
    }
}

