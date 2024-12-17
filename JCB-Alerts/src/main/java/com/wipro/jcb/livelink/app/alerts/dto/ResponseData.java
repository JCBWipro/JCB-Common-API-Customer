package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * This ResponseData is to Handle Response related to status, message and Data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData implements Serializable {
	
	@Serial
	private static final long serialVersionUID = -8197944951108490793L;
	@Schema(description = "Representing the status of activity", example = "SUCCESS/FAILURE", required = true)
	private String status;
	@Schema(description = "Description of the activity processed", example = "Request successful")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;
	@Schema(description = "Contain if any info required in response", example = "Request successful")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String data;
	
	public ResponseData(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

}
