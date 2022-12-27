package com.samsolutions.employeesdep;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class EmployeesdepApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesdepApplication.class, args);
	}

	@PostConstruct
	public void generateAdminPassword() {
		int length = 10;
		boolean useLetters = true;
		boolean useNumbers = true;
		String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
		System.out.println(generatedString);
	}
}
