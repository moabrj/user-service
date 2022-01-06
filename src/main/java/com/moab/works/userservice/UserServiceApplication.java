package com.moab.works.userservice;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.moab.works.userservice.model.Role;
import com.moab.works.userservice.model.User;
import com.moab.works.userservice.repository.RoleRepository;
import com.moab.works.userservice.repository.UserRepository;
import com.moab.works.userservice.services.UserService;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(RoleRepository roleRepository, UserService userService) {

	    return args -> {

	        Role adminRole = roleRepository.findByRole("ADMIN");
	        if (adminRole == null) {
	            Role newAdminRole = new Role();
	            newAdminRole.setRole("ADMIN");
	            roleRepository.save(newAdminRole);
	        }

	        Role userRole = roleRepository.findByRole("USER");
	        if (userRole == null) {
	            Role newUserRole = new Role();
	            newUserRole.setRole("USER");
	            roleRepository.save(newUserRole);
	        }
	        
	        Optional<User> exist = userService.findByEmail("admin@admin.com");
	        if(!exist.isPresent()) {
	        	User user = new User();
	        	user.setPassword("123456");
	        	user.setName("ADMIN");
	        	user.setEmail("admin@admin.com");
	        	userService.createUser(user);
	        }
	    };

	}

}
