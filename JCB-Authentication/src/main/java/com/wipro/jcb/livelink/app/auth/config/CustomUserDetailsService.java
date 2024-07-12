package com.wipro.jcb.livelink.app.auth.config;

import java.util.Optional;

import com.wipro.jcb.livelink.app.auth.entity.UserRegistration;
import com.wipro.jcb.livelink.app.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserRegistration> credential = userRepository.findByFirstName(username);
		return credential.map(CustomUserDetails::new)
				 .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}

}
