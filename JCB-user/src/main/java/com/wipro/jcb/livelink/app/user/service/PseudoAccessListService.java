package com.wipro.jcb.livelink.app.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.repo.TenancyRepo;

@Service
public class PseudoAccessListService {

	@Autowired
	private TenancyRepo tenancyRepo;

	public List<HashMap<String, String>> getPseudoTenancyList() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> records = null;
		try {
			List<Object[]> repoResult = tenancyRepo.getPseudoTenancyList();
			
			for (Object[] obj : repoResult) {
				records = new HashMap<String, String>();
				records.put("Account_ID",   obj[0].toString());
				records.put("Account_Name", obj[1].toString());
				records.put("Account_Code", obj[2].toString());
				records.put("mapping_code", obj[3].toString());
				records.put("Tenancy_ID",   obj[4].toString());
				records.put("Tenancy_Name", obj[5].toString());
				list.add(records);
			}
			System.out.println("Length received is " + list.size());
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		return list;
	}

}
