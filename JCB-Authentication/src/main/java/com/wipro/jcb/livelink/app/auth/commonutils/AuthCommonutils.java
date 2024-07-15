package com.wipro.jcb.livelink.app.auth.commonutils;

import java.util.HashMap;
import java.util.Map;

public class AuthCommonutils {

	public static String getRolesByID(String roleId) {
		Map<String, String> map = new HashMap<>();
		map.put("1", "JCB Account");
		map.put("6", "Dealer");
		map.put("8", "Customer");
		map.put("12", "Super Admin");
		return map.get(roleId);
	}

}
