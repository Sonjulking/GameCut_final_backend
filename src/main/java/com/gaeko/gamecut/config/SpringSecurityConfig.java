package com.gaeko.gamecut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SpringSecurityConfig {
	
	 @Bean
	 public PasswordEncoder passwordEncoder() {
	     return new BCryptPasswordEncoder();
	 }
	
}
