package com.wipro.jcb.livelink.app.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.businessObject.UserDetailsBO;
import com.wipro.jcb.livelink.app.user.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.entity.RoleEntity;
import com.wipro.jcb.livelink.app.user.repo.ContactRepo;
import com.wipro.jcb.livelink.app.user.reponse.UserDetailsReponse;

@Service
public class UserDetailsBOService {
	
	@Autowired
	private ContactRepo contactRepo;
	
	public UserDetailsBO getUserDetails(String userName) {
		UserDetailsReponse repoResult = contactRepo.findContactByContactId(userName);
		
		ContactEntity contactEntity = new ContactEntity();
		contactEntity.setContactId(repoResult.getContactId());
		contactEntity.setFirst_name(repoResult.getFirstname());
		contactEntity.setLast_name(repoResult.getLastname());
		contactEntity.setIs_tenancy_admin(repoResult.getIsTenancyAdmin());
		contactEntity.setSysGeneratedPassword(repoResult.getSysGeneratedPassword());
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setRole_id(repoResult.getRoleId());
		contactEntity.setRole(roleEntity);

		UserDetailsBO userDetailsBO = new UserDetailsBO();
		userDetailsBO.setContact(contactEntity);
		userDetailsBO.setAccount_id(contactRepo.getAccountDetailsByUsername(userName));
		
		return userDetailsBO;
	}

}