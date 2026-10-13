package com.appplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
      
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		    .authorizeHttpRequests(auth -> auth
		    		.requestMatchers("/hello").permitAll()
		    		.requestMatchers("/hi").authenticated()
		    		.anyRequest().permitAll()
		    		)
		   // .httpBasic(Customizer.withDefaults()) // no form login
		   // .formLogin(form -> form.disable());
		    
		    // here we can get the login form
		    .formLogin(Customizer.withDefaults());
		
		return http.build();
		    
	}
}
