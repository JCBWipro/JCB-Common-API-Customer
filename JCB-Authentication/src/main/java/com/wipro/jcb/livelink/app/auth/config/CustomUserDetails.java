package com.wipro.jcb.livelink.app.auth.config;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.wipro.jcb.livelink.app.auth.entity.UserRegistration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails{
	
	@Serial
    private static final long serialVersionUID = 1L;
	private final String username;
	private final String password;
	private final List<GrantedAuthority> authorities;
	

	public CustomUserDetails(UserRegistration userRegistration) {
		super();
		this.username = userRegistration.getFirstName();
		this.password = userRegistration.getPassword();
		this.authorities = Arrays.stream(userRegistration.getRole().toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
