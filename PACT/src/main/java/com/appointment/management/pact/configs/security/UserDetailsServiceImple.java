package com.appointment.management.pact.configs.security;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsServiceImple implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUsernameOrEmail(username, username);
		if(user == null) {
			throw new UsernameNotFoundException("user Not Founds");
		}

		CustomUserDetails cusObject = new CustomUserDetails(user);
		return cusObject;
	}

}
