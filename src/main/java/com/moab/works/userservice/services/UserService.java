package com.moab.works.userservice.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import com.moab.works.userservice.model.User;

public interface UserService {
	
	
	Optional<User> findByEmail(String email);

	List<User> getAllUsers();

	Optional<User> getUser(String id);

	User createUser(User user);

	User updateUser(User user);

	void deleteUser(String id);

	Page<User> getAllUsersWithName(String name, Pageable pageable);
	
}
