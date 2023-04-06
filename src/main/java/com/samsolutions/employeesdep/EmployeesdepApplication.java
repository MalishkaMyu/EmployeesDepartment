package com.samsolutions.employeesdep;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.Config;
import org.keycloak.exportimport.ExportImportManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.samsolutions.employeesdep.config.KeycloakServerProperties;
import com.samsolutions.employeesdep.config.MyPasswordEncoder;
import com.samsolutions.employeesdep.config.RegularJsonConfigProviderFactory;
import com.samsolutions.employeesdep.model.entities.User;
import com.samsolutions.employeesdep.model.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableConfigurationProperties({ KeycloakServerProperties.class })
@Slf4j
public class EmployeesdepApplication extends KeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesdepApplication.class, args);
	}

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

			log.info("Embedded Keycloak started: http://localhost:{}{} to use keycloak", port, keycloakContextPath);
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

	/////////////////////////////////////////////////// KEYCLOAK
	/////////////////////////////////////////////////// /////////////////////////////////////////////////////////////////
	public static KeycloakServerProperties keycloakServerProperties;

	@Override
	protected void loadConfig() {
		JsonConfigProviderFactory factory = new RegularJsonConfigProviderFactory();
		Config.init(factory.create()
				.orElseThrow(() -> new NoSuchElementException("No value present")));
	}

	@Override
	protected ExportImportManager bootstrap() {
		final ExportImportManager exportImportManager = super.bootstrap();
		createMasterRealmAdminUser();
		createEmployeesDepRealm();
		return exportImportManager;
	}

	private void createMasterRealmAdminUser() {
		KeycloakSession session = getSessionFactory().create();
		ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
		KeycloakServerProperties.AdminUser admin = keycloakServerProperties.getAdminUser();
		try {
			session.getTransactionManager().begin();
			applianceBootstrap.createMasterRealmUser(admin.getUsername(), admin.getPassword());
			session.getTransactionManager().commit();
		} catch (Exception ex) {
			log.warn("Couldn't create keycloak master admin user: {}", ex.getMessage());
			session.getTransactionManager().rollback();
		}
		session.close();
	}

	private void createEmployeesDepRealm() {
		KeycloakSession session = getSessionFactory().create();
		try {
			session.getTransactionManager().begin();
			RealmManager manager = new RealmManager(session);
			Resource lessonRealmImportFile = new ClassPathResource(
					keycloakServerProperties.getRealmImportFile());
			manager.importRealm(JsonSerialization.readValue(lessonRealmImportFile.getInputStream(),
					RealmRepresentation.class));
			session.getTransactionManager().commit();
		} catch (Exception ex) {
			log.warn("Failed to import Realm json file: {}", ex.getMessage());
			session.getTransactionManager().rollback();
		}
		session.close();
	}
}
