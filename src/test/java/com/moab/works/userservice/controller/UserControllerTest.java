package com.moab.works.userservice.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moab.works.userservice.model.Role;
import com.moab.works.userservice.model.User;
import com.moab.works.userservice.services.UserService;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class UserControllerTest {
	
	@Autowired
    protected MockMvc mockMvc;
	@Autowired
	UserService userservice;
	@Autowired
    private ObjectMapper objectMapper;
	
	
	private User createUser(String id, String name, String email, String password, String role) {
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		Set<Role> roles = new HashSet<Role>();
		Role r = new Role();
		r.setId(id);
		r.setRole(role);
		roles.add(r);
		user.setRole(roles);
		return user;
	}
	
	@Test
    public void shouldAddUser() throws Exception {
		String name = new ObjectId().toHexString();
		String json = "{\n"
				+ "    \"name\": \""+ name +"\",\n"
				+ "    \"email\": \""+name+"@gmail.com\",\n"
				+ "    \"phone\": \"123456\",\n"
				+ "    \"address\": \"Rua Y numnero x\",\n"
				+ "    \"password\": \"123456\",\n"
				+ "    \"role\": [\n"
				+ "            {\n"
				+ "                \"role\": \"USER\"\n"
				+ "            }\n"
				+ "        ]\n"
				+ "}";
		
		User userExpected = createUser("123456", name, name+"@gmail.com", "123456", "USER");
		
		mockMvc.perform(post("/users")
				.with(httpBasic("admin@admin.com", "123456"))
				.accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is(userExpected.getName())))
                .andExpect(jsonPath("email", is(userExpected.getEmail())));
	}
	
	@Test
    public void shouldReturnUserById() throws Exception {		
		mockMvc.perform(get("/users/61d8481e35ed5a6dd8fe0648")
				.with(httpBasic("admin@admin.com", "123456"))
				.accept(APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(APPLICATION_JSON))
        		.andExpect(jsonPath("$.name", is("Jonas")))
        		.andExpect(jsonPath("$.email", is("jonas@gmail.com")))
        		.andExpect(jsonPath("$.phone", is("123456")));
		
	}
	
	@Test
    public void shouldNotReturnUserById() throws Exception {
		mockMvc.perform(get("/users/8888")
				.with(httpBasic("admin@admin.com", "123456"))
				.accept(APPLICATION_JSON))
        		.andExpect(status().isNotFound());
	}
	
	@Test
    public void shouldDeleteUserById() throws Exception {
		String name = new ObjectId().toHexString();
		String json = "{\n"
				+ "    \"name\": \""+ name +"\",\n"
				+ "    \"email\": \""+name+"@gmail.com\",\n"
				+ "    \"phone\": \"123456\",\n"
				+ "    \"address\": \"Rua Y numnero x\",\n"
				+ "    \"password\": \"123456\",\n"
				+ "    \"role\": [\n"
				+ "            {\n"
				+ "                \"role\": \"USER\"\n"
				+ "            }\n"
				+ "        ]\n"
				+ "}";
		
		User userExpected = createUser("123456", name, name+"@gmail.com", "123456", "USER");
		
		MvcResult result = mockMvc.perform(post("/users")
				.with(httpBasic("admin@admin.com", "123456"))
				.accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is(userExpected.getName())))
                .andExpect(jsonPath("email", is(userExpected.getEmail()))).andReturn();
		
		String object = result.getResponse().getContentAsString();
		User user = objectMapper.readValue(object, User.class);
		mockMvc.perform(get("/users/"+user.getId())
				.with(httpBasic("admin@admin.com", "123456"))
				.accept(APPLICATION_JSON))
        		.andExpect(status().isOk());
	}
	
	@Test
	public void shouldNotAllowUserCreation() throws Exception {
		String name = new ObjectId().toHexString();
		String json = "{\n"
				+ "    \"name\": \""+ name +"\",\n"
				+ "    \"email\": \""+name+"@gmail.com\",\n"
				+ "    \"phone\": \"123456\",\n"
				+ "    \"address\": \"Rua Y numnero x\",\n"
				+ "    \"password\": \"123456\",\n"
				+ "    \"role\": [\n"
				+ "            {\n"
				+ "                \"role\": \"USER\"\n"
				+ "            }\n"
				+ "        ]\n"
				+ "}";
		
		mockMvc.perform(post("/users")
				.with(httpBasic("ana@gmail.com", "123456"))
				.accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
		
	}
	
	@Test
	public void shouldNotAuthorizeUserAction() throws Exception {
		mockMvc.perform(get("/users")
				.with(httpBasic("xxx@yyy.com", "123456"))
				.accept(APPLICATION_JSON))
        		.andExpect(status().isUnauthorized());
	}

}
