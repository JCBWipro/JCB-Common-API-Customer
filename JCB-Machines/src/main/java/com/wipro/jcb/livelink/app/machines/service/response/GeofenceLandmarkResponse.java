package com.wipro.jcb.livelink.app.machines.service.response;

import java.util.List;

import com.wipro.jcb.livelink.app.machines.request.GeofenceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceLandmarkResponse {
	
	private String landmark;
	private List<GeofenceRequest> landmarkDetails;

}
