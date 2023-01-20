package com.samsolutions.employeesdep;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class EmployeesdepApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesdepApplication.class, args);
	}

	@Value("${spring.flyway.placeholders.admin_login}")
	private String adminLogin;

	@Autowired
    private MyPasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void generateAdminPassword() {
		if (userRepository.existsByLogin(adminLogin)) {
			User userAdmin = userRepository.findByLogin(adminLogin).orElse(null);
			if (userAdmin != null && userAdmin.getPasswordHash().isBlank()) {
				// generate password for user 'admin'
				int length = 10;
				boolean useLetters = true;
				boolean useNumbers = true;
				String generatedPassword = RandomStringUtils.random(length, useLetters, useNumbers);
				System.out.println("Remember generated password for user '" + adminLogin + "':" + generatedPassword);
				String passwordHash = encoder.encode(generatedPassword);
				userAdmin.setPasswordHash(passwordHash);
				userRepository.saveAndFlush(userAdmin);
			}
		}
	}
}
