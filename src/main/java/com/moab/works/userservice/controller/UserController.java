package com.moab.works.userservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.moab.works.userservice.model.User;
import com.moab.works.userservice.services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User getUser(@PathVariable String id){
		return userService.getUser(id).orElseThrow();
	}
	
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
	
	@PutMapping("/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(@RequestBody User user, @PathVariable String id) {
		user.setId(id);
		return userService.updateUser(user);
	}
	
	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}
	
	@GetMapping("/users/search/{name}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Map<String, Object>> getAllUsersWithName(
			@PathVariable(required = false) String name,
		    @RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "3") int size){
		
		Map<String, Object> response = userService.getAllUsersWithName(name, page, size);
		if(response != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
}
