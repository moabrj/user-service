package com.moab.works.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "role")
public class Role {
	
	@Id
	private String id;
	private String role;
	
	public Role() {}
	
	public Role(String id, String role) {
		this.id = id;
		this.role = role;
	}
	
	public Role(String role) {
		this.role = role;
	}
	

}
