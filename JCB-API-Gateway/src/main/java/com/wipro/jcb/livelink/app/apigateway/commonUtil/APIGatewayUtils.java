package com.wipro.jcb.livelink.app.apigateway.commonUtil;

import java.util.HashMap;
import java.util.Map;

public class APIGatewayUtils {

	public static Integer getRolesByID(String roleName) {
		Map<String, Integer> map = new HashMap<>();
		map.put("JCB Account",1);
		map.put("JCB HO",2);
		map.put("JCB RO",3);
		map.put("Customer Care",4);
		map.put("Dealer Admin",5);
		map.put("Dealer",6);
		map.put("Customer Fleet Manager",7);
		map.put("Customer",8);
		map.put("wipro personal",9);
		map.put("Pricol",10);
		map.put("MA Manager",11);
		map.put("Super Admin",12);
		map.put("Government Admin",13);
		map.put("JCB",14);
		map.put("Dealer",15);
		map.put("Customer",16);
		return map.get(roleName);
	}

}
