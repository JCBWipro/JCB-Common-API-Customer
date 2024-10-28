package com.wipro.jcb.livelink.app.reports.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelHistoryDataId implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	private String vinId;
	private Date dateTime;

}
