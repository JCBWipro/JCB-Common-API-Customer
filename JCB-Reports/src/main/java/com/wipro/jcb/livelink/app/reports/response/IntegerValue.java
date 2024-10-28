package com.wipro.jcb.livelink.app.reports.response;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegerValue implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	Integer val ;

}
