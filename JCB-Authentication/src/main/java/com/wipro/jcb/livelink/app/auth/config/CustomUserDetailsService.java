package com.wipro.jcb.livelink.app.auth.config;

import java.util.Optional;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private ContactRepo contactRepo;

	@Override
	public UserDetails loadUserByUsername(String contactId) throws UsernameNotFoundException {
		
		Optional<ContactEntity> credential = contactRepo.findByContact_id(contactId);
		return credential.map(CustomUserDetails::new)
				 .orElseThrow(() -> new UsernameNotFoundException("user not found " + contactId));
	}

}
