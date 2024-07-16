package com.wipro.jcb.livelink.app.auth.commonutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.RoleEntity;

public class AuthCommonutils {

	public static String getRolesByID(String roleId) {
		Map<String, String> map = new HashMap<>();
		map.put("1", "JCB Account");
		map.put("6", "Dealer");
		map.put("8", "Customer");
		map.put("12", "Super Admin");
		return map.get(roleId);
	}
	
	public static ContactEntity convertObjectToDTO(List<Object[]> objects) {
		ContactEntity contactEntity = new ContactEntity();
		for(Object[] object : objects) {
			contactEntity.setContactId(object[0].toString());
			contactEntity.setPassword(object[1].toString());
			RoleEntity roleEntity = new RoleEntity();
			roleEntity.setRole_id((int)object[2]);
			contactEntity.setRole(roleEntity);
		}
		return contactEntity;
	}

}
