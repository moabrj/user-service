package com.moab.works.userservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moab.works.userservice.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService userDetailsService = mongoUserDetails();
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
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
