/**
 * 
 */
package com.samsolutions.employeesdep.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alec Kotovich
 *
 */
@Configuration
public class EmbeddedKeycloakDataSource {

	@Bean
	@ConfigurationProperties("spring.datasource.keycloak")
	public DataSourceProperties keycloakDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource keycloakDataSource() {
		return keycloakDataSourceProperties()
				.initializeDataSourceBuilder()
				.build();
	}
}
