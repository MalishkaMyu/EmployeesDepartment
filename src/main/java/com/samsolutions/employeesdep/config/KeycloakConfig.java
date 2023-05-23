package com.samsolutions.employeesdep.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Keycloak Spring configuration
 */
@KeycloakConfiguration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String kcHost;

    @Value("${keycloak.realm}")
    private String kcRealm;

    @Value("${keycloak.resource}")
    private String kcClientId;

    @Value("${keycloak.credentials.secret}")
    private String kcClientSecret;

    @Bean
    public Keycloak getKeycloakServiceAccount(KeycloakSpringBootProperties props) {
        return KeycloakBuilder.builder()
                .serverUrl(kcHost)
                .realm(kcRealm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(kcClientId)
                .clientSecret(kcClientSecret)
                .build();
    }

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}