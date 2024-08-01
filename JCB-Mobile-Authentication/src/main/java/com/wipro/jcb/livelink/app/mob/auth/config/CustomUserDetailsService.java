package com.wipro.jcb.livelink.app.mob.auth.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<Object[]> objects = userRepository.findByUserName(username);
		User user = new User();
		for(Object[] object : objects) {
			user.setUserName(object[0].toString());
			user.setPassword(object[1].toString());
			user.setUserType(UserType.valueOf(object[2].toString()));
		}
		Optional<User> userDetails = Optional.of(user);
		return userDetails.map(CustomUserDetails::new)
				 .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
}
