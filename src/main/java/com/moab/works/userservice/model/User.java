package com.moab.works.userservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Users")
public class User {
	
	@Id
	private String id;
	private String name;
	private String email; 	
	private String phone;
	private String address;
	private String password;
	private Profile profile;
	
}
