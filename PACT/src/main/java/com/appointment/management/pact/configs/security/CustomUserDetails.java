package com.appointment.management.pact.configs.security;

import java.util.Collection;
import java.util.List;

import com.appointment.management.pact.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;

	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantAdmin = new SimpleGrantedAuthority(user.getRole());
		return List.of( simpleGrantAdmin);
	}

	@Override
	public String getPassword() {		
		return user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {		
		return user.getIsActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getIsVerified();
	}

	public User getUser(){
		return user;
	}
}
