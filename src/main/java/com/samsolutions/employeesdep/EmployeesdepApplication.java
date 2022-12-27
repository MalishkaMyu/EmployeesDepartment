package com.samsolutions.employeesdep;

import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class EmployeesdepApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesdepApplication.class, args);
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void generateAdminPassword() {
		User userAdmin = userRepository.findByLogin("admin");
		if (userAdmin.getId() != null && userAdmin.getPasswordHash().isBlank()) {
			// generate password for user 'admin'
			int length = 10;
			boolean useLetters = true;
			boolean useNumbers = true;
			String generatedPassword = RandomStringUtils.random(length, useLetters, useNumbers);
			System.out.println("Remember generated password for user 'admin':" + generatedPassword);
			//String passwordHash = encoder().encode(generatedPassword);
			//userAdmin.setPasswordHash(passwordHash);
			userRepository.saveAndFlush(userAdmin);
		}
	}
}
