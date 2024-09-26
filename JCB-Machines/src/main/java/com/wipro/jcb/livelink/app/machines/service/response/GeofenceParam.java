package com.wipro.jcb.livelink.app.machines.service.response;

public class GeofenceParam {
	private Double centerLatitude = 20.0111;
	private Double centerLongitude = 76.554;
	private Long radis = 12L;

	public GeofenceParam() {
		super();
	}

	public GeofenceParam(Double centerLatitude, Double centerLongitude, Long radis) {
		super();
		this.centerLatitude = centerLatitude;
		this.centerLongitude = centerLongitude;
		this.radis = radis;
	}

	public Double getCenterLatitude() {
		return centerLatitude;
	}

	public void setCenterLatitude(Double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	public Double getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(Double centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public Long getRadis() {
		return radis;
	}

	public void setRadis(Long radis) {
		this.radis = radis;
	}

	@Override
	public String toString() {
		return "GeofenceParam [centerLatitude=" + centerLatitude + ", centerLongitude=" + centerLongitude + ", radis="
				+ radis + "]";
	}

}

