package com.moab.works.userservice.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;

import com.moab.works.userservice.model.User;

public interface UserService {
	
	
	Optional<User> findByEmail(String email);

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	List<User> getAllUsers();

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	Optional<User> getUser(String id);

	@PreAuthorize("hasRole('ADMIN')")
	User createUser(User user);

	@PreAuthorize("hasRole('ADMIN')")
	User updateUser(User user);

	@PreAuthorize("hasRole('ADMIN')")
	void deleteUser(String id);

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	Map<String, Object> getAllUsersWithName(String name, int page, int size);
	
	User createUser(User user, int x);

}
