package com.moab.works.userservice.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moab.works.userservice.model.Role;
import com.moab.works.userservice.model.User;
import com.moab.works.userservice.model.exceptions.UserNotFoundException;
import com.moab.works.userservice.repository.RoleRepository;
import com.moab.works.userservice.repository.UserRepository;

@Service
public class ImplUserService implements UserService {

	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MongoTemplate mongoTemplate; 

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
		
		if(user.getId() == null)
			user.setId(new ObjectId().toHexString());
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		String role = "USER";
		List<Role> roles = user.getRole().stream().collect(Collectors.toList());
		if(!roles.isEmpty())
			role = roles.get(0).getRole();
			
	    Role userRole = roleRepository.findByRole(role);
	    user.setRole(new HashSet<>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

	
	@Override
	public User updateUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		String role = "USER";
		List<Role> roles = user.getRole().stream().collect(Collectors.toList());
		if(!roles.isEmpty())
			role = roles.get(0).getRole();
			
	    Role userRole = roleRepository.findByRole(role);
	    user.setRole(new HashSet<>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(String id) {
		Optional<User> existing = userRepository.findById(id);
		if(existing.isPresent())
			userRepository.deleteById(id);
		else
			throw new UserNotFoundException();
	}
	
	@Override
	public Page<User> getAllUsersWithName(String name, Pageable pageable){
		var query = new Query().with(pageable);
        final List<Criteria> criteria = new ArrayList<>();

        if (name != null && !name.isBlank())
            criteria.add(Criteria.where("name").regex(name, "i"));
        
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, User.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), User.class)
        );
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public void initUser(User user) {
		user.setId(new ObjectId().toHexString());
		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("ADMIN");
	    user.setRole(new HashSet<>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

}
