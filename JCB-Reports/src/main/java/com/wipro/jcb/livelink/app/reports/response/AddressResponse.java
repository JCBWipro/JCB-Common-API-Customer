package com.wipro.jcb.livelink.app.reports.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    @Schema(description = "Location", example = "Chennai", required = true)
    private String location;

}
