package com.wipro.jcb.livelink.app.user.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.web.repo.TenancyRepo;
import com.wipro.jcb.livelink.app.user.web.reponse.AccountTenancyReponse;

@Service
public class PseudoAccessListService {

	@Autowired
	private TenancyRepo tenancyRepo;

	public List<HashMap<String, String>> getPseudoTenancyList() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> records = null;
		try {
			List<AccountTenancyReponse> repoResult = tenancyRepo.getPseudoTenancyList();
			
			for (AccountTenancyReponse obj : repoResult) {
				records = new HashMap<String, String>();
				records.put("Account_ID",   obj.getAccountId());
				records.put("Account_Name", obj.getAccountName());
				records.put("Account_Code", obj.getAccountCode());
				records.put("mapping_code", obj.getMappingCode());
				records.put("Tenancy_ID",   obj.getTenancyId());
				records.put("Tenancy_Name", obj.getTenancyName());
				list.add(records);
			}
			System.out.println("Length received is " + list.size());
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		return list;
	}

}
