package com.moab.works.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moab.works.userservice.model.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

	Role findByRole(String role);
	
}
