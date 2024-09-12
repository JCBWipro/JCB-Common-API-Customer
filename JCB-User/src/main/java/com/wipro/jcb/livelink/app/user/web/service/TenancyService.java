package com.wipro.jcb.livelink.app.user.web.service;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.web.businessObject.TenancyBO;
import com.wipro.jcb.livelink.app.user.web.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.web.entity.AccountTenancyMapping;
import com.wipro.jcb.livelink.app.user.web.entity.TenancyEntity;
import com.wipro.jcb.livelink.app.user.web.repo.AccountTenancyMappingRepo;

@Service
public class TenancyService {
	
	@Autowired
	private AccountTenancyMappingRepo accountTenancyMappingRepo;
	
	public TenancyBO getUserTenancy(String loginId, int accountId) {
		
		TreeMap<Integer,String> tenancyIdProxyUser = new TreeMap<Integer,String>();
		TreeMap<String, TreeMap<Integer,String>> tenancyNameIDProxyUser = new TreeMap<String, TreeMap<Integer,String>>();
		List<AccountTenancyMapping> listOfAccountTenancyMapping = accountTenancyMappingRepo.findAccountAndTenancyID(accountId);
		
		TenancyBO tenancyBO = new TenancyBO();
		String tenancyName = null;
		int first=0;
		
		for(AccountTenancyMapping accountTenancyMapping : listOfAccountTenancyMapping) {
			TenancyEntity tenancyEntity = accountTenancyMapping.getTenancy();
			tenancyName = tenancyEntity.getTenancy_name();
			tenancyIdProxyUser.put(tenancyEntity.getTenancy_id(), loginId);
			
			if(first==0) {
				tenancyNameIDProxyUser.put(tenancyName, tenancyIdProxyUser);
			} else {
				tenancyNameIDProxyUser.put(tenancyName+" - "+(first+1), tenancyIdProxyUser);
			}
			first++;
		}
		
		tenancyBO.setTenancyNameIDProxyUser(tenancyNameIDProxyUser);
		return tenancyBO;
		
	}
	
	public UserAuthenticationRespContract getTenancyObj(TenancyBO tenancy, UserAuthenticationRespContract userAuthenticationResponse) {
		List<String> nameIdProxyUserList = new LinkedList<String>();
		TreeMap<String,TreeMap<Integer,String>> nameIdProxyUser = tenancy.getTenancyNameIDProxyUser();
		for(int j=0; j< nameIdProxyUser.size(); j++)
		{
			String finalNameIdProxyUserString = null;
			String name = (String)nameIdProxyUser.keySet().toArray()[j];
			finalNameIdProxyUserString = name;
			TreeMap<Integer,String> idProxyUser = (TreeMap<Integer,String>)nameIdProxyUser.values().toArray()[j];
			for(int k=0; k<idProxyUser.size(); k++)
			{
				int tenancyId = (Integer) idProxyUser.keySet().toArray()[k];
				String proxyUser = (String)idProxyUser.values().toArray()[k];
				finalNameIdProxyUserString = finalNameIdProxyUserString+","+tenancyId+","+proxyUser;

			}
			nameIdProxyUserList.add(finalNameIdProxyUserString);
		}	
		userAuthenticationResponse.setTenancyNameIDProxyUser(nameIdProxyUserList);
		return userAuthenticationResponse;
	}

}
