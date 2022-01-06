package com.moab.works.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moab.works.userservice.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	/*
	@Query(value = "{'name': {$regex: ?0 } }", sort = "{name:1}") //ASC
	Page<User> getUsersByNameRegex(
			@Param("name") String name,
			Pageable pageable);
	*/
	@Query(value = "{'name': {$regex: ?0 } }")
	List<User> getUsersByNameRegex(@Param("name") String name);
	
	Optional<User> findByEmail(String email);
	
}
