package com.wipro.jcb.livelink.app.auth.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.reponse.ContactResponse;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	ContactRepo contactRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ContactResponse credential = contactRepo.findByContactId(username);
		ContactEntity contactEntity = AuthCommonutils.convertObjectToDTO(credential);
		Optional<ContactEntity> optional = Optional.of(contactEntity);
		return optional.map(CustomUserDetails::new)
				 .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
}
