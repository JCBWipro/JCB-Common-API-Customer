package com.wipro.jcb.livelink.app.machines.request;

import com.wipro.jcb.livelink.app.machines.entity.Operator;

import io.swagger.annotations.ApiModelProperty;

public class MachineProfileRequest {
	
	@ApiModelProperty(value = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
	private String vin;
	@ApiModelProperty(value = "Operator details", required = true)
	private Operator operator;
	@ApiModelProperty(value = "Reg. No/Location/Nickname", example = "-", required = true)
	private String tag;
	@ApiModelProperty(value = "taluka place", example = "-", required = true)
	private String site;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Override
	public String toString() {
		return "MachineProfileRequest [vin=" + vin + ", operator=" + operator + ", tag=" + tag + "]";
	}

	public MachineProfileRequest(String vin, Operator operator, String tag, String site) {
		super();
		this.vin = vin;
		this.operator = operator;
		this.tag = tag;
		this.site = site;
	}

}
