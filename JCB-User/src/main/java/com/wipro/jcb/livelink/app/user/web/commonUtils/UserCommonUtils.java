package com.wipro.jcb.livelink.app.user.web.commonUtils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.user.web.dto.UserDetails;

public class UserCommonUtils {
	
	public static String getRolesByID(int roleId) {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "JCB Account");
		map.put(2, "JCB HO");
		map.put(3, "JCB RO");
		map.put(4, "Customer Care");
		map.put(5, "Dealer Admin");
		map.put(6, "Dealer");
		map.put(7, "Customer Fleet Manager");
		map.put(8, "Customer");
		map.put(9, "wipro personal");
		map.put(10, "Pricol");
		map.put(11, "MA Manager");
		map.put(12, "Super Admin");
		map.put(13, "Government Admin");
		map.put(14, "JCB");
		return map.get(roleId);
	}
	
	public static final String SUPER_ADMIN = "Super Admin";
	public static final String JCB_ACCOUNT = "JCB Account";
	public static final String JCB_HO = "JCB HO";
	public static final String JCB_RO = "JCB RO";
	public static final String DEALER = "Dealer";
	public static final String DEALER_ADMIN = "Dealer Admin";
	public static final String CUSTOMER = "Customer";
	public static final String CUSTOMER_CARE = "Customer Care";
	public static final String CUSTOMER_FLEET_MANAGER = "Customer Fleet Manager";
	public static final String MA_MANAGER = "MA Manager";
	
	public static UserDetails getUserDetails(String userDetails) {
		UserDetails userResponse = new Gson().fromJson(userDetails, UserDetails.class);
		int roleId = Integer.parseInt(userResponse.getRoleName());
		String roleName = UserCommonUtils.getRolesByID(roleId);
		userResponse.setRoleName(roleName);
		return userResponse;
	}

}
