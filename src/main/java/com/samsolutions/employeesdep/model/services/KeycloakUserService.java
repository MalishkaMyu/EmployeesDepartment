package com.samsolutions.employeesdep.model.services;

import com.samsolutions.employeesdep.model.dto.UserKeycloakDTO;

public interface KeycloakUserService {
    UserKeycloakDTO createKeycloakUser(UserKeycloakDTO userDTO);

    UserKeycloakDTO updateKeycloakUser(UserKeycloakDTO userDTO);

    boolean deleteKeycloakUser(String keycloakID);

    String getKeycloakIDByLogin(String login);
}
