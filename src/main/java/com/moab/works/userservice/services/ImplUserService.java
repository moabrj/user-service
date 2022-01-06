package com.moab.works.userservice.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moab.works.userservice.model.Role;
import com.moab.works.userservice.model.User;
import com.moab.works.userservice.repository.RoleRepository;
import com.moab.works.userservice.repository.UserRepository;

@Service
public class ImplUserService implements UserService {

	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUser(String id) {
		return userRepository.findById(id);
	}

	@Override
	public User createUser(User user) {
		Optional<User> existing = userRepository.findByEmail(user.getEmail()); //username is setted as email
		existing.ifPresent(it-> {throw new IllegalArgumentException("user already exists: " + it.getEmail());});
		
		user.setPassword(encoder.encode(user.getPassword()));
	    Role userRole = roleRepository.findByRole("USER");
	    user.setRole(new HashSet<>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

	
	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}
	
	@Override
	public Map<String, Object> getAllUsersWithName(String name, int page, int size){
		
		List<User> users = new ArrayList<>();
		Pageable paging = PageRequest.of(page, size);
		Page<User> pageTuts = userRepository.search(name, paging);
	    users = pageTuts.getContent();
	      
	    if (users.isEmpty()) {
	    	return null;
	    }
  
	    Map<String, Object> response = new HashMap<>();
	    response.put("users", users);
	    response.put("currentPage", pageTuts.getNumber());
	    response.put("totalItems", pageTuts.getTotalElements());
	    response.put("totalPages", pageTuts.getTotalPages());
		
	    return response;
	    
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user, int x) {
		Optional<User> existing = userRepository.findByEmail(user.getEmail()); //username is setted as email
		existing.ifPresent(it-> {throw new IllegalArgumentException("user already exists: " + it.getEmail());});
		
		user.setPassword(encoder.encode(user.getPassword()));
	    Role userRole = roleRepository.findByRole("ADMIN");
	    user.setRole(new HashSet<>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}
	
}
