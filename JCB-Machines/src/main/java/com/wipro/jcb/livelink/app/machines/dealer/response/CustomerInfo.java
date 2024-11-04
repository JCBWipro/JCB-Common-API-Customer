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

}
