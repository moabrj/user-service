package com.moab.works.userservice.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.moab.works.userservice.model.Role;
import com.moab.works.userservice.model.User;
import com.moab.works.userservice.model.exceptions.UserNotFoundException;

public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserService userService;
//	@Autowired
//	private RoleRepository roleRepository;
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UserNotFoundException {

	    User user = userService.findByEmail(email).get();
	    if(user != null) {
	        List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
	        return buildUserForAuthentication(user, authorities);
	    } else {
	        throw new UserNotFoundException("User not found");
	    }
	}
	
	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
	    Set<GrantedAuthority> roles = new HashSet<>();
	    userRoles.forEach((role) -> {
	        roles.add(new SimpleGrantedAuthority(role.getRole()));
	    });

	    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
	    return grantedAuthorities;
	}
	
	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}
