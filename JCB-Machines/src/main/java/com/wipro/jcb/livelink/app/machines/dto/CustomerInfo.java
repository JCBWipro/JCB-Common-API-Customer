package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * CustomerInfo is a data model class that represents detailed information about a customer.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    @ApiModelProperty(value = "Customer userName", example = "loginid", required = true)
    private String customerId;
    @ApiModelProperty(value = "URL for customer thumbnail", example = "Image/Path URL", required = true)
    private String thumbnail;
    @ApiModelProperty(value = "Customer name", example = "Siddhart Auto Engineers", required = true)
    private String customerName;
    @ApiModelProperty(value = "Customer mobile number", example = "9836098360", required = true)
    private String phoneNumber;
    @ApiModelProperty(value = "Customer address if - use country", example = "Wadgaon, Pune", required = true)
    private String address;
    @ApiModelProperty(value = "Country on which customer is located", example = "India", required = true)
    private String country;
    @ApiModelProperty(value = "Number of machines having service overdue alert in the specifed date range", example = "48", required = true)
    private Long machineCount;

}
