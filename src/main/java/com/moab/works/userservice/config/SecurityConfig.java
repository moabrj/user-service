package com.moab.works.userservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moab.works.userservice.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService userDetailsService = mongoUserDetails();
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
		
		System.out.println(userDetailsService.loadUserByUsername("admin@admin.com").toString());
//		PasswordEncoder encoder = passwordEncoder();
//		    	auth
//		          .inMemoryAuthentication()
//		          .withUser("user")
//		          .password(encoder.encode("password"))
//		          .roles("USER")
//		          .and()
//		          .withUser("admin")
//		          .password(encoder.encode("admin"))
//		          .roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		http
	    .csrf().disable()
	    .authorizeRequests()
	    .antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ADMIN", "USER") // Admin should be able to delete
	    .antMatchers(HttpMethod.GET, "/users/{id}").hasAnyAuthority("ADMIN", "USER")
	    .antMatchers(HttpMethod.GET, "/users/search/**").hasAnyAuthority("ADMIN", "USER")
	    .antMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
	    .antMatchers(HttpMethod.PUT, "/users/{id}").hasAuthority("ADMIN")
	    .antMatchers(HttpMethod.DELETE, "/users/{id}").hasAuthority("ADMIN")
	    .anyRequest().authenticated()
	    .and().httpBasic()
	    .and().sessionManagement().disable();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService mongoUserDetails() {
	    return new CustomUserDetailsService();
	}
}
