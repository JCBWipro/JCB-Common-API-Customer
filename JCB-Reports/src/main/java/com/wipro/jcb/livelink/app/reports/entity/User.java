package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.enums.UserType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@XmlRootElement
@Table(name = "LiveLinkUser", indexes = { @Index(name = "LiveLinkUser_email", columnList = "email"),
		@Index(name = "LiveLinkUser_userId", columnList = "USER_ID") })
@DynamicUpdate
public class User implements Serializable {
	@Serial
	private static final long serialVersionUID = 7647358402835262772L;
	@Id
	@Schema(description = "unique identifier for user", example = "Lorey_Holland", required = true)
	@Column(name = "USER_ID", unique = true, columnDefinition = "VARCHAR(64)")
	private String userName;
	@Column(name = "email")
	@Schema(description = "Email Id of user", example = "lorey@mymail.com", required = true)
	private String email;
	@Schema(description = "Role/Type of user", example = "Customer", required = true)
	@Enumerated(EnumType.STRING)
	private UserType userType;
	@Schema(description = "FirstName of user", example = "Lorey", required = true)
	private String firstName;
	@Schema(description = "Users lastname", example = "Hollandr", required = true)
	private String lastName;
	@Schema(description = "Contact of user", example = "1234567890", required = true)
	private String phoneNumber;
	@Schema(description = "Address of User", example = "Pune", required = true)
	private String address;
	@Schema(description = "User understandable language", example = "English", required = true)
	private String smsLanguage;
	@Schema(description = "Time Zone is a region of the globe that observes a uniform standard time", example = "UTC+05:30", required = true)
	private String timeZone;
	@Schema(description = "User Photo", example = "user.png", required = true)
	@JsonIgnore
	private String image;
	@Schema(description = "User thumbnail Photo", example = "user.png", required = true)
	private String thumbnail;
	@Schema(description = "Country in which user resides", example = "India", required = true)
	private String country;
	private String password;
	@Schema(description = "true if it is sys gen password", example = "true", required = true)
	private Boolean sysGenPass;
	@JsonIgnore
	private String activityCompleted;
	@JsonIgnore
	private String serviceHistoryStatus;
	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<String> livelinkPersonName;
	@JsonIgnore
	private String roleName;
	@Schema(description = "true if Secret Question is set ", example = "true", required = true)
	private Boolean isSecretQuestion;

	@JsonIgnore
	private Boolean machineUpdateNotificationEnabled = false;
	@JsonIgnore
	private String userAppVersion;

	@JsonIgnore
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "analytic_last_visited_time")
	private Date analyticLastVistedTime;

	@Schema(description = "Day", example = "2017-07-13", required = false)
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@Column(name = "feed_date")
	private Date feedDate;

	@Column(name = "language")
	@Schema(description = "User Language", example = "English", required = true)
	private String language;

}
