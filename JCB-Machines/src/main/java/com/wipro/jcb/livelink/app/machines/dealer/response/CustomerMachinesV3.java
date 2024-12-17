package com.wipro.jcb.livelink.app.machines.dealer.response;

import java.util.List;

import com.wipro.jcb.livelink.app.machines.service.response.MachineResponseListV3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMachinesV3 {

	private CustomerInfo customerInfo;
	private List<MachineResponseListV3> machines;

}
