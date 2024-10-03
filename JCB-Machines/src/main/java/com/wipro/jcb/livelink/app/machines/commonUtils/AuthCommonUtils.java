package com.wipro.jcb.livelink.app.machines.commonUtils;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-09-2024
 * project: JCB-Common-API-Customer
 */
public class AuthCommonUtils {

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

    public static UserDetails getUserDetails(String userDetails) {
        UserDetails userResponse = new Gson().fromJson(userDetails, UserDetails.class);
        int roleId = Integer.parseInt(userResponse.getRoleName());
        String roleName = AuthCommonUtils.getRolesByID(roleId);
        userResponse.setRoleName(roleName);
        return userResponse;
    }

}
