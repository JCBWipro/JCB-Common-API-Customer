package com.wipro.jcb.livelink.app.machines.dealer.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {
	
	@Schema(description = "Customer userName", example = "loginid", required = true)
	private String customerId;
	@Schema(description = "URL for customer thumbnail", example = "Image/Path URL", required = true)
	private String thumbnail;
	@Schema(description = "Customer name", example = "Siddhart Auto Engineers", required = true)
	private String customerName;
	@Schema(description = "Customer mobile number", example = "9836098360", required = true)
	private String phoneNumber;
	@Schema(description = "Customer address if - use country", example = "Wadgaon, Pune", required = true)
	private String address;
	@Schema(description = "Country on which customer is located", example = "India", required = true)
	private String country;
	@Schema(description = "Number of machines having service overdue alert in the specifed date range", example = "48", required = true)
	private Long machineCount;
	@Schema(description = "Customer userName", example = "name", required = true)
	private String displayName;

	public CustomerInfo(String customerName, String thumbnail, String customerId, Long machineCount, String phoneNumber,
			String address, String country) {
		super();
		this.thumbnail = thumbnail;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.country = country;
		this.machineCount = machineCount;
		this.customerId = customerId;
	}

	public CustomerInfo(String customerId, String thumbnail, String customerName, String phoneNumber, String address,
			String country, Long machineCount) {
		this.customerId = customerId;
		this.thumbnail = thumbnail;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.country = country;
		this.machineCount = machineCount;
	}

}
