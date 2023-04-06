package com.samsolutions.employeesdep;

import com.samsolutions.employeesdep.config.KeycloakServerProperties;
import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableConfigurationProperties({ KeycloakServerProperties.class })
public class EmployeesdepApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesdepApplication.class, args);
    }

    private static final Logger LOG = LoggerFactory.getLogger(EmployeesdepApplication.class);

    @Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;

    @Autowired
    private MyPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Bean
    ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(ServerProperties serverProperties,
                                                                               KeycloakServerProperties keycloakServerProperties) {

        return (evt) -> {

            Integer port = serverProperties.getPort();
            String keycloakContextPath = keycloakServerProperties.getContextPath();

            LOG.info("Embedded Keycloak started: http://localhost:{}{} to use keycloak", port, keycloakContextPath);
        };
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
                String passwordHash = encoder.encode(generatedPassword);
                userAdmin.setPasswordHash(passwordHash);
                userRepository.saveAndFlush(userAdmin);
            }
        }
    }
}
