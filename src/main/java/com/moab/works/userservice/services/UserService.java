package com.moab.works.userservice.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moab.works.userservice.model.User;
import com.moab.works.userservice.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUser(String id) {
		return userRepository.findById(id);
	}

	public User createUser(User user) {
		user.setId(ObjectId.get().toHexString());
		return userRepository.save(user);
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}
	
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
	
	
}
