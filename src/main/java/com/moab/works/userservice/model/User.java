package com.moab.works.userservice.model;


import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "user")
public class User {
	
	@Id
	private String id;
	private String name;
	private String email; 	
	private String phone;
	private String address;
	private String password;
	@DBRef
	private Set<Role> role;
	
}
