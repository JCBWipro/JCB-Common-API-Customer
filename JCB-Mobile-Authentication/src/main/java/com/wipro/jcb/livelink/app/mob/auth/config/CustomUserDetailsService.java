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
import com.wipro.jcb.livelink.app.mob.auth.response.UserResponse;


@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserResponse> listOfUsers = userRepository.findByUserName(username);
		User user = new User();
		for(UserResponse users : listOfUsers) {
			user.setUserName(users.getUSER_ID());
			user.setPassword(users.getpassword());
			user.setUserType(UserType.valueOf(users.getuserType()));
		}
		Optional<User> userDetails = Optional.of(user);
		return userDetails.map(CustomUserDetails::new)
				 .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
}
