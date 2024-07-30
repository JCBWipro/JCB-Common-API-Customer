package com.wipro.jcb.livelink.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.businessObject.UserDetailsBO;
import com.wipro.jcb.livelink.app.user.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.entity.RoleEntity;
import com.wipro.jcb.livelink.app.user.repo.ContactRepo;

@Service
public class UserDetailsBOService {
	
	@Autowired
	private ContactRepo contactRepo;
	
	public UserDetailsBO getUserDetails(String userName) {
		ContactEntity contactEntity = new ContactEntity();
		List<Object[]> repoResult = contactRepo.findContactByContactId(userName);
		for(Object[] obj : repoResult) {
			contactEntity.setContactId(obj[0].toString());
			contactEntity.setFirst_name(obj[1].toString());
			contactEntity.setLast_name(obj[2].toString());
			contactEntity.setIs_tenancy_admin((int)obj[3]);
			contactEntity.setSysGeneratedPassword((int)obj[4]);
			RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRole_id((int) obj[5]);
            contactEntity.setRole(roleEntity);
		}
		UserDetailsBO userDetailsBO = new UserDetailsBO();
		userDetailsBO.setContact(contactEntity);
		userDetailsBO.setAccount_id(contactRepo.getAccountObjByUsername(userName));
		return userDetailsBO;
	}

}
