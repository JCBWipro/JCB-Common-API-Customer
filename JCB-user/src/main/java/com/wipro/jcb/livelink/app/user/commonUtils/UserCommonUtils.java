package com.wipro.jcb.livelink.app.user.commonUtils;

import java.util.HashMap;
import java.util.Map;

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
        return map.get(roleId);
    }

}
