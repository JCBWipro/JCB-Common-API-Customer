package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to MachineTelehandlerDataId
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineTelehandlerDataId implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private Date day;
	private String vinId;

}