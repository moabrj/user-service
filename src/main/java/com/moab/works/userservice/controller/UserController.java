package com.moab.works.userservice.controller;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.moab.works.userservice.model.User;
import com.moab.works.userservice.services.ImplUserService;
import com.moab.works.userservice.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	
	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User getUser(@PathVariable String id){
		return userService.getUser(id).orElseThrow();
	}
	
	//@PostMapping("/users")
	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
	
	//@PutMapping("/users/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(@RequestBody User user, @PathVariable String id) {
		user.setId(new ObjectId(id));
		return userService.updateUser(user);
	}
	
	//@DeleteMapping("/users/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}
	
	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/search/{name}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Page<User>> getAllUsersWithName(
			@PathVariable(required = false) String name,
		    @RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "3") int size){
		
		Pageable pageable = PageRequest.of(page, size);
		
		return ResponseEntity.ok(userService.getAllUsersWithName(name, pageable));
	}
	
}
