package com.samsolutions.employeesdep;

import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.model.dto.UserKeycloakDTO;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import com.samsolutions.employeesdep.model.services.KeycloakUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class EmployeesdepApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesdepApplication.class, args);
    }

    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;

    @Value("${spring.flyway.placeholders.admin_email}")
    private String adminEmail;

    @Autowired
    private MyPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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

                // create keycloak user for 'admin', if not exists
                String adminKeycloakId = keycloakUserService.getKeycloakIDByLogin(adminLogin);
                if (adminKeycloakId == null) {
                    UserKeycloakDTO userKeycloakDTO = new UserKeycloakDTO(adminLogin,
                            generatedPassword, adminEmail, "Admin", "Admin");
                    userKeycloakDTO.setRoles(Collections.singletonList("admin"));
                    UserKeycloakDTO savedUserKeycloakDTO = keycloakUserService.createKeycloakUser(userKeycloakDTO);
                    adminKeycloakId = savedUserKeycloakDTO.getKeycloakId();
                }

                // saving admin user with password and keycloak-id
                String passwordHash = encoder.encode(generatedPassword);
                userAdmin.setPasswordHash(passwordHash);
                userAdmin.setKeycloakId(adminKeycloakId);
                userRepository.saveAndFlush(userAdmin);
            }
        }
    }
}
