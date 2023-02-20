package com.harera.hayat.authorization.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;

    public KeycloakConfig(@Value("${keycloak.auth-server-url}") String serverUrl,
                    @Value("${keycloak.realm}") String realm,
                    @Value("${keycloak.credentials.client-id}") String clientId,
                    @Value("${keycloak.credentials.secret}") String clientSecret,
                    @Value("${keycloak.credentials.username}") String username,
                    @Value("${keycloak.credentials.password}") String password) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverUrl)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();
    }
}
